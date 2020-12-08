package crowdfunding.api;

import crowdfunding.entity.po.MemberPO;
import crowdfunding.entity.vo.*;
import crowdfunding.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author dell
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
@FeignClient("member-mysql-provider")
public interface MysqlRemoteService {

    @RequestMapping("/get/member/by/login/acct/remote")
    ResultEntity<MemberPO> getMemberByLoginAcctRemote(@RequestParam("loginacct") String loginacct);

    @RequestMapping("/save/member/remote")
    ResultEntity<String> saveMemberRemote(@RequestBody MemberPO memberPO);

    @RequestMapping("/save/projectVO/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO,@RequestParam("memberLoginVOId") Integer memberLoginVOId);

    @RequestMapping("/get/portal/type/VO/remote")
    ResultEntity<List<PortalTypeVO>> getPortalTypeVORemote();

    @RequestMapping("/get/project/detail/remote/{projectId}")
    ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId);

    @RequestMapping("/get/order/project/VO/remote/{projectId}/{returnId}")
    ResultEntity<OrderProjectVO> getOrderProjectVORemote(@PathVariable("projectId") Integer projectId, @PathVariable("returnId") Integer returnId);

    @RequestMapping("/get/address/vo/remote")
    ResultEntity<List<AddressVO>> getAddressVORemote(@RequestParam("memberLoginVOId") Integer memberLoginVOId);

    @RequestMapping("/save/address/remote")
    ResultEntity<String> saveAddressRemote(@RequestBody AddressVO addressVO);

    @RequestMapping("/save/order/remote")
    ResultEntity<String> saveOrderRemote(@RequestBody OrderVO orderVO);

}
