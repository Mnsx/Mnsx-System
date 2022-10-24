package top.mnsx.mnsx_system.dao;

import top.mnsx.mnsx_system.entity.Menu;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
public interface MenuMapper {

    List<String> selectPermsByUserId(Long id);
}
