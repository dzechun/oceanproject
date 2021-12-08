package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.dto.MonthInDto;
import com.fantechs.dto.MonthOutDto;
import com.fantechs.dto.ShipmentDetDto;
import com.fantechs.entity.search.SearchShipmentDet;
import com.fantechs.entity.search.SearchMonthInOut;
import com.fantechs.entity.search.SearchMonthInOutBarCode;
import com.fantechs.service.MonthInOutService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/11/22
 */
@RestController
@Api(tags = "月出月入报表")
@RequestMapping("/monthInOut")
@Validated
public class MonthInOutController {

    @Resource
    private MonthInOutService monthInOutService;

    @PostMapping("/findInList")
    @ApiOperation("月入报表")
    public ResponseEntity<List<MonthInDto>> findInList(@RequestBody(required = false) SearchMonthInOut searchMonthInOut){
        Page<Object> page = PageHelper.startPage(searchMonthInOut.getStartPage(), searchMonthInOut.getPageSize());
        return ControllerUtil.returnDataSuccess(monthInOutService.findInList(ControllerUtil.dynamicConditionByEntity(searchMonthInOut)),(int)page.getTotal());
    }

    @PostMapping("/findInListBarCode")
    @ApiOperation("月入条码明细")
    public ResponseEntity<List<String>> findInListBarCode(@RequestBody(required = false) SearchMonthInOutBarCode searchMonthInOutBarCode){
        Page<Object> page = PageHelper.startPage(searchMonthInOutBarCode.getStartPage(), searchMonthInOutBarCode.getPageSize());
        return ControllerUtil.returnDataSuccess(monthInOutService.findInListBarCode(ControllerUtil.dynamicConditionByEntity(searchMonthInOutBarCode)),(int)page.getTotal());
    }

    @PostMapping("/findOutList")
    @ApiOperation("月出报表")
    public ResponseEntity<List<MonthOutDto>> findOutList(@RequestBody(required = false) SearchMonthInOut searchMonthInOut){
        Page<Object> page = PageHelper.startPage(searchMonthInOut.getStartPage(), searchMonthInOut.getPageSize());
        return ControllerUtil.returnDataSuccess(monthInOutService.findOutList(ControllerUtil.dynamicConditionByEntity(searchMonthInOut)),(int)page.getTotal());
    }

    @PostMapping("/findOutListBarCode")
    @ApiOperation("月入条码明细")
    public ResponseEntity<List<String>> findOutListBarCode(@RequestBody(required = false) SearchMonthInOutBarCode searchMonthInOutBarCode){
        Page<Object> page = PageHelper.startPage(searchMonthInOutBarCode.getStartPage(), searchMonthInOutBarCode.getPageSize());
        return ControllerUtil.returnDataSuccess(monthInOutService.findOutListBarCode(ControllerUtil.dynamicConditionByEntity(searchMonthInOutBarCode)),(int)page.getTotal());
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void export(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMonthInOut searchMonthInOut, @ApiParam(value = "type=1 月入导出、type=2 月出导出")@RequestParam Integer type){
        if(type==1){
            List<MonthInDto> list = monthInOutService.findInList(ControllerUtil.dynamicConditionByEntity(searchMonthInOut));
            try {
                // 导出操作
                EasyPoiUtils.exportExcel(list, "月入报表", "月入报表", MonthInDto.class, "月入报表.xls", response);
            } catch (Exception e) {
                throw new BizErrorException(e);
            }
        }else if(type==2) {
            List<MonthOutDto> list = monthInOutService.findOutList(ControllerUtil.dynamicConditionByEntity(searchMonthInOut));
            try {
                // 导出操作
                EasyPoiUtils.exportExcel(list, "月出报表", "月出报表", MonthOutDto.class, "月出报表.xls", response);
            } catch (Exception e) {
                throw new BizErrorException(e);
            }
        }
    }

    @PostMapping("/findShipmentDet")
    @ApiOperation("发货明细报表")
    public ResponseEntity<List<ShipmentDetDto>> findShipmentDet(@RequestBody(required = false) SearchShipmentDet dto){
        Page<Object> page = PageHelper.startPage(dto.getStartPage(), dto.getPageSize());
        List<ShipmentDetDto> list = monthInOutService.findShipmentDet(ControllerUtil.dynamicConditionByEntity(dto));
        return ControllerUtil.returnDataSuccess(list, (int)page.getTotal());
    }

    @PostMapping("/exportShipmentDet")
    @ApiOperation(value = "导出发货明细excel",notes = "导出发货明细excel",produces = "application/octet-stream")
    public void exportShipmentDet(HttpServletResponse response, @ApiParam(value = "查询对象") @RequestBody(required = false) SearchShipmentDet dto){
        List<ShipmentDetDto> list = monthInOutService.findShipmentDet(ControllerUtil.dynamicConditionByEntity(dto));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "发货明细报表", "发货明细报表", ShipmentDetDto.class, "发货明细报表.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
