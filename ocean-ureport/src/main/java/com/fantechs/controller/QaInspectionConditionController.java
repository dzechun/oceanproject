package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.dto.QaInspectionCondition;
import com.fantechs.entity.search.SearchQaInspectionCondition;
import com.fantechs.service.QaInspectionConditionService;
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

/**
 * Created by leifengzhi on 2020/12/29.
 */
@RestController
@Api(tags = "品保抽检情况")
@RequestMapping("/qaInspectionCondition")
@Validated
public class QaInspectionConditionController {

    @Resource
    private QaInspectionConditionService qaInspectionConditionService;

    @ApiOperation("品保抽检报表查询")
    @PostMapping("/findQaInspectionCondition")
    public ResponseEntity<List<QaInspectionCondition>> findQaInspectionCondition(@ApiParam(value = "查询对象") @RequestBody SearchQaInspectionCondition searchQaInspectionCondition) {
        Page<Object> page = PageHelper.startPage(searchQaInspectionCondition.getStartPage(), searchQaInspectionCondition.getPageSize());
        List<QaInspectionCondition> list = qaInspectionConditionService.findQaInspectionCondition(ControllerUtil.dynamicConditionByEntity(searchQaInspectionCondition));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQaInspectionCondition searchQaInspectionCondition){
        List<QaInspectionCondition> list = qaInspectionConditionService.findQaInspectionCondition(ControllerUtil.dynamicConditionByEntity(searchQaInspectionCondition));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "品保抽检导出信息", "品保抽检信息", QaInspectionCondition.class, "品保抽检.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

}
