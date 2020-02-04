package matrix.module.common.exception;

/**
 * @author wangcheng
 */
public class GlobalControllerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GlobalControllerException() {
        super();
    }

    public GlobalControllerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public GlobalControllerException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlobalControllerException(String message) {
        super(message);
    }

    public GlobalControllerException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

}
