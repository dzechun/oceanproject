package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseSamplePlanAqlDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplePlanAql;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSamplePlanAql;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseSamplePlanAqlService;
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
@RequestMapping("/baseSamplePlanAql")
@Validated
public class BaseSamplePlanAqlController {

    @Resource
    private BaseSamplePlanAqlService baseSamplePlanAqlService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<BaseSamplePlanAql> list) {
        return ControllerUtil.returnCRUD(baseSamplePlanAqlService.batchSave(list));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSamplePlanAqlService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseSamplePlanAql.update.class) List<BaseSamplePlanAql> list) {
        return ControllerUtil.returnCRUD(baseSamplePlanAqlService.batchUpdate(list));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSamplePlanAql> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseSamplePlanAql baseSamplePlanAql = baseSamplePlanAqlService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseSamplePlanAql,StringUtils.isEmpty(baseSamplePlanAql)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSamplePlanAqlDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSamplePlanAql searchBaseSamplePlanAql) {
        Page<Object> page = PageHelper.startPage(searchBaseSamplePlanAql.getStartPage(), searchBaseSamplePlanAql.getPageSize());
        List<BaseSamplePlanAqlDto> list = baseSamplePlanAqlService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSamplePlanAql));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
