package top.mnsx.mnsx_system.utils;

/**
 * @BelongsProject: mnsx-utils
 * @User: Mnsx_x
 * @CreateTime: 2022/9/22 19:58
 * @Description: 响应码类——用来保存响应请求的响应码和响应消息
 */
public enum ResultCode {
    // 成功响应
    SUCCESS(200, "请求成功"),
    // 错误响应
    // 登录
    REQUEST_AFTER_LOGIN(5000, "请登录后再使用系统"),
    PERMISSION_NOT_ENOUGH(5001, "用户权限不足"),
    PHONE_NOT_FORMAT(5002, "手机号码格式错误"),
    LOGIN_FAIL(5003, "登录失败，请输入验证码或密码"),
    CODE_NOT_RIGHT(5004, "验证码错误"),
    PASSWORD_NOT_RIGHT(5005, "账号不存在或者密码错误"),
    ILLEGAL_TOKEN(5006, "token非法"),
    // 用户
    USER_NOT_EXIST(5100, "用户不存在"),
    User_HAS_EXIST(5101, "用户已经存在"),
    // 角色
    ROLE_HAS_EXIST(5200, "角色已经存在"),
    ROLE_NOT_EXIST(5201, "角色不存在"),
    ROLE_CASCADE_DELETE_ERROR(5202, "角色级联删除失败"),
    CUR_USER_CAN_NOT_UPDATE(5203, "当前使用的用户不允许修改"),
    // 菜单
    MENU_CASCADE_DELETE_ERROR(5300, "菜单级联删除失败"),
    MENU_HAS_EXIST(5301, "菜单已经存在"),
    MENU_NOT_EXIST(5302, "菜单不存在"),
    // 文件
    FILE_HAS_EXIST(5400, "文件已经存在"),
    FILE_NOT_EXIST(5401, "文件不存在"),
    MAX_UPLOAD_SIZE(5402, "文件超过最大上传大小"),
    FILE_DELETE_FAIL(5403, "文件删除失败"),
    // 服务器内部问题
    INNER_ERROR(500, "服务器内部问题，请联系管理员");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
