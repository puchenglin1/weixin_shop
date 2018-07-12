package com.weixin.model.weixin;

import java.util.Date;

public class AccessToken {

    private String appid;
    //获取到的凭证
    private String accessToken;
    //凭证有效时间，单位：秒
    private int expiresIn;
    //操作时间
    private Date createDate;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
