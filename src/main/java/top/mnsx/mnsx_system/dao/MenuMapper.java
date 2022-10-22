package top.mnsx.mnsx_system.dao;

import top.mnsx.mnsx_system.entity.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
public interface MenuMapper extends BaseMapper<Menu> {

    // TODO: 2022/10/22 获取用户role_key
    List<String> selectPermsByUserId(Long id);
}
