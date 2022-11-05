package top.mnsx.mnsx_system.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import top.mnsx.mnsx_system.constants.SystemConstants;
import top.mnsx.mnsx_system.dto.ExportRoleDTO;
import top.mnsx.mnsx_system.dto.ExportUserDTO;
import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.entity.Role;
import top.mnsx.mnsx_system.entity.User;
import top.mnsx.mnsx_system.service.MenuService;
import top.mnsx.mnsx_system.service.RoleService;
import top.mnsx.mnsx_system.service.impl.ExcelServiceImpl;
import top.mnsx.mnsx_system.utils.ResultMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
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
@PreAuthorize("hasAuthority('sys:role:')")
public class RoleController {

    @Resource
    private RoleService roleService;
    @Resource
    private ExcelServiceImpl excelService;

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
                              @PathVariable Long pageSize) {
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
     * @param ids 角色编号
     * @return void
     */
    @DeleteMapping("/sys/{id}")
    public String remove(@PathVariable Long[] ids) {
        roleService.remove(ids);
        return JSON.toJSONString(ResultMap.ok());
    }

    /**
     * 更改角色对应菜单
     * @param roleId 角色编号
     * @param json 参数
     * @return void
     */
    @PutMapping("/sys/{roleId}")
    @SuppressWarnings("unchecked")
    public String diffMenu(@PathVariable("roleId") Long roleId,
                           @RequestBody String json) {
        HashMap<String, JSONArray> param = JSON.parseObject(json, HashMap.class);
        System.out.println(param);
        List<Long> selectMenus = param.get("selectMenus").stream().mapToLong(item ->
            Long.parseLong((String) item)
        ).boxed().collect(Collectors.toList());
        List<Long> cancelMenus = param.get("cancelMenus").stream().mapToLong(item ->
                Long.parseLong((String) item)
        ).boxed().collect(Collectors.toList());

        roleService.diffMenu(roleId, selectMenus, cancelMenus);
        return JSON.toJSONString(ResultMap.ok());
    }

    /**
     * 获取角色已经选择的菜单
     * @param roleId 角色编号
     * @return 返回菜单编号集合
     */
    @GetMapping("/sys/menu/{roleId}")
    public String showMenuForRole(@PathVariable("roleId") Long roleId) {
        List<Long> menuIds = roleService.queryMenuIdByRoleId(roleId);

        return JSON.toJSONString(ResultMap.ok(menuIds));
    }

    /**
     * 将角色数据导出作为excel
     * @param pageNum 开始页数
     * @param total 一共需要的条数
     * @param response ioc提供
     * @return void response返回excel
     */
    @GetMapping("/sys/ex/{pageNum}/{pageSize}")
    public String exportToExcel(@PathVariable("pageNum") Integer pageNum,
                                @PathVariable("pageSize") Long total,
                                HttpServletResponse response) {
//        List<ExportRoleDTO> exportInfo = roleService.getExportInfo(pageNum, total);
        Page<Role> page = roleService.queryInPage("", pageNum, total);
        excelService.writeExcel(response, "role", page::getData);
        return JSON.toJSONString(ResultMap.ok());
    }

    /**
     * 导入数据通过Excel
     * @param file excel文件
     * @return void
     * @throws IOException
     */
    @PostMapping("/sys/im")
    public String importFromExcel(MultipartFile file) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(file.getInputStream());
        excelService.readExcel(bufferedInputStream, "role", Role.class, (dataList) -> {
            dataList.forEach((item) -> {
                roleService.saveUnchecked((Role) item);
            });
        });
        return JSON.toJSONString(ResultMap.ok());
    }
}

