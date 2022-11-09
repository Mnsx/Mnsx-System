package top.mnsx.mnsx_system.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.mnsx.mnsx_system.component.mybatis.annotation.AssignId;
import top.mnsx.mnsx_system.component.mybatis.annotation.TableFill;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @BelongsProject: mnsx_system
 * @User: Mnsx_x
 * @CreateTime: 2022/11/7 16:47
 * @Description: 文件信息类
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class File implements Serializable {
    private static final long serialVersionUID = 1L;

    @AssignId
    private Long id;
    private String fileName;
    private String format;
    private Double size;
    private Integer isDeleted;
    private String location;

    @TableFill(insertFlag = true, insertMethod = "getUser")
    private Long createUser;

    @TableFill(insertFlag = true, insertMethod = "getNow")
    private LocalDateTime createTime;

    @TableFill(insertFlag = true, insertMethod = "getUser", updateFlag = true, updateMethod = "getUser")
    private Long updateUser;

    @TableFill(insertFlag = true, insertMethod = "getNow", updateFlag = true, updateMethod = "getNow")
    private LocalDateTime updateTime;
}
