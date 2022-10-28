package top.mnsx.mnsx_system.filter;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import top.mnsx.mnsx_system.dto.UserDTO;
import top.mnsx.mnsx_system.entity.LoginUser;
import top.mnsx.mnsx_system.entity.User;
import top.mnsx.mnsx_system.service.UserService;
import top.mnsx.mnsx_system.utils.JWTUtil;
import top.mnsx.mnsx_system.utils.ThreadLocalUtil;
import top.mnsx.mnsx_system.utils.WebUtil;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static top.mnsx.mnsx_system.constants.RedisConstants.LOGIN_USER_KEY;
import static top.mnsx.mnsx_system.constants.RedisConstants.LOGIN_USER_TTL;

/**
 * @BelongsProject: mnsx_book
 * @User: Mnsx_x
 * @CreateTime: 2022/10/15 14:01
 * @Description: jwt登录过滤器
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 获取请求头
        String token = request.getHeader("Authorization");
        // token字段为空，直接放行，因为SecirityContextHolder中没有这个用户的信息所以不能通过过滤连
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        String userId = null;
        // 解析token后去用户id
        try {
            DecodedJWT jwt = JWTUtil.getToken(token);
            userId = jwt.getClaim("userId").asString();
        } catch (Exception e) {
//            e.printStackTrace();
            WebUtil.renderString(response, "非法token");
            return;
        }
        // 从redis中获取用户的信息
        String redisKey = LOGIN_USER_KEY + userId;
        String json = stringRedisTemplate.opsForValue().get(redisKey);
        LoginUser loginUser = JSON.parseObject(json, LoginUser.class);

        if (Objects.isNull(loginUser)) {
            WebUtil.renderString(response, "用户未登录");
            return;
        }

        // 将用户放入LoginUser
        Long id = loginUser.getUser().getId();
        User user = userService.queryById(id);
        UserDTO userDTO = new UserDTO();
        BeanUtil.copyProperties(user, userDTO);
        ThreadLocalUtil.add(userDTO);

        // 获取LoginUser中的权限集合
        Collection<? extends GrantedAuthority> authorities = loginUser.getAuthorities();
        // 将用户信息和权限加入到SecurityContextHolder中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 刷新redis中的过期时间
        stringRedisTemplate.expire(LOGIN_USER_KEY + id, LOGIN_USER_TTL, TimeUnit.MINUTES);

        filterChain.doFilter(request, response);
    }


}
