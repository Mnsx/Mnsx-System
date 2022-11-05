package top.mnsx.mnsx_system.exception;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/11/4 13:44
 * @Description: 角色级联删除异常
 */
public class RoleCascadeDeleteException extends RuntimeException{
    public RoleCascadeDeleteException() {
        super();
    }

    public RoleCascadeDeleteException(String message) {
        super(message);
    }

    public RoleCascadeDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RoleCascadeDeleteException(Throwable cause) {
        super(cause);
    }

    protected RoleCascadeDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
