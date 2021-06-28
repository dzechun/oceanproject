package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsIpqcInspectionOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.entity.QmsIpqcDtaticElectricityModel;
import com.fantechs.entity.QmsIpqcSamplingModel;
import com.fantechs.service.QmsIpqcInspectionUreportService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/qmsIpqcInspection")
@Api(tags = "Ipqc检验单报表")
public class QmsIpqcInspectionUreportController {

    @Resource
    public QmsIpqcInspectionUreportService qmsIpqcInspectionUreportService;

    @ApiOperation("静电地线检测记录表")
    @PostMapping("/findDtaticElectricityList")
    public ResponseEntity<List<QmsIpqcDtaticElectricityModel>> findDtaticElectricityList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder) {
        Page<Object> page = PageHelper.startPage(searchQmsIpqcInspectionOrder.getStartPage(),searchQmsIpqcInspectionOrder.getPageSize());
        List<QmsIpqcDtaticElectricityModel> list = qmsIpqcInspectionUreportService.findDtaticElectricityList(searchQmsIpqcInspectionOrder);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/dtaticElectricityExport")
    @ApiOperation(value = "导出静电地线检测记录excel",notes = "导出静电地线检测记录excel",produces = "application/octet-stream")
    public void dtaticElectricityExport(HttpServletResponse response, @ApiParam(value = "查询对象") @RequestBody(required = false) SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder){
        List<QmsIpqcDtaticElectricityModel> list = qmsIpqcInspectionUreportService.findDtaticElectricityList(searchQmsIpqcInspectionOrder);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "静电地线检测记录", "ElectricityExport信息", QmsIpqcDtaticElectricityModel.class, "ElectricityExport.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @ApiOperation("抽检日报表")
    @PostMapping("/findSamplingList")
    public ResponseEntity<List<QmsIpqcSamplingModel>> findSamplingList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder) {
        Page<Object> page = PageHelper.startPage(searchQmsIpqcInspectionOrder.getStartPage(),searchQmsIpqcInspectionOrder.getPageSize());
        List<QmsIpqcSamplingModel> list = qmsIpqcInspectionUreportService.findSamplingList(searchQmsIpqcInspectionOrder);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/samplingExport")
    @ApiOperation(value = "导出抽检日报表记录excel",notes = "导出抽检日报表记录excel",produces = "application/octet-stream")
    public void samplingExport(HttpServletResponse response, @ApiParam(value = "查询对象")@RequestBody(required = false) SearchQmsIpqcInspectionOrder searchQmsIpqcInspectionOrder){
        List<QmsIpqcSamplingModel> list = qmsIpqcInspectionUreportService.findSamplingList(searchQmsIpqcInspectionOrder);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "静电地线检测记录", "ElectricityExport信息", QmsIpqcSamplingModel.class, "SamplingExport.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }






}
