package com.weixin.service;

import com.weixin.model.weixin.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CoreService {

    public String  processRequest(HttpServletRequest request);

    /**
     * 引导用户进入授权页面同意授权，获取code
     * @param response
     */
    public void wxLogin(HttpServletResponse response);

    /**
     * 网页授权回调地址
     * @param request
     * @param response
     * @return
     */
    public Message callBack(HttpServletRequest request, HttpServletResponse response);

    /**
     * 创建菜单
     * @return
     */
    public Message createMenu();
}
