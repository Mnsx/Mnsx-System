package top.mnsx.mnsx_system.service;

import top.mnsx.mnsx_system.dto.LoginFormDTO;
import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.dto.UserDTO;
import top.mnsx.mnsx_system.entity.User;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
public interface UserService {

    /**
     * 发送验证码
     * @param phone 手机号码
     * @return 返回验证码
     */
    String sendCodeDemo(String phone);

    /**
     * 登录
     * @param loginForm 登录表单信息
     * @return 返回token
     */
    String login(LoginFormDTO loginForm);

    /**
     * 登出
     */
    void logout();

    /**
     * 获取用户关键信息
     * @return 返回用户信息DTO
     */
    UserDTO getInfo();

    /**
     * 通过电话获取用户信息
     * @param phone 电话
     * @return 返回用户所有信息
     */
    User queryByPhone(String phone);

    /**
     * 保存用户
     * @param user 用户
     */
    Long save(User user);

    /**
     * 通过id获取用户信息
     * @param id 用户id
     * @return 返回用户所有信息
     */
    User queryById(Long id);

    /**
     * 条件分页查询
     * @param user 查询条件
     * @param pageNum 当前页数
     * @param pageSize 每页条数
     * @return 返回Page
     */
    Page<User> page(User user, Integer pageNum, Integer pageSize);

    /**
     * 更改用户信息
     * @param user 用户信息
     */
    void modifyOne(User user);

    /**
     * 逻辑删除用户
     * @param id 用户编号
     */
    void removeOne(Long id);
}
