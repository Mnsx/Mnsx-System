package top.mnsx.mnsx_system;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.mnsx.mnsx_system.dao.UserMapper;
import top.mnsx.mnsx_system.entity.User;

import javax.annotation.Resource;

@SpringBootTest
class MnsxSystemApplicationTests {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Resource
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        System.out.println(passwordEncoder.encode("123123"));
    }


    @Test
    public void test1() {
        User user = new User();
        user.setPhone("111");
        user.setNickName("111");
        userMapper.insertOne(user);
    }
}
