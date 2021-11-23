package com.fantechs.provider.kreport.controller;

import com.fantechs.common.base.general.entity.kreport.LogisticsTrackReport;
import com.fantechs.common.base.general.entity.kreport.search.SearchLogisticsTrackReport;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
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

}
