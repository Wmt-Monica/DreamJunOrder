package com.dreamplume.sell.handler;

import com.dreamplume.sell.enums.SellErrorCode;
import com.dreamplume.sell.exception.SellException;
import com.dreamplume.sell.util.ResultVOUtil;
import com.dreamplume.sell.vo.ResultVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Classname SellExceptionHandler
 * @Description TODO
 * @Date 2022/7/27 23:33
 * @Created by 翊
 */
@ControllerAdvice
@Slf4j
public class SellExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResultVO<Object> handleControllerException(Exception ex) {
        if (ex instanceof SellException) {
            SellException sellException = (SellException) ex;
            SellErrorCode errorCode = sellException.getErrorCode();
            log.info("捕获到的异常："+sellException.getErrorCode().getMessage());
            return ResultVOUtil.error(errorCode);
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            return ResultVOUtil.error(SellErrorCode.METHOD_IS_NOT_SUPPORT);
        } else if (ex instanceof MessagingException) {
            return ResultVOUtil.error(SellErrorCode.EMAIL_SEND_FAIL);
        } else {
            log.error("系统内部未知异常：" + ex);
            return ResultVOUtil.error(SellErrorCode.UNKNOWN);
        }
    }
}
