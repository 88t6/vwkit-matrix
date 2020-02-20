package matrix.module.jdbc.annotation;

import java.lang.annotation.*;

/**
 * @author WangCheng
 * @date 2020/2/20
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface MatrixColumn {

    String value() default "";

}
