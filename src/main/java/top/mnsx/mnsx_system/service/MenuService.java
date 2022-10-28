package top.mnsx.mnsx_system.service;

import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.entity.Menu;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
public interface MenuService {

    /**
     * 分页条件查询
     * @param menuName 条件
     * @param pageNum 页数
     * @param pageSize 每页条数
     */
    Page<Menu> queryByPage(String menuName, Integer pageNum, Integer pageSize);

    /**
     * 添加菜单
     * @param menu 菜单数据
     * @return 返回id
     */
    Long save(Menu menu);

    /**
     * 更新菜单
     * @param menu 菜单
     */
    void modify(Menu menu);

    /**
     * 逻辑删除
     * @param id id
     */
    void remove(Long id);
}
