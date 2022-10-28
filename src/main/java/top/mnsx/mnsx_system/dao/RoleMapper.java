package top.mnsx.mnsx_system.dao;

import org.apache.ibatis.annotations.Param;
import top.mnsx.mnsx_system.entity.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
public interface RoleMapper {
    /**
     * 插入角色
     * @param role 角色信息
     * @return void
     */
    Integer insertOne(Role role);

    /**
     * 逻辑删除角色
     * @param id 编号
     * @return void
     */
    Integer deleteOne(Long id);

    /**
     * 更新角色信息
     * @param role 角色信息
     * @return void
     */
    Integer updateOne(Role role);

    /**
     * 分页条件查询
     * @param roleName 条件
     * @param pageNum 页数
     * @param pageSize 每页条数
     * @return 数据
     */
    List<Role> selectByPage(@Param("roleName") String roleName,
                            @Param("pageNum") Integer pageNum,
                            @Param("pageSize") Integer pageSize);

    /**
     * 根据id查询
     * @param id id
     * @return 返回数据
     */
    Role selectById(Long id);
}
