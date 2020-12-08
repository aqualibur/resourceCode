package crowdfunding.service.impl;

import crowdfunding.entity.po.AddressExample;
import crowdfunding.entity.po.AddressPO;
import crowdfunding.entity.po.OrderPO;
import crowdfunding.entity.po.OrderProjectPO;
import crowdfunding.entity.vo.AddressVO;
import crowdfunding.entity.vo.OrderProjectVO;
import crowdfunding.entity.vo.OrderVO;
import crowdfunding.mapper.AddressMapper;
import crowdfunding.mapper.OrderMapper;
import crowdfunding.mapper.OrderProjectMapper;
import crowdfunding.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dell
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private OrderProjectMapper orderProjectMapper;

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private AddressMapper addressMapper;

    @Override
    @Transactional(readOnly = true)
    public OrderProjectVO getOrderProjectVO(Integer projectId, Integer returnId) {
        return orderProjectMapper.selectOrderProjectVO(returnId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AddressVO> getAddressVOList(Integer memberLoginVOId) {
        AddressExample addressExample = new AddressExample();
        AddressExample.Criteria criteria = addressExample.createCriteria();
        criteria.andMemberIdEqualTo(memberLoginVOId);
        List<AddressPO> addressPOList = addressMapper.selectByExample(addressExample);
        List<AddressVO> addressVOList = new ArrayList<>();
        for (AddressPO addressPO : addressPOList) {
            if (addressPO != null) {
                AddressVO addressVO = new AddressVO();
                BeanUtils.copyProperties(addressPO, addressVO);
                addressVOList.add(addressVO);
            }
        }
        return addressVOList;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveAddress(AddressVO addressVO) {
        AddressPO addressPO = new AddressPO();
        BeanUtils.copyProperties(addressVO, addressPO);
        addressMapper.insertSelective(addressPO);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveOrder(OrderVO orderVO) {
        OrderPO orderPO = new OrderPO();
        BeanUtils.copyProperties(orderVO, orderPO);
        orderMapper.insert(orderPO);
        Integer id = orderPO.getId();
        OrderProjectVO orderProjectVO = orderVO.getOrderProjectVO();
        OrderProjectPO orderProjectPO = new OrderProjectPO();
        BeanUtils.copyProperties(orderProjectVO, orderProjectPO);
        orderProjectPO.setOrderId(id);
        orderProjectMapper.insert(orderProjectPO);
    }

}
