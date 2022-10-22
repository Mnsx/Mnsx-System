package top.mnsx.mnsx_system.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @BelongsProject: mnsx-utils
 * @User: Mnsx_x
 * @CreateTime: 2022/10/1 16:17
 * @Description: 响应类——利用建造者模式创建，链式编程构建响应类，通过读取ResultCode的内容返回异常
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultMap {
    // 响应码
    private Integer code;
    // 响应数据
    private Object data;
    // 响应消息
    private String msg;

    /**
     * 请求成功响应方法（带参数）
     * @param data 响应参数
     * @return 返回响应类
     */
    public static <T> ResultMap ok(T data) {
        return builder().code(ResultCode.SUCCESS.getCode())
                .msg(ResultCode.SUCCESS.getMessage())
                .data(data);
    }

    /**
     * 请求成功响应方法
     * @return 返回响应类
     */
    public static ResultMap ok() {
        return builder().code(ResultCode.SUCCESS.getCode())
                .msg(ResultCode.SUCCESS.getMessage());
    }

    /**
     * 请求失败响应方法
     * @param resultCode 错误返回码类
     * @return 返回响应类
     */
    public static ResultMap fail(ResultCode resultCode) {
        return builder().code(resultCode.getCode())
                .msg(resultCode.getMessage());
    }

    /**
     * 请求失败响应方法（带参数）
     * @param resultCode 错误返回码类
     * @param data 响应参数
     * @return 返回响应类
     */
    public static <T> ResultMap fail(ResultCode resultCode, T data) {
        return builder().code(resultCode.getCode())
                .msg(resultCode.getMessage())
                .data(data);
    }

    private <T> ResultMap data(T data) {
        setData(data);
        return this;
    }

    private ResultMap msg(String msg) {
        setMsg(msg);
        return this;
    }

    private ResultMap code(Integer code) {
        setCode(code);
        return this;
    }

    private static ResultMap builder() {
        return new ResultMap();
    }
}