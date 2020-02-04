package matrix.module.based.config;

import matrix.module.common.bean.Result;
import matrix.module.common.constant.BaseCodeConstant;
import matrix.module.common.exception.GlobalControllerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author wangcheng
 */
@Configuration
@ControllerAdvice
public class GlobalExceptionAutoConfiguration {

    private static Logger logger = LogManager.getLogger(GlobalExceptionAutoConfiguration.class);

    @ExceptionHandler(GlobalControllerException.class)
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public Result<?> defaultHandler(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        logger.error(e);
        return Result.fail(e.getMessage()).setResultCode(BaseCodeConstant.FAIL);
    }
}
