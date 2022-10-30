package top.mnsx.mnsx_system.service;

import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.entity.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
public interface RoleService {

    /**
     * 分页条件查询（roleName）
     * @param roleName 条件
     * @param pageNum 页数
     * @param pageSize 每页条数
     * @return page
     */
    Page<Role> queryInPage(String roleName, Integer pageNum, Integer pageSize);

    /**
     * 保存角色
     * @param role 角色信息
     * @return 返回编号
     */
    Long save(Role role);

    /**
     * 跟新
     * @param role 角色信息
     */
    void modify(Role role);

    /**
     * 逻辑删除
     * @param id 编号
     */
    void remove(Long id);

    /**
     * 根据角色编号搜索角色
     * @param roleId 角色编号
     * @return 返回角色信息
     */
    Role queryById(Long roleId);

    /**
     * 通过用户Id获取角色信息
     * @param id 用户编号
     * @return 返回角色信息
     */
    Role queryByUserId(Long id);

    /**
     * 更改角色对应的菜单信息
     * @param roleId 角色编号
     * @param menuId 菜单编号
     */
    void diffMenu(Long roleId, List<Long> menuId);
}
