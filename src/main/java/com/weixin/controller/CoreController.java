package com.weixin.controller;

import com.weixin.service.CoreService;
import com.weixin.util.SignUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/core")
public class CoreController {

    @Resource
    private CoreService coreService;

    /**
     * 微信接入验证
     * @param request
     * @param response
     */
    @RequestMapping(value = "/excute",method = RequestMethod.GET)
    public void excute(HttpServletRequest request, HttpServletResponse response){
        //微信加密签名
        String signature=request.getParameter("signature");
        //时间戳
        String timestamp=request.getParameter("timestamp");
        //随机数
        String nonce=request.getParameter("nonce");
        //随机字符串
        String echostr=request.getParameter("echostr");
        try{
            PrintWriter out=response.getWriter();
            if(SignUtil.checkSignature(signature,timestamp,nonce)){
                out.print(echostr);
            }
            out.close();
            out=null;
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 处理微信服务器发来的消息
     * @param request
     * @param response
     * @param model
     * @throws IOException
     */
    @RequestMapping(value = "/excute",method = RequestMethod.POST)
    public void excute(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException{
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String respMessage=coreService.processRequest(request);
        PrintWriter out=response.getWriter();
        out.print(respMessage);
        out.close();
    }




}
