package top.mnsx.mnsx_system.dao;

import top.mnsx.mnsx_system.entity.User;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
public interface UserMapper {

    User selectByPhone(String phone);

    Integer insertOne(User user);

    User selectById(Long id);
}
