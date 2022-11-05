package top.mnsx.mnsx_system.exception;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/11/4 13:48
 * @Description: 当前用户不能被删除异常
 */
public class CurUserCanNotUpdateException extends RuntimeException {
    public CurUserCanNotUpdateException() {
        super();
    }

    public CurUserCanNotUpdateException(String message) {
        super(message);
    }

    public CurUserCanNotUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurUserCanNotUpdateException(Throwable cause) {
        super(cause);
    }

    protected CurUserCanNotUpdateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
