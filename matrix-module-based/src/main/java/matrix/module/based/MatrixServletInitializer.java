package matrix.module.based;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @author wangcheng
 */
public abstract class MatrixServletInitializer extends SpringBootServletInitializer {

    public abstract Class<?>[] builderClass();

    private Class<?>[] getClassList() {
        return MatrixApplication.getClassList(this.builderClass());
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        builder.properties(MatrixApplication.properties);
        return builder.sources(this.getClassList());
    }

}
