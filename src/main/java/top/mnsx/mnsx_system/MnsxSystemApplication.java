package top.mnsx.mnsx_system;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication
@MapperScan(basePackages = "top.mnsx.mnsx_system.dao")
// 开启SpringSecurity授权功能
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MnsxSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MnsxSystemApplication.class, args);
    }

}
