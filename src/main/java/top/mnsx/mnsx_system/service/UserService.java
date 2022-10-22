package top.mnsx.mnsx_system.service;

import top.mnsx.mnsx_system.dto.LoginFormDTO;
import top.mnsx.mnsx_system.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
public interface UserService extends IService<User> {

    String sendCodeDemo(String phone);

    String login(LoginFormDTO loginForm);

    String logout();

    String getInfo();
}
