package crowdfunding.handler;

import crowdfunding.api.MysqlRemoteService;
import crowdfunding.constant.CrowdFundingConstant;
import crowdfunding.entity.po.MemberPO;
import crowdfunding.entity.vo.MemberLoginVO;
import crowdfunding.entity.vo.MemberVO;
import crowdfunding.util.ResultEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import static crowdfunding.util.ResultEntity.FAILED;

/**
 * @author dell
 */
@Controller
public class MemberHandler {

    @Resource
    private MysqlRemoteService mysqlRemoteService;

    @RequestMapping("/member/auth/do/register")
    public String doRegister(MemberVO memberVO, ModelMap modelMap) {
        try {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String userpswd = memberVO.getUserpswd();
            String encode = bCryptPasswordEncoder.encode(userpswd);
            memberVO.setUserpswd(encode);
            MemberPO memberPO = new MemberPO();
            BeanUtils.copyProperties(memberVO, memberPO);
            ResultEntity<String> resultEntity = mysqlRemoteService.saveMemberRemote(memberPO);
            if (FAILED.equals(resultEntity.getResult())) {
                modelMap.addAttribute(CrowdFundingConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
                return "member-reg";
            }
            return "redirect:http://localhost:80/member/auth/to/login/page";
        } catch (Exception e) {
            e.printStackTrace();
            modelMap.addAttribute(CrowdFundingConstant.ATTR_NAME_MESSAGE, e.getMessage());
            return "member-reg";
        }
    }

    @RequestMapping("/member/auth/do/login")
    public String doLogin(@RequestParam("loginacct") String loginacct,
                          @RequestParam("userpswd") String userpswd,
                          ModelMap modelMap,
                          HttpSession httpSession) {
        ResultEntity<MemberPO> resultEntity = mysqlRemoteService.getMemberByLoginAcctRemote(loginacct);
        if (FAILED.equals(resultEntity.getResult())) {
            modelMap.addAttribute(CrowdFundingConstant.ATTR_NAME_MESSAGE, resultEntity.getMessage());
            return "member-login";
        }
        MemberPO memberPO = resultEntity.getQueryData();
        if (memberPO == null) {
            modelMap.addAttribute(CrowdFundingConstant.ATTR_NAME_MESSAGE, CrowdFundingConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        String userpswdDB = memberPO.getUserpswd();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        boolean matches = bCryptPasswordEncoder.matches(userpswd, userpswdDB);
        if (!matches) {
            modelMap.addAttribute(CrowdFundingConstant.ATTR_NAME_MESSAGE, CrowdFundingConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        MemberLoginVO memberLoginVO = new MemberLoginVO(memberPO.getId(), memberPO.getUsername(), memberPO.getEmail());
        httpSession.setAttribute(CrowdFundingConstant.ATTR_NAME_LOGIN_MEMBER, memberLoginVO);
        return "redirect:http://localhost:80/member/auth/to/center/page";
    }

    @RequestMapping("/member/auth/do/logout")
    public String doLogout(HttpSession httpSession) {
        if (httpSession != null) {
            httpSession.invalidate();
        }
        return "redirect:http://localhost:80/";
    }

}
