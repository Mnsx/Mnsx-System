package top.mnsx.mnsx_system.component.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/10/23 18:03
 * @Description: uuid
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface AssignId {
}
