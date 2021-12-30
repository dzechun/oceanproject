package com.fantechs.provider.ews.config;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.general.entity.ews.EwsWarningEventExecuteLog;
import com.fantechs.common.base.general.entity.ews.EwsWarningEventExecutePushLog;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.ews.mapper.EwsWarningEventExecuteLogMapper;
import com.fantechs.provider.ews.mapper.EwsWarningEventExecutePushLogMapper;
import com.fantechs.provider.ews.service.QuartzManagerService;
import lombok.SneakyThrows;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Auther: Mr.Lei
 * @Date: 2021/3/8
 */
//持久化
@PersistJobDataAfterExecution
//禁止并发执行(Quartz不要并发地执行同一个job定义（这里指一个job类的多个实例）)
@DisallowConcurrentExecution
public class QuartzDoInterface extends QuartzJobBean {
    @Resource
    private QuartzManagerService quartzManager;
    @Resource
    private EwsWarningEventExecuteLogMapper ewsWarningEventExecuteLogMapper;
    @Resource
    private EwsWarningEventExecutePushLogMapper ewsWarningEventExecutePushLogMapper;
    private static Logger log= LoggerFactory.getLogger(QuartzDoInterface.class);
    private Map<String,Object> map=new HashMap<>();
    private String url;
    private String method;
    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public void setUrl(String url) {
        this.url = url;
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
        log.info("任务调度接口："+url+"，传递参数："+ map+"，访问方式："+(method.equals("1")?"GET":"POST"));
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
                exchange = restTemplate.getForEntity(url+params, String.class);
            }else if (method.equals("2")){
                HttpHeaders requestHeaders = new HttpHeaders();
                requestHeaders.add("Content-Type", "application/json;charset=utf-8");
                HttpEntity<String> requestEntity = new HttpEntity<String>((map != null && map.size()>0)? JSONObject.toJSONString(map):"", requestHeaders);
                exchange = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            JobExecutionException e2= new JobExecutionException(e);
            e2.setUnscheduleAllTriggers(true);
        }
        log.info("执行结果："+exchange);
        //解析执行结果
        if(StringUtils.isNotEmpty(exchange) && StringUtils.isNotEmpty(exchange.getBody())){
            Map<String,Object> map = JSONObject.parseObject(exchange.getBody());
            if(map.containsKey("data") && StringUtils.isEmpty(map.get("data"))){
                Map<String,Object> data = JSONObject.parseObject(map.get("data").toString());
                if(StringUtils.isNotEmpty(data.get("schedule"),data.get("warningEventConfigId")) && Integer.parseInt(data.get("schedule").toString())==1){
                    EwsWarningEventExecuteLog ewsWarningEventExecuteLog = new EwsWarningEventExecuteLog();
                    ewsWarningEventExecuteLog.setWarningEventConfigId(Long.parseLong(data.get("warningEventConfigId").toString()));
                    ewsWarningEventExecuteLog.setExecuteParameter(data.containsKey("message")?(StringUtils.isNotEmpty(data.get("message"))?data.get("message").toString():null):null);
                    ewsWarningEventExecuteLog.setHandleResult((byte)1);
                    ewsWarningEventExecuteLog.setCreateTime(new Date());
                    ewsWarningEventExecuteLog.setModifiedTime(new Date());
                    ewsWarningEventExecuteLogMapper.insertUseGeneratedKeys(ewsWarningEventExecuteLog);
                    EwsWarningEventExecutePushLog ewsWarningEventExecutePushLog = new EwsWarningEventExecutePushLog();
                    ewsWarningEventExecutePushLog.setWarningEventExecuteLogId(ewsWarningEventExecuteLog.getWarningEventExecuteLogId());
                    ewsWarningEventExecutePushLog.setMessagePushResult((byte)1);
                    ewsWarningEventExecutePushLog.setPersonnelLevel((byte)1);
                    ewsWarningEventExecutePushLog.setCreateTime(new Date());
                    ewsWarningEventExecutePushLogMapper.insertSelective(ewsWarningEventExecutePushLog);
                }
            }
        }
        /*log.info("执行结果："+exchange!=null?exchange.getBody():"执行错误");*/
    }
}
