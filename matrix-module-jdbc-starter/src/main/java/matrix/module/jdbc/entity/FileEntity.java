package matrix.module.jdbc.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.InputStream;
import java.io.Serializable;

/**
 * @author wangcheng
 * date 2020-03-14
 */
@Data
@Accessors(chain = true)
public class FileEntity implements Serializable {

    private String fileName;

    private InputStream inputStream;
}
