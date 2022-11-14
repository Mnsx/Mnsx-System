package top.mnsx.mnsx_system.controller;


import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;
import top.mnsx.mnsx_system.constants.SystemConstants;
import top.mnsx.mnsx_system.dto.ExportUserDTO;
import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.entity.Menu;
import top.mnsx.mnsx_system.entity.User;
import top.mnsx.mnsx_system.service.MenuService;
import top.mnsx.mnsx_system.service.impl.ExcelServiceImpl;
import top.mnsx.mnsx_system.utils.ResultMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
@RestController
@RequestMapping("/menu")
@PreAuthorize("hasAuthority('sys:menu:')")
public class MenuController {

    @Resource
    private MenuService menuService;
    @Resource
    private ExcelServiceImpl excelService;

    @PutMapping("/sys/page/{pageNum}/{pageSize}")
    public String queryByPage(@RequestBody String menuName,
                              @PathVariable("pageNum") Integer pageNum,
                              @PathVariable("pageSize") Long pageSize) {
        menuName = JSON.parseObject(menuName, String.class);
        Page<Menu> page = menuService.queryByPage(menuName, pageNum, pageSize);
        return JSON.toJSONString(ResultMap.ok(page));
    }

    @PostMapping("/sys")
    public String save(@RequestBody Menu menu) {
        Long menuId = menuService.save(menu);
        return JSON.toJSONString(ResultMap.ok(menuId));
    }

    @PutMapping("/sys")
    public String modify(@RequestBody Menu menu) {
        menuService.modify(menu);
        return JSON.toJSONString(ResultMap.ok());
    }

    @DeleteMapping("/sys/{id}")
    public String remove(@PathVariable("id") Long id) {
        menuService.remove(id);
        return JSON.toJSONString(ResultMap.ok());
    }

    /**
     * 将菜单数据导出作为excel
     * @param pageNum 开始页数
     * @param total 一共需要的条数
     * @param response ioc提供
     * @return void response返回excel
     */
    @GetMapping("/sys/ex/{pageNum}/{pageSize}")
    public String exportToExcel(@PathVariable("pageNum") Integer pageNum,
                                @PathVariable("pageSize") Long total,
                                HttpServletResponse response) {
        Page<Menu> page = menuService.queryByPage("", pageNum, total);
        excelService.writeExcel(response, "menu", page::getData);
        return JSON.toJSONString(ResultMap.ok());
    }

    /**
     * 导入数据通过Excel
     * @param file excel文件
     * @return void
     * @throws IOException
     */
    @PreAuthorize("hasAuthority('sys:user:')")
    @PostMapping("/sys/im")
    public String importFromExcel(MultipartFile file) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(file.getInputStream());
        excelService.readExcel(bufferedInputStream, "menu", Menu.class, (dataList) -> {
            dataList.forEach((item) -> {
                menuService.saveUnchecked((Menu) item);
            });
        });
        return JSON.toJSONString(ResultMap.ok());
    }
}

