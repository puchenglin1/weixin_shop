package com.weixin.service.impl;

import com.weixin.mapper.WeixinUserMapper;
import com.weixin.message.resp.TextMessage;
import com.weixin.model.weixin.WeixinUser;
import com.weixin.service.CoreService;
import com.weixin.util.MessageUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.soap.Text;
import java.util.Date;
import java.util.Map;

@Service("coreService")
public class CoreServiceImpl  implements CoreService{
    @Resource
    private WeixinUserMapper weixinUserMapper;
    /**
     * 处理微信发送过来的请求
     * @param request
     * @return
     */
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
            textMessage.setCreateTime(new Date().getTime());
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
}
