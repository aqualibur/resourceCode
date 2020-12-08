package crowdfunding.service;

import crowdfunding.entity.vo.AddressVO;
import crowdfunding.entity.vo.OrderProjectVO;
import crowdfunding.entity.vo.OrderVO;

import java.util.List;

/**
 * @author dell
 */
public interface OrderService {

    OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId);

    List<AddressVO> getAddressVOList(Integer memberLoginVOId);

    void saveAddress(AddressVO addressVO);

    void saveOrder(OrderVO orderVO);

}
