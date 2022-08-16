package com.fantechs.provider.ews.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.provider.ews.config.QuartzDoInterface;
import com.fantechs.provider.ews.service.QuartzManagerService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Auther: Mr.Lei
 * @Date: 2021/3/8
 */
@Slf4j
@Service
public class QuartzManagerServiceImpl implements QuartzManagerService {

    @Resource
    private Scheduler scheduler;

    /**
     * 创建任务，带参数
     * @param clazz
     * @param jobName
     * @param jobGroupName
     * @param cronExpression
     * @param params
     * @throws Exception
     */
    @Override
    public void addJob(Class clazz, String jobName, String jobGroupName, String cronExpression,Map<String,Object> params) throws Exception {
//        Class<? extends Job> aClass = (Class<? extends Job>) Class.forName(clazz).newInstance().getClass();
        // 任务名，任务组，任务执行类
        JobDetail job = JobBuilder.newJob(clazz).withIdentity(jobName, jobGroupName).build();
        if(params != null){
            job.getJobDataMap().putAll(params);
        }
        // 触发器
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        // 触发器名,触发器组
        triggerBuilder.withIdentity(jobName, jobGroupName);
        triggerBuilder.startNow();
        // 触发器时间设定
        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
        // 创建Trigger对象
        CronTrigger trigger = (CronTrigger) triggerBuilder.build();
        // 调度容器设置JobDetail和Trigger
        scheduler.scheduleJob(job, trigger);
        // 启动
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
        log.info("添加任务："+jobGroupName+"："+jobName);
    }

    /**
     * 创建任务，不带参数
     * @param clazz
     * @param jobName
     * @param jobGroupName
     * @param cronExpression
     */
    @Override
    public void addJob(Class clazz, String jobName, String jobGroupName, String cronExpression) throws Exception {
        addJob(clazz, jobName, jobGroupName, cronExpression, null);
    }


    /**
     * 暂停job
     * @param jobName
     * @param jobGroupName
     * @throws SchedulerException
     */
    @Override
    public void stopJob(String jobName, String jobGroupName) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroupName));
        log.info("任务已经暂停");
    }

    /**
     * 恢复job
     * @param jobName
     * @param jobGroupName
     * @throws SchedulerException
     */
    @Override
    public void resumeJob(String jobName, String jobGroupName) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroupName));
        log.info("任务已经恢复");
    }


    /**
     * 更新job，只更新规则
     * @param jobName
     * @param jobGroupName
     * @param cronExpression
     * @throws Exception
     */
    @Override
    public void updateJob(String jobName, String jobGroupName, String cronExpression) throws Exception {
        updateJob(jobName, jobGroupName, cronExpression,null);
    }


    /**
     * 更新job，更新规则和参数
     * @param jobName
     * @param jobGroupName
     * @param cronExpression
     * @param argMap
     * @throws Exception
     */
    @Override
    public void updateJob(String jobName, String jobGroupName, String cronExpression, Map<String, Object> argMap) throws Exception {
    /*    TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        // 任务名，任务组，任务执行类

        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if(cronExpression == null){
            cronExpression=trigger.getCronExpression();
        }

        // 按新的cronExpression表达式重新构建trigger
        // 表达式调度构建器
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).startNow().build();
       // JobDetail jobDetail = scheduler.getJobDetail(trigger.getJobKey());
        if(argMap != null){
            trigger.getJobDataMap().putAll(argMap);
        }
        scheduler.rescheduleJob(triggerKey, trigger);

        log.info("任务已被重新执行");*/

        //删除旧job
        this.deleteJob(jobName,jobGroupName);

        JobDetail job = JobBuilder.newJob(QuartzDoInterface.class).withIdentity(jobName, jobGroupName).build();
        if(argMap != null){
            job.getJobDataMap().putAll(argMap);
        }
        // 触发器
        TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
        // 触发器名,触发器组
        triggerBuilder.withIdentity(jobName, jobGroupName);
        triggerBuilder.startNow();
        // 触发器时间设定
        triggerBuilder.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression));
        // 创建Trigger对象
        CronTrigger trigger = (CronTrigger) triggerBuilder.build();
        // 调度容器设置JobDetail和Trigger
        scheduler.scheduleJob(job, trigger);
        // 启动
        if (!scheduler.isShutdown()) {
            scheduler.start();
        }
        log.info("任务已被重新执行");
    }

    /**
     * 更新job，只更新参数
     * @param jobName
     * @param jobGroupName
     * @param argMap
     * @throws Exception
     */
    @Override
    public void updateJob(String jobName, String jobGroupName, Map<String, Object> argMap) throws Exception {
        updateJob(jobName, jobGroupName,null,argMap);
        log.info("已更新任务参数："+ JSONObject.toJSONString(argMap));
    }


    /**
     * 立刻执行Job
     * @param jobName
     * @param jobGroupName
     * @throws Exception
     */
    @Override
    public void immediately(String jobName, String jobGroupName) throws Exception {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroupName);
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        scheduler.triggerJob(trigger.getJobKey());
        log.info("已立刻执行任务："+jobGroupName+"："+jobName);
    }


    /**
     * 删除job
     * @param jobName
     * @param jobGroupName
     * @throws Exception
     */
    @Override
    public void deleteJob(String jobName, String jobGroupName) throws Exception {
        scheduler.pauseTrigger(TriggerKey.triggerKey(jobName, jobGroupName));
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroupName));
        boolean b = scheduler.deleteJob(JobKey.jobKey(jobName, jobGroupName));
        log.info("删除任务"+b+"："+jobGroupName+"："+jobName);
    }


    /**
     * 启动所有job
     */
    @Override
    public void startAllJobs() {
        try {
            scheduler.start();
            log.info("已启动所有任务");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭所有job
     */
    @Override
    public void shutdownAllJobs() {
        try {
            if (!scheduler.isShutdown()) {
                scheduler.shutdown();
                log.info("已关闭所有任务");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    /**
     * 获取所有任务列表
     * @return
     * @throws SchedulerException
     */
    @Override
    public List<Map<String, Object>> getAllJob() throws SchedulerException {
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<Map<String, Object>> jobList = new ArrayList<Map<String, Object>>();
        log.info("已获取到任务数："+jobKeys.size());
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                Map<String, Object> job = new HashMap<String, Object>();
                job.put("jobName", jobKey.getName());
                job.put("jobGroupName", jobKey.getGroup());
                job.put("trigger", trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                job.put("jobStatus", triggerState.name());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.put("cronExpression", cronExpression);
                }
                JobDetail jobDetail = scheduler.getJobDetail(jobKey);
                job.put("url",jobDetail.getJobDataMap().get("url"));
                job.put("method",jobDetail.getJobDataMap().get("method")=="1"?"GET":"POST");
                job.put("map",jobDetail.getJobDataMap().get("map")!=null?jobDetail.getJobDataMap().get("map"):"");
                jobList.add(job);
            }
        }
        return jobList;
    }

    @Override
    public List<Map<String, Object>> getJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        List<Map<String, Object>> jobList = new ArrayList<Map<String, Object>>();
        List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
        for (Trigger trigger : triggers) {
            Map<String, Object> job = new HashMap<String, Object>();
            job.put("jobName", jobKey.getName());
            job.put("jobGroupName", jobKey.getGroup());
            job.put("trigger", trigger.getKey());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            job.put("jobStatus", STATUS_ENUM.valueOf(triggerState.name()).getStatus());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.put("cronExpression", cronExpression);
            }
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            job.put("url",jobDetail.getJobDataMap().get("url"));
            job.put("method",jobDetail.getJobDataMap().get("method").equals("1")?"GET":"POST");
            job.put("map",jobDetail.getJobDataMap().get("map")!=null?jobDetail.getJobDataMap().get("map"):"");
            jobList.add(job);
        }

        return jobList;
    }
    public enum  STATUS_ENUM{
        BLOCKED("BLOCKED","阻塞"),
        COMPLETE("COMPLETE","完成"),
        ERROR("ERROR","错误"),
        NONE("NONE","不存在"),
        NORMAL("NORMAL","正常"),
        PAUSED("PAUSED","暂停");

        private String name;
        private String status;
        STATUS_ENUM(String name,String status){
            this.name = name;
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public String getStatus() {
            return status;
        }
    }
}


