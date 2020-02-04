package matrix.module.common.bean;

import java.io.Serializable;
import com.alibaba.fastjson.JSONObject;
import matrix.module.common.constant.BaseCodeConstant;

/**
 * @author wangcheng
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Boolean isSuccess = false;

    private String msg = "";

    private T body;

    private Integer resultCode = BaseCodeConstant.FAIL;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public Result<T> setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Result<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getBody() {
        return body;
    }

    public Result<T> setBody(T body) {
        this.body = body;
        return this;
    }

    public Integer getResultCode() {
        return resultCode;
    }

    public Result<T> setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
        return this;
    }

    public static <S> Result<S> success(S body) {
        return new Result<S>().setIsSuccess(true).setMsg("SUCCESS")
                .setResultCode(BaseCodeConstant.SUCCESS).setBody(body);
    }

    public static <S> Result<S> fail(String msg) {
        return new Result<S>().setIsSuccess(false).setMsg(msg)
                .setResultCode(BaseCodeConstant.FAIL).setBody(null);
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
