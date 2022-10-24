package top.mnsx.mnsx_system.component.mybatis.annotation;

import java.lang.annotation.*;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/10/23 15:46
 * @Description: 关键字段自动注入开启注解
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TableFill {

    boolean insertFlag() default false;

    String insertMethod() default "";

    boolean updateFlag() default false;

    String updateMethod() default "";
}
