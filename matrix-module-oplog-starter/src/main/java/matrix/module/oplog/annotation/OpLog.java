package matrix.module.oplog.annotation;

import java.lang.annotation.*;

/**
 * @author wangcheng
 * @date 2020-03-14
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface OpLog {

    String value() default "NO ACTION NAME";

}
