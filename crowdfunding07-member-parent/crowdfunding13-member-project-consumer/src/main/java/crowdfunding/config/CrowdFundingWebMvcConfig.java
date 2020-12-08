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
        String agreeUrlPath = "/to/agree/page";
        String agreeViewName = "project-agree";
        registry.addViewController(agreeUrlPath).setViewName(agreeViewName);
        String launchUrlPath = "/to/launch/page";
        String launchViewName = "project-launch";
        registry.addViewController(launchUrlPath).setViewName(launchViewName);
        String returnUrlPath = "/to/return/page";
        String returnViewName = "project-return";
        registry.addViewController(returnUrlPath).setViewName(returnViewName);
        String confirmUrlPath = "/create/confirm/page";
        String confirmViewName = "project-confirm";
        registry.addViewController(confirmUrlPath).setViewName(confirmViewName);
        String successUrlPath = "/create/success";
        String successViewName = "project-success";
        registry.addViewController(successUrlPath).setViewName(successViewName);
    }

}
