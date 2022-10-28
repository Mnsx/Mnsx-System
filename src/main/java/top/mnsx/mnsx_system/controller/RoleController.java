package top.mnsx.mnsx_system.controller;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.entity.Role;
import top.mnsx.mnsx_system.service.RoleService;
import top.mnsx.mnsx_system.utils.ResultMap;

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
@RequestMapping("/role")
@Slf4j
public class RoleController {

    @Resource
    private RoleService roleService;

    @GetMapping("/sys/page/{pageNum}/{pageSize}")
    public String queryInPage(@RequestBody Role role,
                              @PathVariable Integer pageNum,
                              @PathVariable Integer pageSize) {
        String roleName = role.getRoleName();
        log.info("{}", roleName);
        Page<Role> page = roleService.queryInPage(roleName, pageNum, pageSize);
        return JSON.toJSONString(ResultMap.ok(page));
    }

    @PostMapping("/sys")
    public String save(@RequestBody Role role) {
        Long roleId = roleService.save(role);
        return JSON.toJSONString(ResultMap.ok(roleId));
    }

    @PutMapping("/sys")
    public String modify(@RequestBody Role role) {
        roleService.modify(role);
        return JSON.toJSONString(ResultMap.ok());
    }

    @DeleteMapping("/sys/{id}")
    public String remove(@PathVariable Long id) {
        roleService.remove(id);
        return JSON.toJSONString(ResultMap.ok());
    }
}

