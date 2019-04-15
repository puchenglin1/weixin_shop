package com.weixin.service.impl;

import com.weixin.service.AccessTokenService;
import com.weixin.util.WeiXinParams;
import com.weixin.util.WeiXinUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service("accessTokenService")
public class AccessTokenServiceImpl implements AccessTokenService {

    @Resource
    private WeiXinUtil weiXinUtil;

    @Override
    public String getToken() {
        String token=weiXinUtil.getAccessToken(WeiXinParams.appid,WeiXinParams.appsecret);
        return token;
    }
}
