package top.mnsx.mnsx_system.dao;

import org.apache.ibatis.annotations.Param;
import top.mnsx.mnsx_system.entity.Role;
import top.mnsx.mnsx_system.entity.User;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
public interface UserMapper {

    /**
     * 通过电话号码获取用户信息
     * @param phone 电话号码
     * @return 返回用户信息
     */
    User selectByPhone(String phone);

    /**
     * 插入用户数据
     * @param user 用户信息
     * @return 返回是否成功
     */
    Integer insertOne(User user);

    /**
     * 通过id查找用户
     * @param id
     * @return
     */
    User selectById(Long id);

    /**
     *
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<User> selectByPage(@Param("user") User user,
                            @Param("pageNum") Long pageNum,
                            @Param("pageSize") Long pageSize);

    /**
     * 更新用户信息
     * @param user 用户信息
     */
    Integer updateOne(User user);

    /**
     * 逻辑删除用户
     * @param ids 用户编号
     * @return vod
     */
    Integer deleteOne(@Param("ids") Long[] ids);

    /**
     * 更改用户的角色
     * @param userId 用户编号
     * @param roleId 角色编号
     */
    void updateUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 插入用户角色
     * @param userId 用户信息
     * @param roleId 角色信息
     */
    void insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /**
     * 获取查询总数
     * @param user 条件
     * @return 返回页数
     */
    Long selectCount(@Param("user") User user);
}
