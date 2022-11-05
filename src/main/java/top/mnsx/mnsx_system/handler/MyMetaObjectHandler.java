package top.mnsx.mnsx_system.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.mnsx.mnsx_system.component.mybatis.handler.MetaObjectHandler;
import top.mnsx.mnsx_system.dto.UserDTO;
import top.mnsx.mnsx_system.exception.UserNotExistException;
import top.mnsx.mnsx_system.utils.ThreadLocalUtil;

import java.time.LocalDateTime;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/10/23 16:53
 * @Description:
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
    public Long getUser() {
        UserDTO userDTO = ThreadLocalUtil.get();
        return userDTO == null ? 0 : userDTO.getId();
    }

    public LocalDateTime getNow() {
        return LocalDateTime.now();
    }
}
