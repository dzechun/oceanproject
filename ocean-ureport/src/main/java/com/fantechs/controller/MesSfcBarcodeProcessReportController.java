package com.fantechs.controller;

import com.fantechs.common.base.general.entity.ureport.MesSfcBarcodeProcessReport;
import com.fantechs.common.base.general.entity.ureport.search.SearchMesSfcBarcodeProcessReport;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.service.MesSfcBarcodeProcessReportService;
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
@Api(tags = "工序过站记录")
@RequestMapping("/mesSfcBarcodeProcessReport")
@Validated
public class MesSfcBarcodeProcessReportController {

    @Resource
    private MesSfcBarcodeProcessReportService mesSfcBarcodeProcessReportService;

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesSfcBarcodeProcessReport>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcBarcodeProcessReport searchMesSfcBarcodeProcessReport) {
        Page<Object> page = PageHelper.startPage(searchMesSfcBarcodeProcessReport.getStartPage(),searchMesSfcBarcodeProcessReport.getPageSize());
        List<MesSfcBarcodeProcessReport> list = mesSfcBarcodeProcessReportService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcessReport));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("条码过站记录列表")
    @PostMapping("/findRecordList")
    public ResponseEntity<MesSfcBarcodeProcessReport> findRecordList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcBarcodeProcessReport searchMesSfcBarcodeProcessReport) {
        Page<Object> page = PageHelper.startPage(searchMesSfcBarcodeProcessReport.getStartPage(),searchMesSfcBarcodeProcessReport.getPageSize());
        MesSfcBarcodeProcessReport mesSfcBarcodeProcessReport = mesSfcBarcodeProcessReportService.findRecordList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcessReport));
        return ControllerUtil.returnDataSuccess(mesSfcBarcodeProcessReport,(int)page.getTotal());
    }

}
