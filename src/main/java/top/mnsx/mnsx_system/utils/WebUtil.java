package top.mnsx.mnsx_system.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @BelongsProject: spring_security_study
 * @User: Mnsx_x
 * @CreateTime: 2022/10/12 21:42
 * @Description:
 */
public class WebUtil {
    public static void renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
