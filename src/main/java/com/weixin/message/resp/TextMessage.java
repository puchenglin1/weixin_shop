package com.weixin.message.resp;

/**
 * 回复文本消息
 */
public class TextMessage extends BaseMessage {
    //回复消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}