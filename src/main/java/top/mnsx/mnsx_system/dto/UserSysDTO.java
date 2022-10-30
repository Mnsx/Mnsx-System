package top.mnsx.mnsx_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.mnsx.mnsx_system.entity.User;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/10/30 15:26
 * @Description: 用户展示类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSysDTO {
    private User user;
    private String roleName;
}
