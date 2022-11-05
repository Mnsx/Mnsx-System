package top.mnsx.mnsx_system.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import top.mnsx.mnsx_system.exception.*;
import top.mnsx.mnsx_system.utils.ResultCode;
import top.mnsx.mnsx_system.utils.ResultMap;

/**
 * @BelongsProject: mnsx_book
 * @User: Mnsx_x
 * @CreateTime: 2022/10/16 15:50
 * @Description:
 */
@ControllerAdvice
@ResponseBody
@Slf4j
public class MyExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public String exceptionHandler(Throwable e) {
        ResultCode resultCode = null;

        if (e instanceof PhoneNotFormatException) {
            resultCode = ResultCode.PHONE_NOT_FORMAT;
        } else if (e instanceof CodeNotRightException) {
            resultCode = ResultCode.CODE_NOT_RIGHT;
        } else if (e instanceof LoginFailException || e instanceof InternalAuthenticationServiceException) {
            resultCode = ResultCode.LOGIN_FAIL;
        } else if (e instanceof PasswordNotRightException || e instanceof BadCredentialsException) {
            resultCode = ResultCode.PASSWORD_NOT_RIGHT;
        } else if (e instanceof AccessDeniedException) {
            resultCode = ResultCode.PERMISSION_NOT_ENOUGH;
        } else if (e instanceof UserNotExistException) {
            resultCode = ResultCode.USER_NOT_EXIST;
        } else if (e instanceof RoleNotExistException) {
            resultCode = ResultCode.ROLE_NOT_EXIST;
        } else if (e instanceof RoleHasExistException) {
            resultCode = ResultCode.ROLE_HAS_EXIST;
        } else if (e instanceof UserHasExistException) {
            resultCode = ResultCode.User_HAS_EXIST;
        } else if (e instanceof MenuNotExistException) {
            resultCode = ResultCode.MENU_NOT_EXIST;
        } else if (e instanceof MenuHasExistException) {
            resultCode = ResultCode.MENU_HAS_EXIST;
        } else if (e instanceof MenuCascadeDeleteException) {
            resultCode = ResultCode.MENU_CASCADE_DELETE_ERROR;
        } else if (e instanceof RoleCascadeDeleteException) {
            resultCode = ResultCode.ROLE_CASCADE_DELETE_ERROR;
        } else if (e instanceof CurUserCanNotUpdateException) {
            resultCode = ResultCode.CUR_USER_CAN_NOT_UPDATE;
        } else {
            resultCode = ResultCode.INNER_ERROR;
            e.printStackTrace();
        }

        return JSON.toJSONString(ResultMap.fail(resultCode));
    }
}
