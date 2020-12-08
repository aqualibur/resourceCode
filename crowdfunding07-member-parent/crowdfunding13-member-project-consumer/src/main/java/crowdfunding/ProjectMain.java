package crowdfunding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author dell
 */
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class ProjectMain {

    public static void main(String[] args) {
        SpringApplication.run(ProjectMain.class, args);
    }

}
