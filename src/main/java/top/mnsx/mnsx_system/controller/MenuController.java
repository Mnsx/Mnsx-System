package top.mnsx.mnsx_system.controller;


import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.*;

import top.mnsx.mnsx_system.dto.Page;
import top.mnsx.mnsx_system.entity.Menu;
import top.mnsx.mnsx_system.service.MenuService;
import top.mnsx.mnsx_system.utils.ResultMap;

import javax.annotation.Resource;

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
public class MenuController {

    @Resource
    private MenuService menuService;

    @GetMapping("/sys/page/{pageNum}/{pageSize}")
    public String queryByPage(@RequestBody Menu menu,
                              @PathVariable("pageNum") Integer pageNum,
                              @PathVariable("pageSize") Integer pageSize) {
        String menuName = menu.getMenuName();
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
}

