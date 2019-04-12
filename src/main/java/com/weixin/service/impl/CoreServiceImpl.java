package com.weixin.service.impl;

import com.weixin.mapper.WeixinUserMapper;
import com.weixin.message.resp.TextMessage;
import com.weixin.model.weixin.WeixinUser;
import com.weixin.service.CoreService;
import com.weixin.util.MessageUtil;
import com.weixin.util.WeiXinParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Map;

@Service("coreService")
public class CoreServiceImpl  implements CoreService{
    private final static Logger log= LoggerFactory.getLogger(CoreServiceImpl.class);
    @Resource
    private WeixinUserMapper weixinUserMapper;
    /**
     * 处理微信发送过来的请求
     * @param request
     * @return
     */
    @Override
    public String processRequest(HttpServletRequest request){
        String respMessage=null;
        try{
            String respContent="请求处理异常，请稍后尝试！";
            //xml请求解析
            Map<String,String> requestMap= MessageUtil.parseXML(request);
            //发送方账号(open_id)
            String fromUserName=requestMap.get("FromUserName");
            //公众账号
            String toUsreName=requestMap.get("ToUserName");
            //消息类型
            String msgType=requestMap.get("MsgType");

            //回复文本消息
            TextMessage textMessage=new TextMessage();
            textMessage.setToUserName(fromUserName);
            textMessage.setFromUserName(toUsreName);
            textMessage.setCreateTime(System.currentTimeMillis());
            textMessage.setMsgType(MessageUtil.RESP_MESSAGE_TYPE_TEXT);
            textMessage.setFuncFlag(0);

            //文本消息
            if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)){
                respContent="您发送的是文本消息！";
            }
            //图片消息
            else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_TEXT)){
                respContent="";
            }
            //地理位置消息
            else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LOCATION)){

                respContent="";
            }
            //链接消息
            else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_LINK)){
                respContent="";
            }
            else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_VOICE)){
                respContent="";
            }
            //事件推送
            else if(msgType.equals(MessageUtil.REQ_MESSAGE_TYPE_EVENT)){
                //事件类型
                String eventType=requestMap.get("Event");
                WeixinUser weixinUser=new WeixinUser();
                weixinUser.setOpenId(fromUserName);
                weixinUser.setWeixinId(toUsreName);
                weixinUser.setCreateTime(Integer.parseInt(requestMap.get("CreateTime")));
                weixinUser.setUpdateTime(Integer.parseInt(requestMap.get("CreateTime")));
                //订阅
                if(eventType.equals(MessageUtil.EVENT_TYPE_SUBSCRIBE)){
                    weixinUser.setStatus(0);
                    int flag=weixinUserMapper.isHaveDate(fromUserName,toUsreName);
                    if(flag==0){
                        weixinUserMapper.subscribe(weixinUser);
                    }else{
                        weixinUserMapper.updateSubscribe(weixinUser);
                    }

                    respContent="谢谢您的关注！";
                }
                //取消订阅
                else if(eventType.equals(MessageUtil.EVENT_TYPE_UNSUBSCRIBE)){
                    weixinUser.setStatus(1);
                    int flag=weixinUserMapper.isHaveDate(fromUserName,toUsreName);
                    if(flag==0){
                        weixinUserMapper.subscribe(weixinUser);
                    }else{
                        weixinUserMapper.updateSubscribe(weixinUser);
                    }
                    respContent=" ";
                }
                //自定义菜单点击事件
                else if(eventType.equals(MessageUtil.EVENT_TYPE_CLICK)){
                    respContent="";
                }
            }
            textMessage.setContent(respContent);
            respMessage=MessageUtil.textMessageToXml(textMessage);
        }catch (Exception e){
            e.printStackTrace();
        }
        return respMessage;
    }

    @Override
    public void wxLogin(HttpServletResponse response) {
        String url = WeiXinParams.authorize_url.replace("APPID",WeiXinParams.appid).replace("REDIRECT_URI", URLEncoder.encode(WeiXinParams.redirect_uri));
        try{
            response.sendRedirect(url);
        }catch (Exception e){
            log.error("网页授权异常：",e);
        }

    }

    @Override
    public void callBack(HttpServletRequest request, HttpServletResponse response) {
        //第二步：通过code换取网页授权access_token

        //从request里面获取code参数(当微信服务器访问回调地址的时候，会把code参数传递过来)
        String code = request.getParameter("code");

        System.out.println("code:"+code);

        //获取code后，请求以下链接获取access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + AuthUtil.APPID
                + "&secret=" + AuthUtil.APPSECRET
                + "&code=" + code
                + "&grant_type=authorization_code";

        //通过网络请求方法来请求上面这个接口
        JSONObject jsonObject = AuthUtil.doGetJson(url);

        System.out.println("==========================jsonObject"+jsonObject);

        //从返回的JSON数据中取出access_token和openid，拉取用户信息时用
        String token =  jsonObject.getString("access_token");
        String openid = jsonObject.getString("openid");

        // 第三步：刷新access_token（如果需要）

        // 第四步：拉取用户信息(需scope为 snsapi_userinfo)
        String infoUrl ="https://api.weixin.qq.com/sns/userinfo?access_token=" + token
                + "&openid=" + openid
                + "&lang=zh_CN";
        //通过网络请求方法来请求上面这个接口
        JSONObject userInfo = AuthUtil.doGetJson(infoUrl);

        System.out.println(userInfo);

    }
}
