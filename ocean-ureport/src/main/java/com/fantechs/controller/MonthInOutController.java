package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.dto.MonthInDto;
import com.fantechs.dto.MonthOutDto;
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
    @ApiModelProperty("月入报表")
    public ResponseEntity<List<MonthInDto>> findInList(@RequestBody(required = false) SearchMonthInOut searchMonthInOut){
        Page<Object> page = PageHelper.startPage(searchMonthInOut.getStartPage(), searchMonthInOut.getPageSize());
        return ControllerUtil.returnDataSuccess(monthInOutService.findInList(ControllerUtil.dynamicConditionByEntity(searchMonthInOut)),(int)page.getTotal());
    }

    @PostMapping("/findInListBarCode")
    @ApiModelProperty("月入条码明细")
    public ResponseEntity<List<String>> findInListBarCode(@RequestBody(required = false) SearchMonthInOutBarCode searchMonthInOutBarCode){
        Page<Object> page = PageHelper.startPage(searchMonthInOutBarCode.getStartPage(), searchMonthInOutBarCode.getPageSize());
        return ControllerUtil.returnDataSuccess(monthInOutService.findInListBarCode(ControllerUtil.dynamicConditionByEntity(searchMonthInOutBarCode)),(int)page.getTotal());
    }

    @PostMapping("/findOutList")
    @ApiModelProperty("月出报表")
    public ResponseEntity<List<MonthOutDto>> findOutList(@RequestBody(required = false) SearchMonthInOut searchMonthInOut){
        Page<Object> page = PageHelper.startPage(searchMonthInOut.getStartPage(), searchMonthInOut.getPageSize());
        return ControllerUtil.returnDataSuccess(monthInOutService.findOutList(ControllerUtil.dynamicConditionByEntity(searchMonthInOut)),(int)page.getTotal());
    }

    @PostMapping("/findOutListBarCode")
    @ApiModelProperty("月入条码明细")
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
}
