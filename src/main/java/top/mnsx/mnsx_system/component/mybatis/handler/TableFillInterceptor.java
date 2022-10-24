package top.mnsx.mnsx_system.component.mybatis.handler;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Autowired;
import top.mnsx.mnsx_system.component.mybatis.annotation.TableFill;
import top.mnsx.mnsx_system.component.mybatis.exception.MethodNotFoundException;
import top.mnsx.mnsx_system.component.mybatis.utils.SpringBeanUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.*;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/10/22 16:05
 * @Description: 公共字段注入拦截器
 */
@Slf4j
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class TableFillInterceptor implements Interceptor {
    @Autowired
    private SpringBeanUtil springBeanUtil;

    /**
     * 拦截器主要方法
     * @param invocation mybatis执行对象
     * @return 返回执行结果
     * @throws Throwable 异常
     */
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
            } else if (SqlCommandType.UPDATE == sqlCommandType) {
                return executeUpdate(executor, mappedStatement, parameter);
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

    /**
     * 插入方法处理
     * @param executor 处理器
     * @param mappedStatement statement属性对象
     * @param parameter 参数
     * @return 返回执行结果
     * @throws SQLException 报错
     */
    private Object executeInsert(final Executor executor, final MappedStatement mappedStatement, final Object parameter) throws SQLException, IllegalAccessException {
        // 从Spring容器中实现类名称
        String implClassName = getMetaObjectHandlerImplClassName();
        // 获取参数的所有属性
        Class<?> paramClass = parameter.getClass();
        Field[] declaredFields = paramClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            // 获取权限
            declaredField.setAccessible(true);
            if (declaredField.isAnnotationPresent(TableFill.class)) {
                TableFill annotation = declaredField.getAnnotation(TableFill.class);
                if (annotation != null && annotation.insertFlag()) {
                    if (annotation.insertMethod() == null) {
                        throw new MethodNotFoundException("请指定插入方法");
                    }
                    // 获取方法名称
                    String methodName = annotation.insertMethod();
                    // 将指定方法的结果注入到属性中
                    Object result = doMethod(implClassName, methodName);
                    declaredField.set(parameter, result);
                }
            }
        }
        return executor.update(mappedStatement, parameter);
    }

    private Object executeUpdate(Executor executor, MappedStatement mappedStatement, Object parameter) throws SQLException, IllegalAccessException {
        // 从Spring容器中实现类名称
        String implClassName = getMetaObjectHandlerImplClassName();
        // 获取参数的所有属性
        Class<?> paramClass = parameter.getClass();
        Field[] declaredFields = paramClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            // 获取权限
            declaredField.setAccessible(true);
            if (declaredField.isAnnotationPresent(TableFill.class)) {
                TableFill annotation = declaredField.getAnnotation(TableFill.class);
                if (annotation != null && annotation.updateFlag()) {
                    if (annotation.updateMethod() == null) {
                        throw new MethodNotFoundException("请指定插入方法");
                    }
                    // 获取方法名称
                    String methodName = annotation.updateMethod();
                    // 将指定方法的结果注入到属性中
                    Object result = doMethod(implClassName, methodName);
                    declaredField.set(parameter, result);
                }
            }
        }
        return executor.update(mappedStatement, parameter);
    }

    private Object doMethod(String implClassName, String methodName) {
        Object obj = null;
        try {
            obj = Class.forName(implClassName).newInstance();
            Class<?> aClass = obj.getClass();
            Method declaredMethod = aClass.getDeclaredMethod(methodName, null);
            return declaredMethod.invoke(obj, null);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getMetaObjectHandlerImplClassName(){
        String implClassName = "";
        // 通过Spring容器获取
        Map<String, MetaObjectHandler> result = springBeanUtil.getApplicationContext().getBeansOfType(MetaObjectHandler.class);
        // 未找到实现类，抛出异常
        if(result.isEmpty()){
            throw new RuntimeException("没有匹配");
        }
        // 实现类大于1，抛出异常
        if(result.size() > 1){
            throw new RuntimeException("匹配过多");
        }
        // 返回第一个实现类的类名
        for (Map.Entry<String, MetaObjectHandler> entry : result.entrySet()) {
            String name = entry.getValue().getClass().getName();
            if(StringUtils.isNotEmpty(name)){
                implClassName = name;
                break;
            }
        }
        return implClassName;
    }
}
