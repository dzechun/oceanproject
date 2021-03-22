package com.fantechs.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
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

}
