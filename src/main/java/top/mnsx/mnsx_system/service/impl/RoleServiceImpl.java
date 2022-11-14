package top.mnsx.mnsx_system.service.impl;

import org.springframework.beans.BeanUtils;
import top.mnsx.mnsx_system.dto.ExportRoleDTO;
import top.mnsx.mnsx_system.dto.ExportUserDTO;
import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.entity.Menu;
import top.mnsx.mnsx_system.entity.Role;
import top.mnsx.mnsx_system.dao.RoleMapper;
import top.mnsx.mnsx_system.entity.User;
import top.mnsx.mnsx_system.exception.RoleCascadeDeleteException;
import top.mnsx.mnsx_system.exception.RoleHasExistException;
import top.mnsx.mnsx_system.exception.RoleNotExistException;
import top.mnsx.mnsx_system.exception.UserNotExistException;
import top.mnsx.mnsx_system.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
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
    public Page<Role> queryInPage(String roleName, Integer pageNum, Long pageSize) {
        System.out.println("-------------roleName" + roleName);
        List<Role> roles = roleMapper.selectByPage(roleName, pageNum - 1L, pageSize);
        Long count = roleMapper.selectCount(roleName);
        return new Page<Role>().setData(roles).setCount(count);
    }

    @Override
    public Long save(Role role) {
        String roleName = role.getRoleName();
        Role result = roleMapper.selectByRoleName(roleName);
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
    public void remove(Long[] ids) {
        Arrays.stream(ids).forEach((item) -> {
            Role result = roleMapper.selectById(item);
            if (result == null) {
                throw new RoleNotExistException();
            }
            List<Long> userIds = roleMapper.selectUserIdByRoleId(item);
            if (userIds.size() != 0) {
                throw new RoleCascadeDeleteException();
            }
            roleMapper.deleteOne(item);
        });
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
    public void diffMenu(Long roleId, List<Long> selectMenus, List<Long> cancelMenus) {
        // 通过roleId获取当前用户的menuId集合
        List<Menu> menus = roleMapper.selectRoleMenuByRoleId(roleId);

        // 筛去已经存在的菜单编号
        List<Long> oldMenu = menus.stream().map(Menu::getId).collect(Collectors.toList());
        List<Long> newMenu = selectMenus.stream().filter((item) -> !oldMenu.contains(item)).collect(Collectors.toList());

        // 将不存在的菜单编号添加
        if (newMenu.size() != 0) {
            roleMapper.insertRoleMenu(roleId, newMenu);
        }

        // 将被反选的编号对应的数据删除
        if (cancelMenus.size() != 0) {
            roleMapper.deleteRoleMenu(roleId, cancelMenus);
        }
    }

    @Override
    public List<Long> queryMenuIdByRoleId(Long roleId) {
        List<Menu> menus = roleMapper.selectRoleMenuByRoleId(roleId);
        return menus.stream().map(Menu::getId).collect(Collectors.toList());
    }

    @Override
    public void saveUnchecked(Role role) {
        Role result = roleMapper.selectByRoleName(role.getRoleName());
        if (result == null) {
            roleMapper.insertOne(role);
        }
    }
}
