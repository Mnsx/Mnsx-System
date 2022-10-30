package top.mnsx.mnsx_system.controller;


import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.entity.Role;
import top.mnsx.mnsx_system.service.RoleService;
import top.mnsx.mnsx_system.utils.ResultMap;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 分页条件查询
     * @param role 条件
     * @param pageNum 当前页数
     * @param pageSize 每页条数
     * @return page
     */
    @GetMapping("/sys/page/{pageNum}/{pageSize}")
    public String queryInPage(@RequestBody Role role,
                              @PathVariable Integer pageNum,
                              @PathVariable Integer pageSize) {
        String roleName = role.getRoleName();
        log.info("{}", roleName);
        Page<Role> page = roleService.queryInPage(roleName, pageNum, pageSize);
        return JSON.toJSONString(ResultMap.ok(page));
    }

    /**
     * 保存角色信息
     * @param role 角色信息
     * @return 返回id
     */
    @PostMapping("/sys")
    public String save(@RequestBody Role role) {
        Long roleId = roleService.save(role);
        return JSON.toJSONString(ResultMap.ok(roleId));
    }

    /**
     * 更改角色信息
     * @param role 角色信息
     * @return void
     */
    @PutMapping("/sys")
    public String modify(@RequestBody Role role) {
        roleService.modify(role);
        return JSON.toJSONString(ResultMap.ok());
    }

    /**
     * 删除角色信息
     * @param id 角色编号
     * @return void
     */
    @DeleteMapping("/sys/{id}")
    public String remove(@PathVariable Long id) {
        roleService.remove(id);
        return JSON.toJSONString(ResultMap.ok());
    }

    /**
     * 更改角色对应菜单
     * @param roleId 角色编号
     * @param menuStr 菜单编号
     * @return void
     */
    @PutMapping("/sys/{roleId}")
    public String diffMenu(@PathVariable("roleId") Long roleId,
                           @RequestBody String[] menuStr) {
        List<Long> menuIds = Arrays.stream(menuStr).map(Long::parseLong).collect(Collectors.toList());
        System.out.println(menuIds);

        roleService.diffMenu(roleId, menuIds);
        return JSON.toJSONString(ResultMap.ok());
    }
}

