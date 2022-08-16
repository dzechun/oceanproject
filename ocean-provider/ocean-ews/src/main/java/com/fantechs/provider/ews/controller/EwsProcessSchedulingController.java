package com.fantechs.provider.ews.controller;

import com.fantechs.common.base.general.dto.ews.EwsProcessSchedulingDto;
import com.fantechs.common.base.general.entity.ews.EwsProcessScheduling;
import com.fantechs.common.base.general.entity.ews.search.SearchEwsProcessScheduling;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.ValidGroup;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.ews.service.EwsProcessSchedulingService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.quartz.SchedulerException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/03/08.
 */
@RestController
@Api(tags = "程序排程")
@RequestMapping("/ewsProcessScheduling")
@Validated
public class EwsProcessSchedulingController {

    @Resource
    private EwsProcessSchedulingService ewsProcessSchedulingService;


    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EwsProcessScheduling ewsProcessScheduling) {
        return ControllerUtil.returnCRUD(ewsProcessSchedulingService.save(ewsProcessScheduling));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(ewsProcessSchedulingService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= ValidGroup.update.class) EwsProcessScheduling ewsProcessScheduling) {
        return ControllerUtil.returnCRUD(ewsProcessSchedulingService.update(ewsProcessScheduling));
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EwsProcessSchedulingDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEwsProcessScheduling searchEwsProcessScheduling) {
        Page<Object> page = PageHelper.startPage(searchEwsProcessScheduling.getStartPage(), searchEwsProcessScheduling.getPageSize());
        List<EwsProcessSchedulingDto> list = ewsProcessSchedulingService.findList(searchEwsProcessScheduling);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("开始排程")
    @GetMapping("/start")
    public ResponseEntity start(@ApiParam(value = "Id",required = true)@RequestParam Long Id) throws SchedulerException {
        return ControllerUtil.returnCRUD(ewsProcessSchedulingService.start(Id));
    }

    @ApiOperation("暂停排程")
    @GetMapping("/stop")
    public ResponseEntity stop(@ApiParam(value = "Id",required = true)@RequestParam Long Id) throws SchedulerException {
        return ControllerUtil.returnCRUD(ewsProcessSchedulingService.stop(Id));
    }

    @ApiOperation("查看任务")
    @GetMapping("/detail")
    public ResponseEntity detail(@ApiParam(value = "Id",required = true)@RequestParam Long Id){
        List<Map<String, Object>> allJob = ewsProcessSchedulingService.detail(Id);
        return ControllerUtil.returnDataSuccess(allJob, StringUtils.isEmpty(allJob)?0:allJob.size());
    }

    @ApiOperation("立即开始排程")
    @GetMapping("/immediately")
    public ResponseEntity immediately(@ApiParam(value = "Id",required = true)@RequestParam Long Id) throws SchedulerException {
        return ControllerUtil.returnCRUD(ewsProcessSchedulingService.immediately(Id));
    }



}
