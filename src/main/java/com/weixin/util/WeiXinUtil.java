package com.weixin.util;

import com.weixin.model.weixin.Menu;
import com.weixin.model.weixin.Message;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component
public class WeiXinUtil {

    private final static Logger log=LoggerFactory.getLogger(WeiXinUtil.class);
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取token
     * @param appid
     * @param appsecret
     * @return
     */
    public String getAccessToken(String appid,String appsecret){

        String token= (String)redisUtil.get(appid);
        if(token!=null&&!"".equals(token)){
            return token;
        }else{
            String requestUrl=WeiXinParams.access_token_url.replace("APPID",WeiXinParams.appid).replace("APPSECRET",WeiXinParams.appsecret);
            JSONObject jsonObject=httpRequest(requestUrl,"GET",null);
            if(jsonObject!=null){
                token=jsonObject.getString("access_token");
                redisUtil.set(appid,token,jsonObject.getInt("expires_in")-200);
            }
        }
        return token;
    }

    /**
     * 创建菜单
     * @param menu
     * @return
     */
    public Message createMenu(Menu menu){
        Message message=new Message();
        String token=getAccessToken(WeiXinParams.appid,WeiXinParams.appsecret);
        String jsonMenu=JSONObject.fromObject(menu).toString();
        String requestUrl=WeiXinParams.create_menu_url.replace("ACCESS_TOKEN",token);
        JSONObject jsonObject=httpRequest(requestUrl,"GET",jsonMenu);
        if(jsonObject!=null){
            message.setSuccess(jsonObject.getInt("errcode")+"");
            message.setMessage(jsonObject.getString("errmsg"));
            if(jsonObject.getInt("errcode")!=0){
                jsonObject.getInt("errcode");
                log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }
        return message;
    }


    public JSONObject httpRequest(String requestUrl, String requestMethod, String outputStr) {
        JSONObject jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建 SSLContext 对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new X509TrustManager (){  //证书信任管理器（用于 https 请求）
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
            };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述 SSLContext 对象中得到 SSLSocketFactory 对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);
            if ("GET".equalsIgnoreCase(requestMethod)){
                httpUrlConn.connect();
            }
            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = JSONObject.fromObject(buffer.toString());
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
        return jsonObject;
    }


}
