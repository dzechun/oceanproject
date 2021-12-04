package com.fantechs.provider.kreport.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.kreport.LogisticsTrackReport;
import com.fantechs.common.base.general.entity.kreport.search.SearchLogisticsTrackReport;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.provider.kreport.service.LogisticsTrackReportService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Api(tags = "物流轨迹报表")
@RequestMapping("/logisticsTrackReport")
@Validated
public class LogisticsTrackReportController {

    @Resource
    private LogisticsTrackReportService logisticsTrackReportService;

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<LogisticsTrackReport>> findList(@ApiParam(value = "查询对象")@RequestBody SearchLogisticsTrackReport searchLogisticsTrackReport) {
        Page<Object> page = PageHelper.startPage(searchLogisticsTrackReport.getStartPage(),searchLogisticsTrackReport.getPageSize());
        List<LogisticsTrackReport> list = logisticsTrackReportService.findList(ControllerUtil.dynamicConditionByEntity(searchLogisticsTrackReport));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void export(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchLogisticsTrackReport searchLogisticsTrackReport){
        Page<Object> page = PageHelper.startPage(searchLogisticsTrackReport.getStartPage(),searchLogisticsTrackReport.getPageSize());
        List<LogisticsTrackReport> list = logisticsTrackReportService.findList(ControllerUtil.dynamicConditionByEntity(searchLogisticsTrackReport));
        String title = "";
        if (searchLogisticsTrackReport.getType() == 1) {
            title = "收货物流轨迹信息";
        }else {
            title = "发货物流轨迹信息";
        }
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, title, title, LogisticsTrackReport.class, title+".xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

}
