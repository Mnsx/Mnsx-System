package top.mnsx.mnsx_system.dto;

import lombok.Data;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/11/4 16:07
 * @Description:
 */
@Data
public class ExportRoleDTO {
    private String roleName;

    private String content;

    private Integer status;
}
