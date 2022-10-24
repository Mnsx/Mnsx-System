package top.mnsx.mnsx_system.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.mnsx.mnsx_system.dto.LoginFormDTO;
import top.mnsx.mnsx_system.entity.User;
import top.mnsx.mnsx_system.service.UserService;
import top.mnsx.mnsx_system.service.impl.UserServiceImpl;

import javax.annotation.Resource;

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
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/code/{phone}")
    public String sendCodeDemo(@PathVariable String phone) {
        return userService.sendCodeDemo(phone);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginFormDTO loginForm) {
        return userService.login(loginForm);
    }

    @GetMapping("/logout")
    public String logout() {
        return userService.logout();
    }

    @GetMapping
    public String getInfo() {
        return userService.getInfo();
    }
}

