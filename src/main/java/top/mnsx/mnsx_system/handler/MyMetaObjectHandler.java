package top.mnsx.mnsx_system.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;
import top.mnsx.mnsx_system.utils.ThreadLocalUtil;

import java.time.LocalDateTime;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/10/22 16:05
 * @Description:
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("createUser", ThreadLocalUtil.get() != null ? ThreadLocalUtil.get().getId() : 0);
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateUser", ThreadLocalUtil.get() != null ? ThreadLocalUtil.get().getId() : 0);
        metaObject.setValue("updateTime", LocalDateTime.now());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateUser", ThreadLocalUtil.get().getId());
        metaObject.setValue("updateTime", LocalDateTime.now());
    }
}
