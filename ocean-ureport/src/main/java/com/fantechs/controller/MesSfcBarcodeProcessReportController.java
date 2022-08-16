package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessRecordDto;
import com.fantechs.common.base.general.entity.ureport.*;
import com.fantechs.common.base.general.entity.ureport.search.SearchMesSfcBarcodeProcessReport;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
//        Page<Object> page = PageHelper.startPage(searchMesSfcBarcodeProcessReport.getStartPage(),searchMesSfcBarcodeProcessReport.getPageSize());
        List<MesSfcBarcodeProcessReport> list = mesSfcBarcodeProcessReportService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcessReport));
        int total = 0;
        if (StringUtils.isNotEmpty(list)) {
            total = list.get(0).getTotal();
        }
        return ControllerUtil.returnDataSuccess(list,total);
    }

    @ApiOperation("条码过站记录列表")
    @PostMapping("/findRecordList")
    public ResponseEntity<MesSfcBarcodeProcessReport> findRecordList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcBarcodeProcessReport searchMesSfcBarcodeProcessReport) {
        Page<Object> page = PageHelper.startPage(searchMesSfcBarcodeProcessReport.getStartPage(),searchMesSfcBarcodeProcessReport.getPageSize());
        MesSfcBarcodeProcessReport mesSfcBarcodeProcessReport = mesSfcBarcodeProcessReportService.findRecordList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcessReport));
        return ControllerUtil.returnDataSuccess(mesSfcBarcodeProcessReport,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")@RequestBody SearchMesSfcBarcodeProcessReport searchMesSfcBarcodeProcessReport) throws IOException {
        Map<String, Object> stringObjectMap = ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcessReport);
        stringObjectMap.put("export",1);
        List<MesSfcBarcodeProcessReport> list = mesSfcBarcodeProcessReportService.findList(stringObjectMap);
        MesSfcBarcodeProcessReport recordList = mesSfcBarcodeProcessReportService.findRecordList(stringObjectMap);
        try {
            Map<String,String> map = new LinkedHashMap<>();
            List<Class<?>> clzList = new ArrayList<>();

            map.put("过站详细信息",JsonUtils.objectToJson(list));
            clzList.add(MesSfcBarcodeProcessReport.class);

            if (StringUtils.isNotEmpty(recordList.getAssemblyList())) {
                map.put("装配记录",JsonUtils.objectToJson(recordList.getAssemblyList()));
                clzList.add(AssemblyRecordUreport.class);
            }
            if (StringUtils.isNotEmpty(recordList.getBarcodeList())) {
                map.put("条码过站记录",JsonUtils.objectToJson(recordList.getBarcodeList()));
                clzList.add(MesSfcBarcodeProcessRecordDto.class);
            }
            if (StringUtils.isNotEmpty(recordList.getBoxList())) {
                map.put("包箱记录",JsonUtils.objectToJson(recordList.getBoxList()));
                clzList.add(BoxRecordUreport.class);
            }
            if (StringUtils.isNotEmpty(recordList.getInspectionList())) {
                map.put("检验记录",JsonUtils.objectToJson(recordList.getInspectionList()));
                clzList.add(InspectionRecordUreport.class);
            }
            if (StringUtils.isNotEmpty(recordList.getPalletList())) {
                map.put("栈板记录",JsonUtils.objectToJson(recordList.getPalletList()));
                clzList.add(PalletRecordUreport.class);
            }
            if (StringUtils.isNotEmpty(recordList.getReworkList())) {
                map.put("返修记录",JsonUtils.objectToJson(recordList.getReworkList()));
                clzList.add(ReworkRecordUreport.class);
            }
            if (StringUtils.isNotEmpty(recordList.getEquipmentParameterList())) {
                map.put("设备参数记录",JsonUtils.objectToJson(recordList.getEquipmentParameterList()));
                clzList.add(EquipmentParameterRecordUreport.class);
            }
            EasyPoiUtils.exportExcelSheets(map,clzList,"工序过站.xls", response);
            //EasyPoiUtils.exportExcel(mesSfcBarcodeProcessReports, "工序过站", "工序过站信息", MesSfcBarcodeProcessReport.class, "工序过站.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

}
