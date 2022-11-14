package top.mnsx.mnsx_system.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.mnsx.mnsx_system.dao.FileMapper;
import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.entity.File;
import top.mnsx.mnsx_system.exception.FileDeleteFailException;
import top.mnsx.mnsx_system.exception.FileNotExistException;
import top.mnsx.mnsx_system.service.FileService;
import top.mnsx.mnsx_system.utils.ImageUtil;
import top.mnsx.mnsx_system.utils.ThreadLocalUtil;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static top.mnsx.mnsx_system.constants.SystemConstants.FILE_LOCATION_PREFIX;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/11/7 17:00
 * @Description:
 */
@Service
public class FileServiceImpl implements FileService {
    @Resource
    private FileMapper fileMapper;

    @Override
    public Page<File> page(File file, Integer pageNum, Long pageSize) {
        List<File> files = fileMapper.selectByPage(file, pageNum - 1, pageSize);
        Long count = fileMapper.selectCount(file);
        return new Page<File>()
                .setData(files)
                .setCount(count);
    }

    @Override
    public Long save(File file, MultipartFile multipartFile) {
        // 编辑路径
        String fileSuffix = ThreadLocalUtil.get().getId() + "/"
                + LocalDateTime.now().getYear() + "/"
                + LocalDateTime.now().getMonthValue() + "/"
                + LocalDateTime.now().getDayOfMonth();
        String path = FILE_LOCATION_PREFIX + fileSuffix;
                // 保存文件
        String fileName = ImageUtil.saveImg(multipartFile, path, file.getFormat());
        // 保存路径
        path = fileSuffix + "/" + fileName;

        // 数据持久化
        // 保存文件信息
        file.setLocation(path);

        return fileMapper.insertOne(file);
    }

    @Override
    public void modifyFile(File file) {
        File result = fileMapper.selectById(file.getId());
        if (result == null) {
            throw new FileNotExistException();
        }
        fileMapper.updateOne(file);
    }

    @Override
    public void removeBath(Long[] ids) {
        Arrays.stream(ids).forEach(item -> {
            File file = fileMapper.selectById(item);
            if (file == null) {
                throw new FileNotExistException();
            }
            java.io.File curFile = new java.io.File(file.getLocation());
            if (curFile.exists()) {
                boolean ifDeleted = curFile.delete();
                if (!ifDeleted) {
                    throw new FileDeleteFailException();
                }
            }
        });
        fileMapper.deleteBath(ids);
    }
}
