package com.weixin.mapper;

import com.weixin.model.weixin.AccessToken;
import com.weixin.util.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface WeixinAccessTokenMapper  extends MyMapper <AccessToken>{
    /**
     *
     * @param appid
     * @return
     */
    public AccessToken getToken(@Param("appid") String appid);

    /**
     * 新增记录
     * @param accessToken
     */
    public void insertData(AccessToken accessToken);

    /**
     * 更新token
     * @param accessToken
     */
    public void updateData(AccessToken accessToken);

}
