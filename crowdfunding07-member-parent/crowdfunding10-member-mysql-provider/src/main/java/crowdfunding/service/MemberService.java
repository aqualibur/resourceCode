package crowdfunding.service;

import crowdfunding.entity.po.MemberPO;

/**
 * @author dell
 */
public interface MemberService {

    MemberPO getMemberByLoginAcct(String loginacct);

    void saveMember(MemberPO memberPO);

}
