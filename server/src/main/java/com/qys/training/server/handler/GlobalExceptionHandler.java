package com.qys.training.server.handler;

import com.qys.training.base.dto.BaseResult;
import com.qys.training.base.enumerate.BizCodeEnum;
import com.qys.training.base.exception.QysException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 捕捉所有异常
    @ExceptionHandler(Exception.class)
    public @ResponseBody
    BaseResult globalExceptionHandler(HttpServletResponse response, Exception e) throws Exception {
        e.printStackTrace();
        BaseResult result = null;
        if (e instanceof QysException) {// 业务异常
            QysException bizException = (QysException) e;
            result = BaseResult.build(bizException.getCode(), bizException.getDescription(), null);
        } else {// 不可预知的异常
            result = BaseResult.build(BizCodeEnum.UNKNOWN_ERROR.getCode(), BizCodeEnum.UNKNOWN_ERROR.getDescription(), null);
        }
        // 指定内部异常
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return result;
    }
}
