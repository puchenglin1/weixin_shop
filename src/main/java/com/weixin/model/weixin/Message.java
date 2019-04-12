package com.weixin.model.weixin;

import java.io.Serializable;

public class Message implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public Message(String success, String message) {
        super();
        this.success = success;
        this.message = message;
    }

    public Message(String success, Object data) {
        super();
        this.success = success;
        this.data = data;
    }

    public Message(String success, String message, Object data) {
        super();
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public Message() {
        super();
    }

    private String success;

    private Object data;

    private String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}