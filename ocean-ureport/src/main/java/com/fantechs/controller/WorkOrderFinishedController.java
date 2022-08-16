package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.dto.WorkOrderFinished;
import com.fantechs.entity.search.SearchWorkOrderFinished;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.service.WorkOrderFinishedService;
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
@RequestMapping("/workOrderFinished")
@Api(tags = "工单完工报表")
public class WorkOrderFinishedController {

    @Resource
    WorkOrderFinishedService workOrderFinishedService;

    @ApiOperation("工单完工列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WorkOrderFinished>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWorkOrderFinished searchWorkOrderFinished) {
        Page<Object> page = PageHelper.startPage(searchWorkOrderFinished.getStartPage(),searchWorkOrderFinished.getPageSize());
        List<WorkOrderFinished> list = workOrderFinishedService.findList(searchWorkOrderFinished);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWorkOrderFinished searchWorkOrderFinished){
        List<WorkOrderFinished> list = workOrderFinishedService.findList(searchWorkOrderFinished);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "工单完工信息", "WorkOrderFinished信息", WorkOrderFinished.class, "WorkOrderFinished.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
