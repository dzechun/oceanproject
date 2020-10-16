package com.fantechs.provider.quartz.config;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: bingo.ren
 * @Date: 2020/5/7 10:29
 * @Description:
 * @Version: 1.0
 */

public class QuartzDoInterface extends QuartzJobBean {
    @Resource
    private QuartzManager quartzManager;
    private static Logger log= LoggerFactory.getLogger(QuartzDoInterface.class);
    private Map<String,Object> map=new HashMap<>();
    private String uri;
    private String method;
    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        //解析出参数
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<String> exchange=null;
        log.info("任务调度接口："+uri+"，传递参数："+ map+"，访问方式："+(method.equals("1")?"GET":"POST"));
        try{
            if(method.equals("1")){
                String params="";
                if(map != null && map.size()>0){
                    Set<String> keySet = map.keySet();
                    for (String s : keySet) {
                        params+="&"+s+"="+map.get(s);
                    }
                    params="?"+params.substring(1);
                }
                exchange = restTemplate.getForEntity(uri+params, String.class);
            }else if (method.equals("2")){
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.add("Content-Type", "application/json;charset=utf-8");
                HttpEntity<String> requestEntity = new HttpEntity<String>((map != null && map.size()>0)? JSONObject.toJSONString(map):"", requestHeaders);
                exchange = restTemplate.exchange(uri, HttpMethod.POST, requestEntity, String.class);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            JobExecutionException e2= new JobExecutionException(e);
            e2.setUnscheduleAllTriggers(true);
        }
        log.info("执行结果："+exchange);
        /*log.info("执行结果："+exchange!=null?exchange.getBody():"执行错误");*/
    }
}
