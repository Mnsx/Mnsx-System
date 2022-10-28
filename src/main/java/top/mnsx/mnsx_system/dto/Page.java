package top.mnsx.mnsx_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/10/26 14:52
 * @Description: 分页返回数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Page<T> {
    private List<T> data;
    private Long count;
}
