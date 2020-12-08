package crowdfunding.service.impl;

import crowdfunding.entity.po.MemberPO;
import crowdfunding.entity.po.MemberExample;
import crowdfunding.entity.po.MemberExample.Criteria;
import crowdfunding.mapper.MemberMapper;
import crowdfunding.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author dell
 */
@Service
public class MemberServiceImpl implements MemberService {

    @Resource
    private MemberMapper memberMapper;

    @Override
    @Transactional(readOnly = true)
    public MemberPO getMemberByLoginAcct(String loginacct) {
        MemberExample memberExample = new MemberExample();
        Criteria criteria = memberExample.createCriteria();
        criteria.andLoginacctEqualTo(loginacct);
        List<MemberPO> memberPOS = memberMapper.selectByExample(memberExample);
        if (memberPOS == null || memberPOS.size() == 0) {
            return null;
        } else {
            return memberPOS.get(0);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveMember(MemberPO memberPO) {
        memberMapper.insertSelective(memberPO);
    }

}
