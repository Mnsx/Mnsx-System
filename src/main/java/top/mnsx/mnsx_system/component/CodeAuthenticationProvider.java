package top.mnsx.mnsx_system.component;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import top.mnsx.mnsx_system.dao.MenuMapper;
import top.mnsx.mnsx_system.entity.LoginUser;
import top.mnsx.mnsx_system.entity.User;
import top.mnsx.mnsx_system.exception.CodeNotRightException;
import top.mnsx.mnsx_system.service.UserService;
import top.mnsx.mnsx_system.utils.RedisConstants;
import top.mnsx.mnsx_system.utils.SystemConstants;

import javax.annotation.Resource;
import java.util.List;

/**
 * @BelongsProject: mnsx_book
 * @User: Mnsx_x
 * @CreateTime: 2022/10/15 15:24
 * @Description: SpringSecurity提供验证码登录
 */
@Component
public class CodeAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserService userService;
    @Resource
    private MenuMapper menuMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String phone = (String) authentication.getPrincipal();
        String code = (String) authentication.getCredentials();

        String cacheCode = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + phone);
        cacheCode = JSON.parseObject(cacheCode, String.class);
        if (cacheCode == null || !cacheCode.equals(code)) {
            // 不一致报错
            throw new CodeNotRightException();
        }
        // 一致，根据手机号查询用户是否存在
        User user = userService.query().eq("phone", phone).one();
        if (user == null) {
            // 不存在，存储用户到数据库
            user = createUserWithPhone(phone);
        }

        LoginUser loginUser = new LoginUser();
        loginUser.setUser(user);

        // 获取用户权限
        List<String> menus = menuMapper.selectPermsByUserId(user.getId());
        // 返回LoginUser——》继承了UserDetails接口
        loginUser.setPermissions(menus);

        CodeAuthenticationToken result = new CodeAuthenticationToken(loginUser, loginUser.getAuthorities());
        result.setDetails(authentication.getDetails());

        return result;
    }

    private User createUserWithPhone(String phone) {
        // 创建用户
        User user = new User();
        user.setPhone(phone);
        user.setNickName(SystemConstants.USER_NICK_NAME_PREFIX + RandomUtil.randomString(7));
        // 保存用户
        userService.save(user);
        return user;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
