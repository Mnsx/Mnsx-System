package top.mnsx.mnsx_system.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.mnsx.mnsx_system.component.CodeAuthenticationToken;
import top.mnsx.mnsx_system.dto.LoginFormDTO;
import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.dto.UserDTO;
import top.mnsx.mnsx_system.entity.LoginUser;
import top.mnsx.mnsx_system.entity.User;
import top.mnsx.mnsx_system.dao.UserMapper;
import top.mnsx.mnsx_system.exception.LoginFailException;
import top.mnsx.mnsx_system.exception.PhoneNotFormatException;
import top.mnsx.mnsx_system.exception.UserHasExistException;
import top.mnsx.mnsx_system.exception.UserNotExistException;
import top.mnsx.mnsx_system.service.UserService;
import org.springframework.stereotype.Service;
import top.mnsx.mnsx_system.utils.JWTUtil;
import top.mnsx.mnsx_system.utils.RegexUtil;
import top.mnsx.mnsx_system.utils.ResultMap;
import top.mnsx.mnsx_system.utils.ThreadLocalUtil;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static top.mnsx.mnsx_system.constants.RedisConstants.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserMapper userMapper;
    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
        return code;
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
    public void logout() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken)
                SecurityContextHolder.getContext().getAuthentication();

        LoginUser loginUser = (LoginUser) authenticationToken.getPrincipal();
        Long id = loginUser.getUser().getId();

        // 删除redis中当前用户的字段
        stringRedisTemplate.delete(LOGIN_USER_KEY + id);
    }

    @Override
    public UserDTO getInfo() {
        // 从ThreadLocal中获取用户id
        UserDTO userDTO = ThreadLocalUtil.get();
        // 隐藏重要信息
        userDTO.setId(null);
        // 返回
        return userDTO;
    }

    @Override
    public User queryByPhone(String phone) {
       return userMapper.selectByPhone(phone);
    }

    @Override
    public Long save(User user) {
        String phone = user.getPhone();
        User result = queryByPhone(phone);
        if (result != null) {
            throw new UserHasExistException();
        }
        if (user.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userMapper.insertOne(user);
        return user.getId();
    }

    @Override
    public User queryById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public Page<User> page(User user, Integer pageNum, Integer pageSize) {
        List<User> users = userMapper.selectByPage(user, pageNum - 1, pageSize);
        return new Page<User>()
                .setData(users)
                .setCount((long) users.size());
    }

    @Override
    public void modifyOne(User user) {
        User result = queryById(user.getId());
        if (result == null) {
            throw new UserNotExistException();
        }
        if (StringUtils.isNotBlank(user.getPassword())) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        }
        userMapper.updateOne(user);
    }

    @Override
    public void removeOne(Long id) {
        User user = queryById(id);
        if (user == null) {
            throw new UserNotExistException();
        }
        userMapper.deleteOne(id);
    }
}
