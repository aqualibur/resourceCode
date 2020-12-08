package crowdfunding.handler;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import crowdfunding.api.MysqlRemoteService;
import crowdfunding.config.PaymentProperties;
import crowdfunding.entity.vo.OrderProjectVO;
import crowdfunding.entity.vo.OrderVO;
import crowdfunding.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author dell
 */
@Controller
@Slf4j
public class PaymentHandler {

    @Resource
    private PaymentProperties paymentProperties;

    @Resource
    private MysqlRemoteService mysqlRemoteService;

    @ResponseBody
    @RequestMapping("/generate/order")
    public String generateOrder(OrderVO orderVO, HttpSession httpSession) throws AlipayApiException {
        OrderProjectVO orderProjectVO = (OrderProjectVO) httpSession.getAttribute("orderProjectVO");
        orderVO.setOrderProjectVO(orderProjectVO);
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String userId = UUID.randomUUID().toString().replace("-", "").toUpperCase();
        String orderNum = time + userId;
        orderVO.setOrderNum(orderNum);
        Double orderAmount = (double) (orderProjectVO.getSupportPrice() * orderProjectVO.getReturnCount() + orderProjectVO.getFreight());
        orderVO.setOrderAmount(orderAmount);
        httpSession.setAttribute("orderVO", orderVO);
        return sendRequestToAlipay(orderNum, orderAmount, orderProjectVO.getProjectName(), orderProjectVO.getReturnContent());
    }

    @RequestMapping("/notify")
    public void notifyUrlMethod(HttpServletRequest request) throws AlipayApiException {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(
                params,
                paymentProperties.getAlipayPublicKey(),
                paymentProperties.getCharset(),
                paymentProperties.getSignType());
        /* 实际验证过程建议商户务必添加以下校验：
        1、需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
        2、判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
        3、校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email）
        4、验证app_id是否为该商户本身。
        */
        if (!signVerified) {
            //验证失败
            log.info("验证失败");
            //out.println("fail");
        } else {
            //验证成功
            //商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            log.info("out_trade_no=" + out_trade_no);
            log.info("trade_no=" + trade_no);
            log.info("trade_status=" + trade_status);
            //out.println("success");
        }
    }

    @ResponseBody
    @RequestMapping("/return")
    public String returnUrlMethod(HttpServletRequest request, HttpSession httpSession) throws AlipayApiException {
        //获取支付宝GET过来反馈信息
        Map<String,String> params = new HashMap<>();
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }
        //调用SDK验证签名
        boolean signVerified = AlipaySignature.rsaCheckV1(
                params,
                paymentProperties.getAlipayPublicKey(),
                paymentProperties.getCharset(),
                paymentProperties.getSignType());
        if(signVerified) {
            //商户订单号
            String orderNum = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //支付宝交易号
            String payOrderNum = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            //付款金额
            String orderAmount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            OrderVO orderVO = (OrderVO) httpSession.getAttribute("orderVO");
            orderVO.setPayOrderNum(payOrderNum);
            ResultEntity<String> resultEntity = mysqlRemoteService.saveOrderRemote(orderVO);
            log.info("save result=" + resultEntity.getResult());
            return "trade_no:" + payOrderNum + "<br/>out_trade_no:" + orderNum + "<br/>total_amount:" + orderAmount;
        }else {
            return "验签失败";
        }
    }

    private String sendRequestToAlipay(String outTradeNo, Double totalAmount, String subject, String body) throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                paymentProperties.getGatewayUrl(),
                paymentProperties.getAppId(),
                paymentProperties.getMerchantPrivateKey(),
                "json",
                paymentProperties.getCharset(),
                paymentProperties.getAlipayPublicKey(),
                paymentProperties.getSignType());
        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(paymentProperties.getReturnUrl());
        alipayRequest.setNotifyUrl(paymentProperties.getNotifyUrl());
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ outTradeNo +"\","
                + "\"total_amount\":\""+ totalAmount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
        //请求
        return alipayClient.pageExecute(alipayRequest).getBody();
    }

}
