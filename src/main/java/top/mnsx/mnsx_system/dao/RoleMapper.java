package top.mnsx.mnsx_system.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;
import top.mnsx.mnsx_system.entity.Menu;
import top.mnsx.mnsx_system.entity.Role;
import top.mnsx.mnsx_system.entity.User;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
public interface RoleMapper {
    /**
     * 插入角色
     * @param role 角色信息
     * @return void
     */
    Integer insertOne(Role role);

    /**
     * 逻辑删除角色
     * @param id 编号
     * @return void
     */
    Integer deleteOne(Long id);

    /**
     * 更新角色信息
     * @param role 角色信息
     * @return void
     */
    Integer updateOne(Role role);

    /**
     * 分页条件查询
     * @param roleName 条件
     * @param pageNum 页数
     * @param pageSize 每页条数
     * @return 数据
     */
    List<Role> selectByPage(@Param("roleName") String roleName,
                            @Param("pageNum") Long pageNum,
                            @Param("pageSize") Long pageSize);

    /**
     * 根据id查询
     * @param id id
     * @return 返回数据
     */
    Role selectById(Long id);

    /**
     * 通过用户id查询角色信息
     * @param userId 用户id
     * @return 角色信息
     */
    Role selectByUserId(Long userId);

    /**
     * 根据角色编号查询菜单信息
     * @param roleId 角色编号
     * @return 返回菜单信息集合
     */
    List<Menu> selectRoleMenuByRoleId(Long roleId);

    /**
     * 批量插入角色菜单
     * @param roleId 角色编号
     * @param newMenu 新的角色菜单
     */
    void insertRoleMenu(@Param("roleId") Long roleId, @Param("newMenu") List<Long> newMenu);

    /**
     * 批量删除角色菜单
     * @param roleId 角色编号
     * @param deleteMenu 需要删除的角色菜单
     */
    void deleteRoleMenu(@Param("roleId") Long roleId, @Param("deleteMenu") List<Long> deleteMenu);

    /**
     * 通过角色编号获取用户编号
     * @param id 角色编号
     * @return 返回用户id集合
     */
    List<Long> selectUserIdByRoleId(Long id);

    /**
     * 通过roleName获取
     * @param roleName
     * @return
     */
    Role selectByRoleName(String roleName);

    /**
     * 获取查询总数
     * @param roleName 条件
     * @return 数量
     */
    Long selectCount(String roleName);
}
