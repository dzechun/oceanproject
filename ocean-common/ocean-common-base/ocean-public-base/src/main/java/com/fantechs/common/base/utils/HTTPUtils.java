package com.fantechs.common.base.utils;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @Auther: bingo.ren
 * @Date: 2020/4/28 9:04
 * @Description: HTTP相关操作
 * @Version: 1.0
 */
public class HTTPUtils {

    /**
     * 获取下载输出流
     * @param response
     * @param fileName
     * @return
     * @throws IOException
     */
    public static ServletOutputStream getDownload(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename="+new String(fileName.getBytes("utf-8"),
                "ISO-8859-1"));
        response.flushBuffer();
        return response.getOutputStream();
    }


    /**
     * SSL post
     * @param url
     * @param headerMap
     * @param bodyMap
     * @return
     */
    public static String postMap(String url, Map<String,String> headerMap, Map<String, Object> bodyMap) {
        String result = null;
        CloseableHttpClient httpClient = SkipHttpsUtil.wrapClient();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> content = new ArrayList<NameValuePair>();
        for(String key:bodyMap.keySet()){//keySet获取map集合key的集合  然后在遍历key即可
            String value = bodyMap.get(key).toString();//
            content.add(new BasicNameValuePair(key,value));
        }

        CloseableHttpResponse response = null;
        try {

            for(String key:headerMap.keySet()){//keySet获取map集合key的集合  然后在遍历key即可
                String value = headerMap.get(key).toString();//
                post.addHeader(key,value);
            }

            if(content.size() > 0){
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(content,"UTF-8");
                post.setEntity(entity);
            }
            response = httpClient.execute(post);            //发送请求并接收返回数据
            if(response != null && response.getStatusLine().getStatusCode() == 200)
            {
                HttpEntity entity = response.getEntity();       //获取response的body部分
                result = EntityUtils.toString(entity);          //读取reponse的body部分并转化成字符串
            }
            return result;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
                if(response != null)
                {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

}
