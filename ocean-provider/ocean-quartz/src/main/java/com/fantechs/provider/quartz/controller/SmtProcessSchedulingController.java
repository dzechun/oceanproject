package com.fantechs.provider.quartz.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.bcm.SmtProcessSchedulingDto;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.entity.bcm.SmtProcessScheduling;
import com.fantechs.common.base.general.entity.bcm.search.SearchSmtProcessScheduling;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.ValidGroup;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.quartz.service.QuartzManagerService;
import com.fantechs.provider.quartz.service.SmtProcessSchedulingService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by mr.lei on 2021/03/08.
 */
@RestController
@Api(tags = "程序排程")
@RequestMapping("/smtProcessScheduling")
@Validated
public class SmtProcessSchedulingController {

    @Resource
    private SmtProcessSchedulingService smtProcessSchedulingService;
    @Resource
    QuartzManagerService quartzManagerService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtProcessScheduling smtProcessScheduling) {
        return ControllerUtil.returnCRUD(smtProcessSchedulingService.save(smtProcessScheduling));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtProcessSchedulingService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= ValidGroup.update.class) SmtProcessScheduling smtProcessScheduling) {
        return ControllerUtil.returnCRUD(smtProcessSchedulingService.update(smtProcessScheduling));
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtProcessSchedulingDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtProcessScheduling searchSmtProcessScheduling) {
        Page<Object> page = PageHelper.startPage(searchSmtProcessScheduling.getStartPage(),searchSmtProcessScheduling.getPageSize());
        List<SmtProcessSchedulingDto> list = smtProcessSchedulingService.findList(searchSmtProcessScheduling);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<SmtProcessScheduling>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtProcessScheduling searchSmtProcessScheduling) {
//        Page<Object> page = PageHelper.startPage(searchSmtProcessScheduling.getStartPage(),searchSmtProcessScheduling.getPageSize());
//        List<SmtProcessScheduling> list = smtProcessSchedulingService.findHtList(ControllerUtil.dynamicConditionByEntity(searchSmtProcessScheduling));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

//    @PostMapping(value = "/export")
//    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
//    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
//    @RequestBody(required = false) SearchSmtProcessScheduling searchSmtProcessScheduling){
//    List<SmtProcessSchedulingDto> list = smtProcessSchedulingService.findList(searchSmtProcessScheduling);
//    try {
//        // 导出操作
//        EasyPoiUtils.exportExcel(list, "导出信息", "SmtProcessScheduling信息", SmtProcessSchedulingDto.class, "SmtProcessScheduling.xls", response);
//        } catch (Exception e) {
//        throw new BizErrorException(e);
//        }
//    }

    @ApiOperation("开始排程")
    @GetMapping("/start")
    public ResponseEntity start(@ApiParam(value = "Id",required = true)@RequestParam Long Id) throws SchedulerException {
        return ControllerUtil.returnCRUD(smtProcessSchedulingService.start(Id));
    }

    @ApiOperation("暂停排程")
    @GetMapping("/stop")
    public ResponseEntity stop(@ApiParam(value = "Id",required = true)@RequestParam Long Id) throws SchedulerException {
        return ControllerUtil.returnCRUD(smtProcessSchedulingService.stop(Id));
    }

    @ApiOperation("查看任务")
    @GetMapping("/detail")
    public ResponseEntity detail(@ApiParam(value = "Id",required = true)@RequestParam Long Id){
        List<Map<String, Object>> allJob = smtProcessSchedulingService.detail(Id);
        return ControllerUtil.returnDataSuccess(allJob, StringUtils.isEmpty(allJob)?0:allJob.size());
    }
}
