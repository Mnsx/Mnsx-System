package top.mnsx.mnsx_system.utils;

import top.mnsx.mnsx_system.dto.UserDTO;

/**
 * @BelongsProject: take_out
 * @User: Mnsx_x
 * @CreateTime: 2022/10/7 21:30
 * @Description: ThreadLocal工具类
 */
public class ThreadLocalUtil {
    private static final ThreadLocal<UserDTO> threadLocal = new ThreadLocal<>();

    public static void add(UserDTO temp) {
        threadLocal.set(temp);
    }

    public static UserDTO get() {
        return threadLocal.get();
    }

    public static void remove() {
        threadLocal.remove();
    }
}
