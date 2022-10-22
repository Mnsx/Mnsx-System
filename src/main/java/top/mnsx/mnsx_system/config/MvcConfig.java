package top.mnsx.mnsx_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.mnsx.mnsx_system.filter.UserHolderInterceptor;

import javax.annotation.Resource;

/**
 * @BelongsProject: mnsx_book
 * @User: Mnsx_x
 * @CreateTime: 2022/10/16 16:49
 * @Description:
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    @Resource
    private UserHolderInterceptor userHolderInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userHolderInterceptor);
    }
}
