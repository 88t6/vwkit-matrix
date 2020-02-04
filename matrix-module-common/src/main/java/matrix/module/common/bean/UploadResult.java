package matrix.module.common.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * @author wangcheng
 */
public class UploadResult implements Serializable {
    private static final long serialVersionUID = 1L;
    private String fieldName;
    private String fileName;
    private String sysFileName;
    private boolean success = false;
    private String message;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSysFileName() {
        return sysFileName;
    }

    public void setSysFileName(String sysFileName) {
        this.sysFileName = sysFileName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UploadResult(String fieldName, String fileName, String sysFileName) {
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.sysFileName = sysFileName;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

}
