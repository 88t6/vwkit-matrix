package matrix.module.based.config;

import matrix.module.common.bean.Result;
import matrix.module.common.constant.BaseCodeConstant;
import matrix.module.common.exception.GlobalControllerException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * @author wangcheng
 */
@Configuration
@ControllerAdvice
public class GlobalExceptionAutoConfiguration {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionAutoConfiguration.class);

    @ExceptionHandler(GlobalControllerException.class)
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public Result<?> defaultHandler(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        Throwable throwable = e.getCause();
        while (throwable instanceof GlobalControllerException && throwable.getCause() != null) {
            throwable = throwable.getCause();
        }
        if (throwable instanceof ConstraintViolationException) {
            //内部异常校验
            ConstraintViolationException ex = (ConstraintViolationException) throwable;
            logger.error(ex.getConstraintViolations().stream().map(item -> item.getRootBeanClass().getName()
                    + "." + item.getPropertyPath().toString() + ":" + item.getMessage()).collect(Collectors.joining("|")));
            return Result.fail(ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("|"))).setResultCode(BaseCodeConstant.FAIL);
        }
        logger.error(throwable);
        return Result.fail(throwable.getMessage()).setResultCode(BaseCodeConstant.FAIL);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public Result<?> defaultHandler(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        logger.error(bindingResult.getAllErrors().stream().map(item -> item.getObjectName() + ":" + item.getDefaultMessage()).collect(Collectors.joining("|")));
        return Result.fail(bindingResult.getAllErrors().stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining("|"))).setResultCode(BaseCodeConstant.FAIL);
    }
}
