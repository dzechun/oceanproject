package com.fantechs.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
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

}
