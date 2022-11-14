package top.mnsx.mnsx_system.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.entity.File;
import top.mnsx.mnsx_system.entity.Menu;
import top.mnsx.mnsx_system.service.FileService;
import top.mnsx.mnsx_system.utils.ResultMap;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/11/7 16:53
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/file/info")
public class FileController {
    @Resource
    private FileService fileService;

    /**
     * 分页条件查询
     * @param file 条件——文件名、文件类型
     * @param pageNum 当前页
     * @param pageSize 每页条数
     * @return 返回数据
     */
    @GetMapping("/sys/page/{pageNum}/{pageSize}")
    public String getPage(@RequestBody File file,
                          @PathVariable("pageNum") Integer pageNum,
                          @PathVariable("pageSize") Long pageSize) {
        Page<File> page = fileService.page(file, pageNum, pageSize);
        return JSON.toJSONString(ResultMap.ok(page));
    }

    /**
     * 保存数据
     * @param fileInfoStr json数据，文件名、类型、大小
     * @param multipartFile 文件
     * @return 返回id
     */
    @PostMapping("/sys")
    public String save(@RequestParam("fileInfo") String fileInfoStr, @RequestParam("file") MultipartFile multipartFile) {
        File file = JSON.parseObject(fileInfoStr, File.class);
        file.setSize((double) multipartFile.getSize());
        Long fileId = fileService.save(file, multipartFile);
        return JSON.toJSONString(ResultMap.ok(fileId));
    }

    /**
     * 更新数据
     * @param file 文件名、文件类型
     * @return void
     */
    @PutMapping("/sys")
    public String modify(@RequestBody File file) {
        fileService.modifyFile(file);
        return JSON.toJSONString(ResultMap.ok());
    }

    /**
     * 逻辑删除数据
     * @param ids 编号
     * @return void
     */
    @DeleteMapping("/sys")
    public String remove(@RequestBody Long[] ids) {
        fileService.removeBath(ids);
        return JSON.toJSONString(ResultMap.ok());
    }
}
