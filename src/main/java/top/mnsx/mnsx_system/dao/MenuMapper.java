package top.mnsx.mnsx_system.dao;

import org.apache.ibatis.annotations.Param;
import top.mnsx.mnsx_system.entity.Menu;
import top.mnsx.mnsx_system.entity.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
public interface MenuMapper {

    /**
     * 通过用户id获取用户权限
     * @param id 编号
     * @return 返回权限字符集合
     */
    List<String> selectPermsByUserId(Long id);

    /**
     * 分页条件查询
     * @param menuName 菜单名称
     * @param pageNum 页数
     * @param pageSize 每页条数
     * @return 返回数据
     */
    List<Menu> selectByPage(@Param("menuName") String menuName,
                            @Param("pageNum") Long pageNum,
                            @Param("pageSize") Long pageSize);

    /**
     * 插入数据
     * @param menu 菜单数据
     * @return void
     */
    Integer insert(Menu menu);

    /**
     * 更改数据
     * @param menu 菜单数据
     * @return void
     */
    Integer update(Menu menu);

    /**
     * 逻辑删除
     * @param id 编号
     * @return void
     */
    Integer delete(Long id);

    /**
     * 通过id获取菜单信息
     * @param id id
     * @return 菜单信息
     */
    Menu selectById(Long id);

    /**
     * 通过菜单id获取角色id集合
     * @param id 菜单id
     * @return 返回角色id
     */
    List<Long> selectRoleIdByMenuId(Long id);

    /**
     * 通过菜单名称获取
     * @param menuName
     * @return
     */
    Menu selectByMenuName(String menuName);

    /**
     * 查询数量
     * @param menuName 条件
     * @return 数量
     */
    Long selectCount(String menuName);
}
