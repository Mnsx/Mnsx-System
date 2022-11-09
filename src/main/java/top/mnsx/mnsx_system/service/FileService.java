package top.mnsx.mnsx_system.service;

import org.springframework.web.multipart.MultipartFile;
import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.entity.File;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/11/7 16:54
 * @Description:
 */
public interface FileService {
    /**
     * 分页查询
     * @param file 条件——文件名、文件类型
     * @param pageNum 当前页数
     * @param pageSize 每页条数
     * @return 返回数据
     */
    Page<File> page(File file, Integer pageNum, Long pageSize);

    /**
     * 添加数据
     * @param file 文件信息
     * @param multipartFile 文件
     * @return 返回id
     */
    Long save(File file, MultipartFile multipartFile);

    /**
     * 更新数据
     * @param file 文件数据
     */
    void modifyFile(File file);

    /**
     * 批量逻辑删除
     * @param ids id
     */
    void removeBath(Long[] ids);
}
