package top.mnsx.mnsx_system;

import com.alibaba.excel.EasyExcel;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import top.mnsx.mnsx_system.dao.UserMapper;
import top.mnsx.mnsx_system.entity.User;
import top.mnsx.mnsx_system.service.UserService;
import top.mnsx.mnsx_system.service.impl.ExcelServiceImpl;
import top.mnsx.mnsx_system.dto.ExportUserDTO;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

@SpringBootTest
class MnsxSystemApplicationTests {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Resource
    private UserMapper userMapper;
    @Resource
    private ExcelServiceImpl excelService;

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


    @Resource
    private UserService userService;

    @Test
    public void testEasyExcel() throws FileNotFoundException {
        File file = new File("D:\\WorkSpace\\Mnsx-System\\test.xlsx");
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        List<ExportUserDTO> exportInfo = userService.getExportInfo(1, 10L);
        EasyExcel.write(fileOutputStream, ExportUserDTO.class).sheet("user").doWrite(() -> exportInfo);
    }

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    public void testMQ() {
        rabbitTemplate.convertAndSend("delayQueue", "routing-key-delay", "发送", new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                message.getMessageProperties().setExpiration("10000");
                return message;
            }
        });
    }
}
