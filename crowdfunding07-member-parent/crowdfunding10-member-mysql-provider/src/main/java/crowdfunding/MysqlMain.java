package crowdfunding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author dell
 */
@EnableEurekaClient
@SpringBootApplication
public class MysqlMain {

    public static void main(String[] args) {
        SpringApplication.run(MysqlMain.class, args);
    }

}
