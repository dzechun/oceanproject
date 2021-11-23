package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.entity.BarcodeTraceModel;
import com.fantechs.entity.MonthInOutModel;
import com.fantechs.entity.search.SearchBarcodeTrace;
import com.fantechs.entity.search.SearchMonthInOut;
import com.fantechs.service.MonthInOutService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
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

    @PostMapping("/findOutList")
    @ApiModelProperty("月出报表")
    public ResponseEntity<List<MonthInOutModel>> findList(@RequestBody(required = false) SearchMonthInOut searchMonthInOut){
        Page<Object> page = PageHelper.startPage(searchMonthInOut.getStartPage(), searchMonthInOut.getPageSize());
        return ControllerUtil.returnDataSuccess(monthInOutService.findOutList(ControllerUtil.dynamicConditionByEntity(searchMonthInOut)),(int)page.getTotal());
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void export(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMonthInOut searchMonthInOut){
        List<MonthInOutModel> list = monthInOutService.findOutList(ControllerUtil.dynamicConditionByEntity(searchMonthInOut));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "月出报表", "月出报表", MonthInOutModel.class, "月出报表.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
