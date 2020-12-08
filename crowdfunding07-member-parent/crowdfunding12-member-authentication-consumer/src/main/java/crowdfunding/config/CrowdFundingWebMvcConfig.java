package crowdfunding.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author dell
 */
@Configuration
public class CrowdFundingWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(@NotNull ViewControllerRegistry registry) {
        String regUrlPath = "/member/auth/to/reg/page";
        String regViewName = "member-reg";
        registry.addViewController(regUrlPath).setViewName(regViewName);
        String loginUrlPath = "/member/auth/to/login/page";
        String loginViewName = "member-login";
        registry.addViewController(loginUrlPath).setViewName(loginViewName);
        String centerUrlPath = "/member/auth/to/center/page";
        String centerViewName = "member-center";
        registry.addViewController(centerUrlPath).setViewName(centerViewName);
        String myCrowdFundingUrlPath = "/member/my/crowdfunding";
        String myCrowdFundingViewName = "member-crowdfunding";
        registry.addViewController(myCrowdFundingUrlPath).setViewName(myCrowdFundingViewName);
    }

}
