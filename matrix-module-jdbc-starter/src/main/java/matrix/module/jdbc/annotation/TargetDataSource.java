package matrix.module.jdbc.annotation;

import java.lang.annotation.*;

/**
 * @author WangCheng
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TargetDataSource {

    String value() default "master";

}
