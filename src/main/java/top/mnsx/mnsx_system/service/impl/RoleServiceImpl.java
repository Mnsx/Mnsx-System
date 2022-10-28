package top.mnsx.mnsx_system.service.impl;

import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.entity.Role;
import top.mnsx.mnsx_system.dao.RoleMapper;
import top.mnsx.mnsx_system.exception.RoleHasExistException;
import top.mnsx.mnsx_system.exception.RoleNotExistException;
import top.mnsx.mnsx_system.service.RoleService;
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
}
