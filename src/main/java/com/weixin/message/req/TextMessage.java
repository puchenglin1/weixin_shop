package com.weixin.message.req;

/**
 * 发送文本消息
 */
public class TextMessage extends BaseMessage {
    //消息内容
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
