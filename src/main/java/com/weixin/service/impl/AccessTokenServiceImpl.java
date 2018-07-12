package com.weixin.service.impl;

import com.weixin.mapper.WeixinAccessTokenMapper;
import com.weixin.model.weixin.AccessToken;
import com.weixin.service.AccessTokenService;
import com.weixin.util.WeiXinParams;
import com.weixin.util.WeiXinUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service("accessTokenService")
public class AccessTokenServiceImpl implements AccessTokenService {
    @Resource
    private WeixinAccessTokenMapper weixinAccessTokenMapper;

    @Override
    public AccessToken getToken() {
        String appid=WeiXinParams.appid;
        AccessToken token=weixinAccessTokenMapper.getToken(appid);
        if(null!=token){
            long timeInterval=new Date().getTime()-token.getCreateDate().getTime();
            if(timeInterval>=token.getExpiresIn()){
                token=WeiXinUtil.getAccessToken(appid,WeiXinParams.appsecret);
                token.setCreateDate(new Date());
                token.setAppid(appid);
            }
        }else{
            token=WeiXinUtil.getAccessToken(appid,WeiXinParams.appsecret);
            token.setCreateDate(new Date());
            token.setAppid(appid);
        }
        return token;
    }
}
