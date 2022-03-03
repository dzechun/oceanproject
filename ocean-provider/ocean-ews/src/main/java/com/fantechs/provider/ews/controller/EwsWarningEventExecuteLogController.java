package com.fantechs.provider.ews.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.ews.EwsWarningEventExecuteLogDto;
import com.fantechs.common.base.general.dto.ews.LogUreportDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningEventExecuteLog;
import com.fantechs.common.base.general.entity.ews.search.SearchEwsWarningEventExecuteLog;
import com.fantechs.common.base.general.entity.ews.search.SearchLogUreport;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.ews.service.EwsWarningEventExecuteLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by mr.lei on 2021/12/28.
 */
@RestController
@Api(tags = "预警推送日志")
@RequestMapping("/ewsWarningEventExecuteLog")
@Validated
public class EwsWarningEventExecuteLogController {

    @Resource
    private EwsWarningEventExecuteLogService ewsWarningEventExecuteLogService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EwsWarningEventExecuteLog ewsWarningEventExecuteLog) {
        return ControllerUtil.returnCRUD(ewsWarningEventExecuteLogService.save(ewsWarningEventExecuteLog));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(ewsWarningEventExecuteLogService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EwsWarningEventExecuteLog.update.class) EwsWarningEventExecuteLog ewsWarningEventExecuteLog) {
        return ControllerUtil.returnCRUD(ewsWarningEventExecuteLogService.update(ewsWarningEventExecuteLog));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EwsWarningEventExecuteLog> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EwsWarningEventExecuteLog  ewsWarningEventExecuteLog = ewsWarningEventExecuteLogService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(ewsWarningEventExecuteLog,StringUtils.isEmpty(ewsWarningEventExecuteLog)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EwsWarningEventExecuteLogDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEwsWarningEventExecuteLog searchEwsWarningEventExecuteLog) {
        Page<Object> page = PageHelper.startPage(searchEwsWarningEventExecuteLog.getStartPage(),searchEwsWarningEventExecuteLog.getPageSize());
        List<EwsWarningEventExecuteLogDto> list = ewsWarningEventExecuteLogService.findList(ControllerUtil.dynamicConditionByEntity(searchEwsWarningEventExecuteLog));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<EwsWarningEventExecuteLogDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchEwsWarningEventExecuteLog searchEwsWarningEventExecuteLog) {
        List<EwsWarningEventExecuteLogDto> list = ewsWarningEventExecuteLogService.findList(ControllerUtil.dynamicConditionByEntity(searchEwsWarningEventExecuteLog));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("推送")
    @GetMapping("/push")
    public ResponseEntity<Integer> push() {
        return ControllerUtil.returnCRUD(ewsWarningEventExecuteLogService.push());
    }

    @ApiOperation("预警报表")
    @PostMapping("/findUreport")
    public ResponseEntity<List<LogUreportDto>> findUreport(@RequestBody SearchLogUreport searchLogUreport){
        Page<Object> page = PageHelper.startPage(searchLogUreport.getStartPage(),searchLogUreport.getPageSize());
        List<LogUreportDto> list = ewsWarningEventExecuteLogService.findLogUreport(ControllerUtil.dynamicConditionByEntity(searchLogUreport));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("确认")
    @GetMapping("/affirm")
    public ResponseEntity affirm(@RequestParam Long warningEventExecutePushLogId){
        return ControllerUtil.returnCRUD(ewsWarningEventExecuteLogService.affirm(warningEventExecutePushLogId));
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象") @RequestBody(required = false) SearchLogUreport searchLogUreport){
        List<LogUreportDto> list = ewsWarningEventExecuteLogService.findLogUreport(ControllerUtil.dynamicConditionByEntity(searchLogUreport));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "预警推送报表", "预警推送报表", LogUreportDto.class, "预警推送报表.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
