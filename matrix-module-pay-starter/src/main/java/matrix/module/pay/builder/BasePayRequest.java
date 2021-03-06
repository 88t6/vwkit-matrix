package matrix.module.pay.builder;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * @author wangcheng
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BasePayRequest implements Serializable {

    private static final long serialVersionUID = 1L;

}
