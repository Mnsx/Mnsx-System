package top.mnsx.mnsx_system.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import top.mnsx.mnsx_system.component.CodeAuthenticationToken;
import top.mnsx.mnsx_system.dto.LoginFormDTO;
import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.dto.UserDTO;
import top.mnsx.mnsx_system.dto.UserSysDTO;
import top.mnsx.mnsx_system.entity.LoginUser;
import top.mnsx.mnsx_system.entity.Role;
import top.mnsx.mnsx_system.entity.User;
import top.mnsx.mnsx_system.dao.UserMapper;
import top.mnsx.mnsx_system.exception.*;
import top.mnsx.mnsx_system.service.RoleService;
import top.mnsx.mnsx_system.service.UserService;
import org.springframework.stereotype.Service;
import top.mnsx.mnsx_system.utils.JWTUtil;
import top.mnsx.mnsx_system.utils.RegexUtil;
import top.mnsx.mnsx_system.utils.ThreadLocalUtil;
import top.mnsx.mnsx_system.dto.ExportUserDTO;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    @Resource
    private RoleService roleService;

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
    @Transactional
    public Long save(User user, Long roleId) {
        String phone = user.getPhone();
        User result = queryByPhone(phone);
        if (result != null) {
            throw new UserHasExistException();
        }
        System.out.println("-------user" + user);
        if (user.getPassword() != null) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            System.out.println("password--------------" + user.getPassword());
        }
        userMapper.insertOne(user);
        this.saveUserRole(user.getId(), roleId);
        return user.getId();
    }

    @Override
    public User queryById(Long id) {
        return userMapper.selectById(id);
    }

    @Override
    public Page<UserSysDTO> page(User user, Integer pageNum, Long pageSize) {
        List<User> users = userMapper.selectByPage(user, pageSize * (pageNum - 1), pageSize);
        Long count = userMapper.selectCount(user);
        List<UserSysDTO> userSysDTOS = users.stream().map((item) -> {
            UserSysDTO userSysDTO = new UserSysDTO();
            Role role = roleService.queryByUserId(item.getId());
            System.out.println(role);
            userSysDTO.setRoleName(role.getRoleName());
            userSysDTO.setUser(item);
            return userSysDTO;
        }).collect(Collectors.toList());
        return new Page<UserSysDTO>()
                .setData(userSysDTOS)
                .setCount(count);
    }

    @Override
    public List<ExportUserDTO> getExportInfo(Integer pageNum, Long total) {
        List<User> users = userMapper.selectByPage(new User(), (long) pageNum - 1, total);
        return users.stream().map((item) -> {
                ExportUserDTO exportUserVO = new ExportUserDTO();
                BeanUtils.copyProperties(item, exportUserVO);
                return exportUserVO;
            }).collect(Collectors.toList());
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
    public void removeOne(Long[] ids) {
        Arrays.stream(ids).forEach((item) -> {
            User user = queryById(item);
            if (user == null) {
                throw new UserNotExistException();
            }
        });
        userMapper.deleteOne(ids);
    }

    @Override
    public void changeUserRoleId(Long userId, Long roleId) {
        Long curId = ThreadLocalUtil.get().getId();
        if (curId.equals(userId)) {
            throw new CurUserCanNotUpdateException();
        }
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new UserNotExistException();
        }
        Role role = roleService.queryById(roleId);
        if (role == null) {
            throw new RoleNotExistException();
        }
        userMapper.updateUserRole(userId, roleId);
    }

    @Override
    public void saveUserRole(Long userId, Long roleId) {
        Role role = roleService.queryById(roleId);
        if (role == null) {
            throw new RoleNotExistException();
        }
        userMapper.insertUserRole(userId, roleId);
    }

    @Override
    public void saveUnchecked(User user, Long defaultRoleId) {
        String phone = user.getPhone();
        User result = queryByPhone(phone);
        if (result == null) {
            userMapper.insertOne(user);
            this.saveUserRole(user.getId(), defaultRoleId);
        }
    }
}
