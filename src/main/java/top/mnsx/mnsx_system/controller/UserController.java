package top.mnsx.mnsx_system.controller;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import top.mnsx.mnsx_system.dto.LoginFormDTO;
import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.dto.UserDTO;
import top.mnsx.mnsx_system.entity.User;
import top.mnsx.mnsx_system.service.UserService;
import top.mnsx.mnsx_system.utils.ResultMap;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 发送验证码
     * @param phone 手机号码
     * @return 返回验证码
     */
    @GetMapping("/code/{phone}")
    public String sendCodeDemo(@PathVariable String phone) {
        String code = userService.sendCodeDemo(phone);
        return JSON.toJSONString(ResultMap.ok(code));
    }

    /**
     * 登录
     * @param loginForm 账号、密码、验证码
     * @return 返回token
     */
    @PostMapping("/login")
    public String login(@RequestBody LoginFormDTO loginForm) {
        String token = userService.login(loginForm);
        return JSON.toJSONString(ResultMap.ok(token));
    }

    /**
     * 登出
     * @return void
     */
    @GetMapping("/logout")
    public String logout() {
        userService.logout();
        return JSON.toJSONString(ResultMap.ok());
    }

    /**
     * 获取用户信息
     * @return 返回用户信息
     */
    @GetMapping
    public String getInfo() {
        UserDTO user = userService.getInfo();
        return JSON.toJSONString(ResultMap.ok(user));
    }

    /**
     * 后台管理系统添加功能
     * @param user 用户信息
     * @return 返回用户Id
     */
    @PostMapping("/sys")
    public String save(@RequestBody User user) {
        Long userId = userService.save(user);
        return JSON.toJSONString(ResultMap.ok(userId));
    }

    /**
     * 分页展示数据，通过电话号、邮箱、昵称进行搜索
     * @param user phone, email, nickName
     * @param pageNum 当前页数
     * @param pageSize 展示条数
     * @return 返回数据
     */
    @GetMapping("/sys/page/{pageNum}/{pageSize}")
    public String getPage(@RequestBody User user,
                          @PathVariable("pageNum") Integer pageNum,
                          @PathVariable("pageSize") Integer pageSize) {
        Page<User> page = userService.page(user, pageNum, pageSize);
        return JSON.toJSONString(ResultMap.ok(page));
    }

    /**
     * 更改用户数据
     * @param user 用户数据
     * @return void
     */
    @PutMapping("/sys")
    public String modify(@RequestBody User user) {
        userService.modifyOne(user);
        return JSON.toJSONString(ResultMap.ok());
    }

    @DeleteMapping("/sys/{id}")
    public String remove(@PathVariable Long id) {
        userService.removeOne(id);
        return JSON.toJSONString(ResultMap.ok());
    }
}

