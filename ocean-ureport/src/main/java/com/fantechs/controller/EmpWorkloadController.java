package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.dto.EmpWorkload;
import com.fantechs.entity.search.SearchEmpWorkload;
import com.fantechs.service.EmpWorkloadService;
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
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/29.
 */
@RestController
@Api(tags = "员工工作量查询")
@RequestMapping("/empWorkload")
@Validated
public class EmpWorkloadController {

    @Resource
    private EmpWorkloadService empWorkloadService;

    @ApiOperation("员工工作量报表查询")
    @PostMapping("/findHistogram")
    public ResponseEntity<List<Map<String, Object>>> findHistogram(@ApiParam(value = "查询对象") @RequestBody SearchEmpWorkload searchEmpWorkload) {
        Page<Object> page = PageHelper.startPage(searchEmpWorkload.getStartPage(), searchEmpWorkload.getPageSize());
        return ControllerUtil.returnDataSuccess(empWorkloadService.findHistogram(ControllerUtil.dynamicConditionByEntity(searchEmpWorkload)), (int) page.getTotal());
    }

    @ApiOperation("员工工作量列表查询")
    @PostMapping("/findHistogramList")
    public ResponseEntity<List<EmpWorkload>> findHistogramList(@ApiParam(value = "查询对象") @RequestBody SearchEmpWorkload searchEmpWorkload) {
        Page<Object> page = PageHelper.startPage(searchEmpWorkload.getStartPage(), searchEmpWorkload.getPageSize());
        return ControllerUtil.returnDataSuccess(empWorkloadService.findHistogramList(ControllerUtil.dynamicConditionByEntity(searchEmpWorkload)), (int) page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEmpWorkload searchEmpWorkload){
        List<EmpWorkload> list = empWorkloadService.findHistogramList(ControllerUtil.dynamicConditionByEntity(searchEmpWorkload));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "员工工作量导出信息", "员工工作量信息", EmpWorkload.class, "员工工作量.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

}
