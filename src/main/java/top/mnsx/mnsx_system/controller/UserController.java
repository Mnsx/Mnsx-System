package top.mnsx.mnsx_system.controller;


import com.alibaba.fastjson.JSON;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import top.mnsx.mnsx_system.constants.SystemConstants;
import top.mnsx.mnsx_system.dto.LoginFormDTO;
import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.dto.UserDTO;
import top.mnsx.mnsx_system.dto.UserSysDTO;
import top.mnsx.mnsx_system.entity.User;
import top.mnsx.mnsx_system.service.UserService;
import top.mnsx.mnsx_system.service.impl.ExcelServiceImpl;
import top.mnsx.mnsx_system.utils.ResultMap;
import top.mnsx.mnsx_system.dto.ExportUserDTO;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

import static top.mnsx.mnsx_system.utils.SmsSender.sendSms;

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
    @Resource
    private ExcelServiceImpl excelService;

    /**
     * 发送验证码
     * @param phone 手机号码
     * @return 返回验证码
     */
    @GetMapping("/code/{phone}")
    public String sendCodeDemo(@PathVariable String phone) {
        String code = userService.sendCodeDemo(phone);
//        return JSON.toJSONString(ResultMap.ok(code));

        sendSms(phone, code);

        return JSON.toJSONString(ResultMap.ok());
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
    @PreAuthorize("hasAuthority('sys:user:')")
    @PostMapping("/sys/{roleId}")
    public String save(@RequestBody User user,
                       @PathVariable("roleId") Long roleId) {
        Long userId = userService.save(user, roleId);
        return JSON.toJSONString(ResultMap.ok(userId));
    }

    /**
     * 分页展示数据，通过电话号、邮箱、昵称进行搜索
     * @param user phone, email, nickName
     * @param pageNum 当前页数
     * @param pageSize 展示条数
     * @return 返回数据
     */
    @PreAuthorize("hasAuthority('sys:user:')")
    @PostMapping("/sys/page/{pageNum}/{pageSize}")
    public String getPage(@RequestBody User user,
                          @PathVariable("pageNum") Integer pageNum,
                          @PathVariable("pageSize") Long pageSize) {
        Page<UserSysDTO> page = userService.page(user, pageNum, pageSize);
        return JSON.toJSONString(ResultMap.ok(page));
    }

    /**
     * 更改用户数据
     * @param user 用户数据
     * @return void
     */
    @PreAuthorize("hasAuthority('sys:user:')")
    @PutMapping("/sys")
    public String modify(@RequestBody User user) {
        userService.modifyOne(user);
        return JSON.toJSONString(ResultMap.ok());
    }

    /**
     * 删除用户数据
     * @param ids 编号
     * @return void
     */
    @PreAuthorize("hasAuthority('sys:user:')")
    @DeleteMapping("/sys")
    public String remove(@RequestBody Long[] ids) {
        userService.removeOne(ids);
        return JSON.toJSONString(ResultMap.ok());
    }

    /**
     * 更改用户角色
     * @param roleId 角色编号
     * @return void
     */
    @PreAuthorize("hasAuthority('sys:user:')")
    @PutMapping("/sys/role/{userId}/{roleId}")
    public String changeUserRoleId(@PathVariable("roleId") Long roleId,
                                   @PathVariable("userId") Long userId) {
        userService.changeUserRoleId(userId, roleId);

        return JSON.toJSONString(ResultMap.ok());
    }

    /**
     * 将用户数据导出作为excel
     * @param pageNum 开始页数
     * @param total 一共需要的条数
     * @param response ioc提供
     * @return void response返回excel
     */
    @PreAuthorize("hasAuthority('sys:user:')")
    @GetMapping("/sys/ex/{pageNum}/{pageSize}")
    public String exportToExcel(@PathVariable("pageNum") Integer pageNum,
                                @PathVariable("pageSize") Long total,
                                HttpServletResponse response) {
        List<ExportUserDTO> exportInfo = userService.getExportInfo(pageNum, total);
        excelService.writeExcel(response, "user", () -> exportInfo);
        return JSON.toJSONString(ResultMap.ok());
    }

    /**
     * 导入数据通过Excel
     * @param file excel文件
     * @return void
     * @throws IOException
     */
    @PreAuthorize("hasAuthority('sys:user:')")
    @PostMapping("/sys/im")
    public String importFromExcel(MultipartFile file) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(file.getInputStream());
        excelService.readExcel(bufferedInputStream, "user", ExportUserDTO.class, (dataList) -> {
            dataList.forEach((item) -> {
                User user = new User();
                BeanUtils.copyProperties(item, user);
                userService.saveUnchecked(user, SystemConstants.DEFAULT_ROLE_ID);
            });
        });
        return JSON.toJSONString(ResultMap.ok());
    }
}

