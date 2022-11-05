package top.mnsx.mnsx_system.exception;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/11/4 13:33
 * @Description: 菜单级联删除异常
 */
public class MenuCascadeDeleteException extends RuntimeException {
    public MenuCascadeDeleteException() {
        super();
    }

    public MenuCascadeDeleteException(String message) {
        super(message);
    }

    public MenuCascadeDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuCascadeDeleteException(Throwable cause) {
        super(cause);
    }

    protected MenuCascadeDeleteException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
