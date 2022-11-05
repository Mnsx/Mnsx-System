package top.mnsx.mnsx_system.service.impl;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.mnsx.mnsx_system.dao.MenuMapper;
import top.mnsx.mnsx_system.dao.UserMapper;
import top.mnsx.mnsx_system.entity.LoginUser;
import top.mnsx.mnsx_system.entity.User;
import top.mnsx.mnsx_system.exception.PasswordNotRightException;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;


/**
 * @BelongsProject: mnsx_book
 * @User: Mnsx_x
 * @CreateTime: 2022/10/15 14:19
 * @Description: Security用户登录细节
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private MenuMapper menuMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        // 验证数据库中是否有用户信息
        User user = userMapper.selectByPhone(phone);
        if (Objects.isNull(user)) {
            throw new PasswordNotRightException();
        }

        // 获取用户权限
        List<String> menus = menuMapper.selectPermsByUserId(user.getId());

        // 返回LoginUser——》继承了UserDetails接口
        return new LoginUser(user, menus);

    }
}