package top.mnsx.mnsx_system.handler;

import org.springframework.stereotype.Component;
import top.mnsx.mnsx_system.component.mybatis.handler.MetaObjectHandler;

import java.time.LocalDateTime;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/10/23 16:53
 * @Description:
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    public Long getUser() {
        return 1L;
    }

    public LocalDateTime getNow() {
        return LocalDateTime.now();
    }
}
