package com.weixin.util;

public class WeiXinParams {
    //Token
    public static String token = "kmmiel";

    public static String appid="wxb17c8c7059dc63ce";

    public static String appsecret="4fd961d009a84c78a096ffc5da813a51";
    //获取token
    public static String access_token_url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    //网页授权
    public static String authorize_url="https://open.weixin.qq.com/connect/oauth2/authorize?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
    //网页授权回调地址
    public static String redirect_uri="http://puchenglin1.uicp.net/core/callBack";
    //获取网页授权token
    public static String authorize_access_token_url="https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    //创建菜单
    public static String create_menu_url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
}
