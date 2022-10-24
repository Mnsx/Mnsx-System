package top.mnsx.mnsx_system.component.mybatis.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/10/23 15:49
 * @Description:
 */
@ConfigurationProperties(
        prefix = "mnsx.mybatis.interceptor", ignoreInvalidFields = true
)
public class InterceptorProperties {
    private Boolean enable = Boolean.TRUE;

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
