package com.fantechs.common.base.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * RestTemplate 工具类
 *
 * @author: author
 * @create: 2019-06-05 14:06
 **/
@Slf4j
public class RestTemplateUtil {

    private static class SingletonRestTemplate {
        static final RestTemplate INSTANCE = new RestTemplate();
    }

    private RestTemplateUtil() {
    }

    public static RestTemplate getInstance() {
        return SingletonRestTemplate.INSTANCE;
    }

    private static HttpEntity<String> setHeaders(Map<String, Object> map) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Encoding", "UTF-8");
        headers.add("Content-Type", "application/json; charset=UTF-8");
        /*String token = CurrentUserInfoService.getToken();
        log.info("rest调用token："+token);
        if(StringUtils.isNotEmpty(token)){
            headers.add("token",token);
            headers.add("Access-Control-Expose-Headers","token");
        }*/
        String param = "";
        if (map != null) {
            Set<String> keySet = map.keySet();
            if (keySet.size() == 1 && keySet.contains("")) {
                param = JSONObject.toJSONString(map.get(""));
            } else {
                param = JSONObject.toJSONString(map);
            }
        }
        HttpEntity<String> httpEntity = new HttpEntity<>(param, headers);
        return httpEntity;
    }

    private static <T> T getBody(String url, Class<T> response) {
        log.info("【服务调用路径】："+url);
        ResponseEntity<T> entity = RestTemplateUtil.getInstance().exchange(url, HttpMethod.GET, setHeaders(null), response);
        return entity.getBody();
    }

    private static <T> T postBody(String url, Map<String, Object> map, Class<T> response) {
        log.info("【服务调用路径】："+url);
        ResponseEntity<T> entity = RestTemplateUtil.getInstance().exchange(url, HttpMethod.POST, setHeaders(map), response);
        return entity.getBody();
    }


    /**
     * 返回实体集合
     *
     * @param url
     * @param responseClass 返回类型
     * @param <T>
     * @return
     */
    public static <T> List<T> getForListEntity(String url, Class<T> responseClass) {
        String json = JSONObject.toJSONString(getForResponseDTO(url).getData());
        return BeanUtils.jsonToListObject(json, responseClass);
    }

    /**
     * 返回实体
     *
     * @param url
     * @param responseClass 返回类型
     * @param <T>
     * @return
     */
    public static <T> T getForEntity(String url, Class<T> responseClass) {
        String json = JSONObject.toJSONString(getForResponseDTO(url).getData());
        return BeanUtils.jsonToObject(json, responseClass);
    }

    /**
     * 返回String
     *
     * @param url 请求路径
     */
    public static String getForString(String url) {
        return JSONObject.toJSONString(getForResponseDTO(url).getData());
    }

    /**
     * 返回ResponseDTO
     * @param url 请求路径
     * @return
     */
    public static  ResponseDTO getForResponseDTO(String url){
        return getBody(url, ResponseDTO.class);
    }


    /**
     * 返回String
     *
     * @param url 请求路径
     * @param map 参数
     * @return
     */
    public static String postForString(String url, Map<String, Object> map) {
        return JSONObject.toJSONString(postForResponseDTO(url,map).getData());
    }

    /**
     * 返回ResponseDTO
     * @param url 请求路径
     * @return
     */
    public static  ResponseDTO postForResponseDTO(String url, Map<String, Object> map){
        return postBody(url, map, ResponseDTO.class);
    }

    /**
     * 返回实体
     *
     * @param url 请求路径
     * @param map 参数
     * @return
     */
    public static <T> T postForEntity(String url, Map<String, Object> map, Class<T> responseClass) {
        String json = JSONObject.toJSONString(postForResponseDTO(url,map).getData());
        return BeanUtils.jsonToObject(json, responseClass);
    }

    /**
     * 返回实体集合
     *
     * @param url 请求路径
     * @param map 参数
     * @return
     */
    public static <T> List<T> postForListEntity(String url, Map<String, Object> map, Class<T> responseClass) {
        String json = JSONObject.toJSONString(postForResponseDTO(url,map).getData());
        return BeanUtils.jsonToListObject(json, responseClass);
    }

}