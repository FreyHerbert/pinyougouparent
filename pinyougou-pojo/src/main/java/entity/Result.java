package entity;

import java.io.Serializable;

/**
 * 在增删改后，对返回结果进行封装的类
 * 方便转成 json 格式
 * @author leiyu
 */
public class Result implements Serializable {
    /**
     * 状态值，判断操作是否成功
     */
    private boolean success;

    /**
     * 需要返回的信息
     */
    private String message;

    public Result() {
        success = true;
        message = "";
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
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
}
