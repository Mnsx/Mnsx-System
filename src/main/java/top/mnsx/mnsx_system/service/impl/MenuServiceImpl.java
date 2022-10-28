package top.mnsx.mnsx_system.service.impl;

import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.entity.Menu;
import top.mnsx.mnsx_system.dao.MenuMapper;
import top.mnsx.mnsx_system.exception.MenuHasExistException;
import top.mnsx.mnsx_system.exception.MenuNotExistException;
import top.mnsx.mnsx_system.service.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
@Service
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public Page<Menu> queryByPage(String menuName, Integer pageNum, Integer pageSize) {
        List<Menu> menus = menuMapper.selectByPage(menuName, pageNum - 1, pageSize);
        return new Page<Menu>().setData(menus).setCount((long) menus.size());
    }

    @Override
    public Long save(Menu menu) {
        Long menuId = menu.getId();
        Menu result = menuMapper.selectById(menuId);
        if (result != null) {
            throw new MenuHasExistException();
        }
        menuMapper.insert(menu);
        return menu.getId();
    }

    @Override
    public void modify(Menu menu) {
        Long id = menu.getId();
        Menu result = menuMapper.selectById(id);
        if (result == null) {
            throw new MenuNotExistException();
        }
        menuMapper.update(menu);
    }

    @Override
    public void remove(Long id) {
        Menu result = menuMapper.selectById(id);
        if (result == null) {
            throw new MenuNotExistException();
        }
        menuMapper.delete(id);
    }
}
