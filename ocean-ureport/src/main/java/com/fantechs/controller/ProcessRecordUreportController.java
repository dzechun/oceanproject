package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.dto.ProcessRecordUreportDto;
import com.fantechs.dto.WorkOrderUreportDto;
import com.fantechs.entity.search.SearchProcessRecordUreportDto;
import com.fantechs.entity.search.SearchWorkOrderUreportDto;
import com.fantechs.service.ProcessRecordUreportService;
import com.fantechs.service.WorkOrderUreportService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author bgkun
 * @Date 2021/11/24
 */
@RestController
@Api(tags = "过站日报表")
@RequestMapping("/processRecordUreport")
public class ProcessRecordUreportController {

    @Resource
    private ProcessRecordUreportService processRecordUreportService;


    @PostMapping("/findList")
    @ApiModelProperty("过站日报表")
    public ResponseEntity<List<ProcessRecordUreportDto>> findList(@RequestBody(required = false) SearchProcessRecordUreportDto dto){
        dto.setPageSize(9999);
        Page<Object> page = PageHelper.startPage(dto.getStartPage(), dto.getPageSize());
        return ControllerUtil.returnDataSuccess(processRecordUreportService.findList(ControllerUtil.dynamicConditionByEntity(dto)), (int)page.getTotal());
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void export(HttpServletResponse response, @RequestBody(required = false)SearchWorkOrderUreportDto dto){
        List<ProcessRecordUreportDto> list = processRecordUreportService.findList(ControllerUtil.dynamicConditionByEntity(dto));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "过站日报表", "过站日报表", ProcessRecordUreportDto.class, "过站日报表.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
