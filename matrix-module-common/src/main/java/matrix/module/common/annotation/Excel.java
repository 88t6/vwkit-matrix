package matrix.module.common.annotation;

import java.lang.annotation.*;

/**
 * @author wangcheng
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.TYPE, ElementType.FIELD})
public @interface Excel {

    String value() default "";

    int width() default 200;
}
