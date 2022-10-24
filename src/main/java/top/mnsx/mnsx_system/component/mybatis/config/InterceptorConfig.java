package top.mnsx.mnsx_system.component.mybatis.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import top.mnsx.mnsx_system.component.mybatis.handler.AssignIdInterceptor;
import top.mnsx.mnsx_system.component.mybatis.handler.TableFillInterceptor;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/10/23 15:51
 * @Description:
 */
@Configuration
@EnableConfigurationProperties({InterceptorProperties.class})
@ConditionalOnProperty(
        name = {"mnsx.mybatis.interceptor.enabled"},
        matchIfMissing = true,
        havingValue = "true"
)
public class InterceptorConfig {
    @Bean
    @DependsOn("springBeanUtil")
    public TableFillInterceptor tableFillInterceptor() {
        return new TableFillInterceptor();
    }

    @Bean
    @DependsOn("idWorkerUtil")
    public AssignIdInterceptor assignIdInterceptor() {
        return new AssignIdInterceptor();
    }
}
