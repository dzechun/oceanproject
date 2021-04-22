package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseSamplingPlanAqlDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplingPlanAql;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSamplingPlanAql;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseSamplingPlanAqlService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */
@RestController
@Api(tags = "抽样方案AQL")
@RequestMapping("/baseSamplingPlanAql")
@Validated
public class BaseSamplingPlanAqlController {

    @Resource
    private BaseSamplingPlanAqlService baseSamplingPlanAqlService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<BaseSamplingPlanAql> list) {
        return ControllerUtil.returnCRUD(baseSamplingPlanAqlService.batchSave(list));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSamplingPlanAqlService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseSamplingPlanAql.update.class) List<BaseSamplingPlanAql> list) {
        return ControllerUtil.returnCRUD(baseSamplingPlanAqlService.batchUpdate(list));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSamplingPlanAql> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseSamplingPlanAql  baseSamplingPlanAql = baseSamplingPlanAqlService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseSamplingPlanAql,StringUtils.isEmpty(baseSamplingPlanAql)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSamplingPlanAqlDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSamplingPlanAql searchBaseSamplingPlanAql) {
        Page<Object> page = PageHelper.startPage(searchBaseSamplingPlanAql.getStartPage(),searchBaseSamplingPlanAql.getPageSize());
        List<BaseSamplingPlanAqlDto> list = baseSamplingPlanAqlService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSamplingPlanAql));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
