package crowdfunding.handler;

import crowdfunding.entity.vo.AddressVO;
import crowdfunding.entity.vo.OrderProjectVO;
import crowdfunding.entity.vo.OrderVO;
import crowdfunding.service.OrderService;
import crowdfunding.util.ResultEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dell
 */
@RestController
public class OrderHandler {

    @Resource
    private OrderService orderService;

    @RequestMapping("/get/order/project/VO/remote/{projectId}/{returnId}")
    public ResultEntity<OrderProjectVO> getOrderProjectVORemote(@PathVariable("projectId") Integer projectId,
                                                                @PathVariable("returnId") Integer returnId) {
        try {
            OrderProjectVO orderProjectVO = orderService.getOrderProjectVO(projectId, returnId);
            return ResultEntity.successWithData(orderProjectVO);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/get/address/vo/remote")
    public ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberLoginVOId")Integer memberLoginVOId) {
        try {
            List<AddressVO> addressVOList = orderService.getAddressVOList(memberLoginVOId);
            return ResultEntity.successWithData(addressVOList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/save/address/remote")
    public ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO) {
        try {
            orderService.saveAddress(addressVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

    @RequestMapping("/save/order/remote")
    public ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVO) {
        try {
            orderService.saveOrder(orderVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }

}
