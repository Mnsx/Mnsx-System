package top.mnsx.mnsx_system.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import top.mnsx.mnsx_system.component.CodeAuthenticationToken;
import top.mnsx.mnsx_system.dto.LoginFormDTO;
import top.mnsx.mnsx_system.dto.UserDTO;
import top.mnsx.mnsx_system.entity.LoginUser;
import top.mnsx.mnsx_system.entity.User;
import top.mnsx.mnsx_system.dao.UserMapper;
import top.mnsx.mnsx_system.exception.LoginFailException;
import top.mnsx.mnsx_system.exception.PhoneNotFormatException;
import top.mnsx.mnsx_system.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.mnsx.mnsx_system.utils.JWTUtil;
import top.mnsx.mnsx_system.utils.RegexUtil;
import top.mnsx.mnsx_system.utils.ResultMap;
import top.mnsx.mnsx_system.utils.ThreadLocalUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static top.mnsx.mnsx_system.utils.RedisConstants.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String sendCodeDemo(String phone) {
        // 验证手机号格式
        if (RegexUtil.isPhoneInvalid(phone)) {
            // 如果不符合，返回错误信息
            throw new PhoneNotFormatException();
        }
        // RandomUtl生成验证码
        String code = RandomUtil.randomNumbers(6);
        // 将验证码保存到redis中
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY + phone, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
        // 返回验证码
        return JSON.toJSONString(ResultMap.ok(code));
    }

    @Override
    public String login(LoginFormDTO loginForm) {
        // 验证手机号码是否正确
        String phone = loginForm.getPhone();
        if (RegexUtil.isPhoneInvalid(phone)) {
            // 如果手机格式错误，返回错误信息
            throw new PhoneNotFormatException();
        }

        AbstractAuthenticationToken authenticationToken = null;
        // 获取用户输入验证码
        // 让AuthenticationManager验证用户登录信息是否正确，调用自定义的UserDetailsServiceImpl
        if (StrUtil.isNotBlank(loginForm.getCode())) {
            authenticationToken = new CodeAuthenticationToken(loginForm.getPhone(), loginForm.getCode());
        } else if (StrUtil.isNotBlank(loginForm.getPassword())) {
            // 密码登录
            authenticationToken = new UsernamePasswordAuthenticationToken(loginForm.getPhone(), loginForm.getPassword());
        } else {
            throw new LoginFailException();
        }

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authenticate)) {
            throw new LoginFailException();
        }

        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Long id = loginUser.getUser().getId();

        Map<String, String> map = new HashMap<>();
        map.put("userId", JSON.toJSONString(id));
        map.put("loginTime", JSON.toJSONString(LocalDateTime.now()));
        String token = JWTUtil.getToken(map);

        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY + id, JSON.toJSONString(loginUser), LOGIN_USER_TTL, TimeUnit.MINUTES);

        return token;
    }

    @Override
    public String logout() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();

        LoginUser loginUser = (LoginUser) authenticationToken.getPrincipal();
        Long id = loginUser.getUser().getId();

        // 删除redis中当前用户的字段
        stringRedisTemplate.delete(LOGIN_USER_KEY + id);
        return JSON.toJSONString(ResultMap.ok());
    }

    @Override
    public String getInfo() {
        // 从ThreadLocal中获取用户id
        UserDTO userDTO = ThreadLocalUtil.get();
        // 隐藏重要信息
        userDTO.setId(null);
        // 返回
        return JSON.toJSONString(ResultMap.ok(userDTO));
    }
}
