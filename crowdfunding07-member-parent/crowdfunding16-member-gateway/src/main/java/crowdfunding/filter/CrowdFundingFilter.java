package crowdfunding.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import crowdfunding.constant.AccessResources;
import crowdfunding.constant.CrowdFundingConstant;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author dell
 */
@Component
public class CrowdFundingFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        String servletPath = request.getServletPath();
        boolean containsResult = AccessResources.ACCESS_RES_SET.contains(servletPath);
        if (containsResult) {
            return false;
        } else {
            boolean judgeResult = AccessResources.judgePathWithStaticRes(servletPath);
            return !judgeResult;
        }
    }

    @Override
    public Object run() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();
        HttpSession session = request.getSession();
        Object loginMember = session.getAttribute(CrowdFundingConstant.ATTR_NAME_LOGIN_MEMBER);
        if (loginMember == null) {
            HttpServletResponse response = currentContext.getResponse();
            session.setAttribute(CrowdFundingConstant.ATTR_NAME_MESSAGE, CrowdFundingConstant.MESSAGE_ACCESS_FORBIDEN);
            try {
                response.sendRedirect("/member/auth/to/login/page");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
