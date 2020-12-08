package crowdfunding.handler;

import crowdfunding.api.MysqlRemoteService;
import crowdfunding.constant.CrowdFundingConstant;
import crowdfunding.entity.vo.AddressVO;
import crowdfunding.entity.vo.MemberLoginVO;
import crowdfunding.entity.vo.OrderProjectVO;
import crowdfunding.util.ResultEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author dell
 */
@Controller
@Slf4j
public class OrderHandler {

    @Resource
    private MysqlRemoteService mysqlRemoteService;

    @RequestMapping("/return/confirm/info/{projectId}/{returnId}")
    public String showReturnConfirmInfo(@PathVariable("projectId") Integer projectId,
                                        @PathVariable("returnId") Integer returnId,
                                        HttpSession httpSession) {
        ResultEntity<OrderProjectVO> resultEntity = mysqlRemoteService.getOrderProjectVORemote(projectId, returnId);
        String result = resultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(result)) {
            OrderProjectVO queryData = resultEntity.getQueryData();
            httpSession.setAttribute("orderProjectVO", queryData);
        }
        return "confirm-return";
    }

    @RequestMapping("/confirm/order/{returnCount}")
    public String showConfirmOrderInfo(@PathVariable("returnCount") Integer returnCount,
                                       HttpSession httpSession) {
        OrderProjectVO orderProjectVO = (OrderProjectVO) httpSession.getAttribute("orderProjectVO");
        orderProjectVO.setReturnCount(returnCount);
        httpSession.setAttribute("orderProjectVO", orderProjectVO);
        MemberLoginVO memberLoginVO = (MemberLoginVO) httpSession.getAttribute(CrowdFundingConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberLoginVOId = memberLoginVO.getId();
        ResultEntity<List<AddressVO>> resultEntity = mysqlRemoteService.getAddressVORemote(memberLoginVOId);
        String result = resultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(result)) {
            List<AddressVO> queryData = resultEntity.getQueryData();
            httpSession.setAttribute("addressVOList", queryData);
        }
        return "confirm-order";
    }

    @RequestMapping("/save/address")
    public String saveAddress(AddressVO addressVO, HttpSession httpSession) {
        ResultEntity<String> resultEntity = mysqlRemoteService.saveAddressRemote(addressVO);
        log.debug(resultEntity.getResult());
        OrderProjectVO orderProjectVO = (OrderProjectVO) httpSession.getAttribute("orderProjectVO");
        Integer returnCount = orderProjectVO.getReturnCount();
        return "redirect:http://localhost:80/order/confirm/order/" + returnCount;
    }

}
