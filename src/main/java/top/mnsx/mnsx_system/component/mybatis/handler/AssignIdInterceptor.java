package top.mnsx.mnsx_system.component.mybatis.handler;

import cn.hutool.core.util.StrUtil;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import top.mnsx.mnsx_system.component.mybatis.annotation.AssignId;
import top.mnsx.mnsx_system.component.mybatis.annotation.TableFill;
import top.mnsx.mnsx_system.component.mybatis.exception.MethodNotFoundException;
import top.mnsx.mnsx_system.component.mybatis.utils.IdWorkerUtil;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/10/23 17:45
 * @Description:
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class AssignIdInterceptor implements Interceptor {

    @Resource
    private IdWorkerUtil idWorkerUtil;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof Executor && invocation.getArgs().length == 2) {
            // mybatis执行器
            final Executor executor = (Executor) invocation.getTarget();
            // statement属性类
            final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            // 方法参数
            final Object parameter = invocation.getArgs()[1];
            // sql种类
            final SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
            if (SqlCommandType.INSERT == sqlCommandType) {
                return executeInsert(executor, mappedStatement, parameter);
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }

    private Object executeInsert(final Executor executor, final MappedStatement mappedStatement, final Object parameter) throws SQLException, IllegalAccessException {
        // 获取参数的所有属性
        Class<?> paramClass = parameter.getClass();
        Field[] declaredFields = paramClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            // 获取权限
            declaredField.setAccessible(true);
            if (declaredField.isAnnotationPresent(AssignId.class)) {
                AssignId annotation = declaredField.getAnnotation(AssignId.class);
                if (annotation != null) {
                    declaredField.set(parameter, idWorkerUtil.nextId());
                }
            }
        }
        return executor.update(mappedStatement, parameter);
    }
}
