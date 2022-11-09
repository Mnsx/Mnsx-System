package top.mnsx.mnsx_system.dao;

import org.apache.ibatis.annotations.Param;
import top.mnsx.mnsx_system.entity.File;

import java.util.List;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/11/7 16:55
 * @Description:
 */
public interface FileMapper {
    /**
     * 条件查询
     * @param file 文件信息
     * @param pageNum 当前条数
     * @param pageSize 每页条数
     * @return 返回数据
     */
    List<File> selectByPage(@Param("file")File file,
                            @Param("pageNum")Integer pageNum,
                            @Param("pageSize")Long pageSize);

    /**
     * 返回搜索数据的条数
     * @param file 查询条件
     * @return 返回条数
     */
    Long selectCount(@Param("file")File file);

    /**
     * 插入数据
     * @param file 文件信息
     * @return id
     */
    Long insertOne(File file);

    /**
     * 通过编号查询数据
     * @param id 编号
     * @return 数据
     */
    File selectById(Long id);

    /**
     * 更新数据
     * @param file 文件信息
     */
    void updateOne(File file);

    /**
     * 批量逻辑删除
     * @param ids 编号
     */
    void deleteBath(@Param("ids") Long[] ids);
}
