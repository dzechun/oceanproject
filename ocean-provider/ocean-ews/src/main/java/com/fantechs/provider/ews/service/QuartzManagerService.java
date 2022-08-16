package com.fantechs.provider.ews.service;

import org.quartz.SchedulerException;

import java.util.List;
import java.util.Map;

public interface QuartzManagerService {
    List<Map<String, Object>> getAllJob() throws SchedulerException;
    void addJob(Class clazz, String jobName, String jobGroupName, String cronExpression, Map<String, Object> params) throws Exception;
    void addJob(Class clazz, String jobName, String jobGroupName, String cronExpression) throws Exception;
    void stopJob(String jobName, String jobGroupName) throws SchedulerException;
    void resumeJob(String jobName, String jobGroupName) throws SchedulerException;
    void updateJob(String jobName, String jobGroupName, String cronExpression) throws Exception;
    void updateJob(String jobName, String jobGroupName, String cronExpression, Map<String, Object> argMap) throws Exception;
    void updateJob(String jobName, String jobGroupName, Map<String, Object> argMap) throws Exception;
    void immediately(String jobName, String jobGroupName) throws Exception;
    void deleteJob(String jobName, String jobGroupName) throws Exception;
    void startAllJobs();
    void shutdownAllJobs();
    List<Map<String, Object>> getJob(String name, String group) throws SchedulerException;
}
