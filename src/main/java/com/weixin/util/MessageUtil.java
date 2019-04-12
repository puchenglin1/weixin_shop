package com.weixin.util;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.weixin.message.resp.TextMessage;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息工具类
 */
public class MessageUtil {
    /**
     * 返回消息类型：文本
     */
    public static final String RESP_MESSAGE_TYPE_TEXT="text";
    /**
     * 返回消息类型:音乐
     */
    public static final String RESP_MESSAGE_TYPE_MUSIC="music";
    /**
     * 返回消息类型：图文
     */
    public static final String RESP_MESSAGE_TYPE_NEWS="news";

    /**
     * 请求消息类型：文本
     */
    public static final String REQ_MESSAGE_TYPE_TEXT="text";
    /**
     * 请求消息类型：图片
     */
    public static final String REQ_MESSAGE_TYPE_IMAGE="image";
    /**
     * 请求消息类型：连接
     */
    public static final String REQ_MESSAGE_TYPE_LINK="link";
    /**
     * 请求消息类型：地理位置
     */
    public static final String REQ_MESSAGE_TYPE_LOCATION="location";
    /**
     * 请求消息类型：音频
     */
    public static final String REQ_MESSAGE_TYPE_VOICE="voice";
    /**
     * 请求消息类型：推送
     */
    public static final String REQ_MESSAGE_TYPE_EVENT="event";
    /**
     * 事件类型：订阅
     */
    public static final String EVENT_TYPE_SUBSCRIBE="subscribe";
    /**
     * 事件类型：取消订阅
     */
    public static final String EVENT_TYPE_UNSUBSCRIBE="unsubscribe";
    /**
     * 事件类型：CLICK
     */
    public static final String EVENT_TYPE_CLICK="CLICK";
    /**
     * 解析微信发来的请求(XML)
     * @param request
     * @return
     * @throws Exception
     */
    public static Map<String,String> parseXML(HttpServletRequest request) throws Exception{
        //将解析结果存储在HashMap中
        Map<String,String> map=new HashMap<String,String>();

        InputStream inputStream=request.getInputStream();
        SAXReader reader=new SAXReader();
        Document document=reader.read(inputStream);
        Element root=document.getRootElement();
        List<Element> elementList=root.elements();
        for(Element e:elementList){
            map.put(e.getName(),e.getText());
        }
        inputStream.close();
        inputStream=null;
        return map;
    }

    /**
     *文本消息对象转换成xml
     * @param textMessage
     * @return
     */
    public static String textMessageToXml(TextMessage textMessage){
        xstream.alias("xml",textMessage.getClass());
        return xstream.toXML(textMessage);
    }

    /**
     * 扩展xstream，使其支持CDATA块
     */
    private static XStream xstream=new XStream(new XppDriver(){
        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out){
                boolean cdata=true;
                @Override
                public void startNode(String name, Class clazz){
                    super.startNode(name,clazz);
                }
                @Override
                protected void writeText(QuickWriter writer, String text){
                    if(cdata){
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    }else{
                        writer.write(text);
                    }
                }
            };
        }
    });
}
