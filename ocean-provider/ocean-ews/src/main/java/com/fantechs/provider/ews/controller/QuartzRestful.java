package com.fantechs.provider.ews.controller;


import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.ews.config.QuartzDoInterface;
import com.fantechs.provider.ews.entity.QuartzSearch;
import com.fantechs.provider.ews.service.QuartzManagerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: Mr.Lei
 * @Date: 2021/3/8
 */
@RestController
@Api(tags = "任务调度对外开发接口",basePath = "/restful/quartz/dointerface")
@RequestMapping("/restful/quartz/dointerface")
public class QuartzRestful {

    @Resource
    private QuartzManagerService quartzManager;

    @ApiOperation(value = "获取任务列表")
    @GetMapping("findList")
    private ResponseEntity findList() throws SchedulerException{
            List<Map<String, Object>> allJob = quartzManager.getAllJob();
            return ControllerUtil.returnDataSuccess(allJob, StringUtils.isEmpty(allJob)?0:allJob.size());

    }
    @ApiOperation(value = "添加任务调度")
    @PostMapping("/add")
    private ResponseEntity start(@RequestBody QuartzSearch quartzSearch) throws Exception {

            quartzManager.addJob(QuartzDoInterface.class, quartzSearch.getName(), "doInterfaceGroup",
                    quartzSearch.getCron(),ControllerUtil.dynamicConditionByEntity(quartzSearch));
        return ControllerUtil.returnSuccess();
    }
    @ApiOperation(value = "暂定任务")
    @GetMapping("/stop")
    private ResponseEntity stop(@ApiParam("调度任务名称")@RequestParam String name) throws SchedulerException{

            quartzManager.stopJob(name,"doInterfaceGroup");

        return ControllerUtil.returnSuccess();
    }
    @ApiOperation(value = "恢复任务")
    @GetMapping("/resume")
    private ResponseEntity resume(@ApiParam("调度任务名称")@RequestParam String name) throws SchedulerException {
            quartzManager.resumeJob(name,"doInterfaceGroup");
        return ControllerUtil.returnSuccess();
    }
    @ApiOperation(value = "销毁任务")
    @GetMapping("/delete")
    private ResponseEntity delete(@ApiParam("调度任务名称")@RequestParam String name) throws Exception {

        quartzManager.deleteJob(name, "doInterfaceGroup");

        return ControllerUtil.returnSuccess();
    }
    @ApiOperation(value = "更新任务")
    @PostMapping("/update")
    private ResponseEntity update(
            @ApiParam("更新参数")@RequestBody QuartzSearch quartzSearch) throws Exception {

            quartzManager.updateJob(
                    quartzSearch.getName(),
                    "doInterfaceGroup",
                    StringUtils.isEmpty(quartzSearch.getCron())?null:quartzSearch.getCron(),
                    StringUtils.isEmpty(quartzSearch.getMap())?null:quartzSearch.getMap());

        return ControllerUtil.returnSuccess();
    }

    @ApiOperation(value = "立刻执行任务")
    @GetMapping("/immediately")
    private ResponseEntity immediately(@ApiParam("调度任务名称")@RequestParam String name) throws Exception {

        quartzManager.immediately(name, "doInterfaceGroup");
        return ControllerUtil.returnSuccess();
    }

}
