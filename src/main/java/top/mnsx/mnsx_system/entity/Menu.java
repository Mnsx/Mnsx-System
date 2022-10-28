package top.mnsx.mnsx_system.entity;


import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.mnsx.mnsx_system.component.mybatis.annotation.AssignId;
import top.mnsx.mnsx_system.component.mybatis.annotation.TableFill;

/**
 * <p>
 * 
 * </p>
 *
 * @author Mnsx_x
 * @since 2022-10-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @AssignId
    private Long id;

    private String menuName;

    private String content;

    private String router;

    private String roleKey;

    private Integer status;

    private Integer isDeleted;

    @TableFill(insertFlag = true, insertMethod = "getUser")
    private Long createUser;

    @TableFill(insertFlag = true, insertMethod = "getNow")
    private LocalDateTime createTime;

    @TableFill(insertFlag = true, insertMethod = "getUser", updateFlag = true, updateMethod = "getUser")
    private Long updateUser;

    @TableFill(insertFlag = true, insertMethod = "getNow", updateFlag = true, updateMethod = "getNow")
    private LocalDateTime updateTime;
}
