package top.mnsx.mnsx_system.dto;

import lombok.Data;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/11/2 16:06
 * @Description: excel导出类
 */
@Data
public class ExportUserDTO {
    private String phone;
    private String email;
    private String nickName;
    private String icon;
    private Integer status;
}
