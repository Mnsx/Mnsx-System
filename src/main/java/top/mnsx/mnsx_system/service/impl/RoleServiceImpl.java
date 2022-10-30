package top.mnsx.mnsx_system.service.impl;

import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.entity.Menu;
import top.mnsx.mnsx_system.entity.Role;
import top.mnsx.mnsx_system.dao.RoleMapper;
import top.mnsx.mnsx_system.exception.RoleHasExistException;
import top.mnsx.mnsx_system.exception.RoleNotExistException;
import top.mnsx.mnsx_system.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Override
    public Page<Role> queryInPage(String roleName, Integer pageNum, Integer pageSize) {
        List<Role> roles = roleMapper.selectByPage(roleName, pageNum - 1, pageSize);
        return new Page<Role>().setData(roles).setCount((long) roles.size());
    }

    @Override
    public Long save(Role role) {
        Long id = role.getId();
        Role result = roleMapper.selectById(id);
        if (result != null) {
            throw new RoleHasExistException();
        }
        roleMapper.insertOne(role);
        return role.getId();
    }

    @Override
    public void modify(Role role) {
        Long id = role.getId();
        Role result = roleMapper.selectById(id);
        if (result == null) {
            throw new RoleNotExistException();
        }
        roleMapper.updateOne(role);
    }

    @Override
    public void remove(Long id) {
        Role result = roleMapper.selectById(id);
        if (result == null) {
            throw new RoleNotExistException();
        }
        roleMapper.deleteOne(id);
    }

    @Override
    public Role queryById(Long roleId) {
        return roleMapper.selectById(roleId);
    }

    @Override
    public Role queryByUserId(Long id) {
        return roleMapper.selectByUserId(id);
    }

    @Override
    public void diffMenu(Long roleId, List<Long> menuId) {
        // 通过roleId获取当前用户的menuId集合
        List<Menu> menus = roleMapper.selectRoleMenuByRoleId(roleId);

        // 筛去已经存在的菜单编号
        List<Long> oldMenu = menus.stream().map(Menu::getId).collect(Collectors.toList());
        List<Long> newMenu = menuId.stream().filter((item) -> !oldMenu.contains(item)).collect(Collectors.toList());
        System.out.println(menus);
        System.out.println(oldMenu);
        System.out.println(newMenu);

        // 将不存在的菜单编号添加
        if (newMenu.size() != 0) {
            roleMapper.insertRoleMenu(roleId, newMenu);
        }

        // 筛去被删除的菜单编号
        List<Long> deleteMenu = oldMenu.stream().filter(menuId::contains).collect(Collectors.toList());

        // 将被反选的编号对应的数据删除
        if (deleteMenu.size() != 0) {
            roleMapper.deleteRoleMenu(roleId, deleteMenu);
        }
    }
}
