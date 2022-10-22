package top.mnsx.mnsx_system.dto;

import lombok.Data;

/**
 * @BelongsProject: mnsx_book
 * @User: Mnsx_x
 * @CreateTime: 2022/10/15 14:40
 * @Description: 登录信息DTO
 */
@Data
public class LoginFormDTO {
    private String phone;
    private String code;
    private String password;
}
