package com.weixin.service.impl;

import com.weixin.service.AccessTokenService;
import com.weixin.util.WeiXinParams;
import com.weixin.util.WeiXinUtil;
import org.springframework.stereotype.Service;



@Service("accessTokenService")
public class AccessTokenServiceImpl implements AccessTokenService {

    @Override
    public String getToken() {
        String token=WeiXinUtil.getAccessToken(WeiXinParams.appid,WeiXinParams.appsecret);
        return token;
    }
}
