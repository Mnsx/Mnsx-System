package top.mnsx.mnsx_system.service;

import top.mnsx.mnsx_system.dto.ExportRoleDTO;
import top.mnsx.mnsx_system.dto.ExportUserDTO;
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
    Page<Role> queryInPage(String roleName, Integer pageNum, Long pageSize);

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
     * @param ids 编号
     */
    void remove(Long[] ids);

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
     * @param selectMenus 选中
     * @param cancelMenus 取消
     */
    void diffMenu(Long roleId, List<Long> selectMenus, List<Long> cancelMenus);

    /**
     * 通过roleId后去MenuId
     * @param roleId 角色编号
     * @return 返回菜单编号集合
     */
    List<Long> queryMenuIdByRoleId(Long roleId);

    /**
     * 将数据转换成EXCEL
     * @param pageNum 开始页数
     * @param total 所需页数
     * @return 返回集合
     */
    List<ExportRoleDTO> getExportInfo(Integer pageNum, Long total);

    /**
     * 添加不存在的role
     * @param role
     */
    void saveUnchecked(Role role);
}
