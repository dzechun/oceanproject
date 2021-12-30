package com.fantechs.provider.ews.utils;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/12/28
 */
@Component
public class DingService {
    //请求地址以及access_token
    public String Webhook = "https://oapi.dingtalk.com/robot/send?access_token=";
    //密钥
    public String secret = "YOUR SECRET";

    /*
     ** 生成时间戳和验证信息
     */

    public String encode() throws Exception {
        //获取时间戳
        Long timestamp = System.currentTimeMillis();
        //把时间戳和密钥拼接成字符串，中间加入一个换行符
        String stringToSign = timestamp + "\n" + this.secret;
        //声明一个Mac对象，用来操作字符串
        Mac mac = Mac.getInstance("HmacSHA256");
        //初始化，设置Mac对象操作的字符串是UTF-8类型，加密方式是SHA256
        mac.init(new SecretKeySpec(this.secret.getBytes("UTF-8"), "HmacSHA256"));
        //把字符串转化成字节形式
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        //新建一个Base64编码对象
        Base64.Encoder encoder = Base64.getEncoder();
        //把上面的字符串进行Base64加密后再进行URL编码
        String sign = URLEncoder.encode(new String(encoder.encodeToString(signData)),"UTF-8");
        System.out.println(timestamp);
        System.out.println(sign);
        String result = "&timestamp=" + timestamp + "&sign=" + sign;
        return result;
    }

    /**
     *
     * @param isAtAll 是否通知所有人
     * @param atMobiles 通知人手机号码列表
     * @param message 要发送的信息
     * @return
     */
    public String buildReqStr(Boolean isAtAll, List<String> atMobiles, String message){
        //消息内容
        Map<String, String> contentMap = new HashMap<>();
        contentMap.put("content", message);

        //通知人
        Map<String, Object> atMap = new HashMap<>();
        //1.是否通知所有人
        atMap.put("isAtAll", isAtAll);
        //2.通知具体人的手机号码列表
        atMap.put("atMobiles", atMobiles);

        Map<String, Object> reqMap = new HashMap<>();
        reqMap.put("msgtype", "text");
        reqMap.put("text", contentMap);
        reqMap.put("at", atMap);

        return JSON.toJSONString(reqMap);
    }

    /**
     *
     * @param req 消息
     * @return
     */
    public String dingRequest(String req){
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = this.Webhook;
        try {
            url = this.Webhook + this.encode();
        } catch (Exception e) {
            e.printStackTrace();
        }
        HttpPost httpPost = new HttpPost(url);
        //设置http的请求头，发送json字符串，编码UTF-8
        httpPost.setHeader("Content-Type", "application/json;charset=utf8");
        StringEntity entity = new StringEntity(req, "UTF-8");
        //设置http请求的内容
        httpPost.setEntity(entity);
        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            HttpEntity responseEntity = response.getEntity();
            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + EntityUtils.toString(responseEntity));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return response.getEntity().toString();
    }
}
