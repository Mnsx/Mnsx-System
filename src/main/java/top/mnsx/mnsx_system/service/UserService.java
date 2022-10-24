package top.mnsx.mnsx_system.service;

import top.mnsx.mnsx_system.dto.LoginFormDTO;
import top.mnsx.mnsx_system.entity.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
public interface UserService {

    String sendCodeDemo(String phone);

    String login(LoginFormDTO loginForm);

    String logout();

    String getInfo();

    User queryByPhone(String phone);

    void save(User user);

    User queryById(Long id);
}
