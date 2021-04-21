package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSamplingPlanDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplingPlan;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSamplingPlan;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSamplingPlan;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtSamplingPlanService;
import com.fantechs.provider.base.service.BaseSamplingPlanService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */
@RestController
@Api(tags = "抽样方案")
@RequestMapping("/baseSamplingPlan")
@Validated
public class BaseSamplingPlanController {

    @Resource
    private BaseSamplingPlanService baseSamplingPlanService;

    @Resource
    private BaseHtSamplingPlanService baseHtSamplingPlanService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseSamplingPlan baseSamplingPlan) {
        return ControllerUtil.returnCRUD(baseSamplingPlanService.save(baseSamplingPlan));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSamplingPlanService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseSamplingPlan.update.class) BaseSamplingPlan baseSamplingPlan) {
        return ControllerUtil.returnCRUD(baseSamplingPlanService.update(baseSamplingPlan));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSamplingPlan> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseSamplingPlan  baseSamplingPlan = baseSamplingPlanService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseSamplingPlan,StringUtils.isEmpty(baseSamplingPlan)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSamplingPlanDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSamplingPlan searchBaseSamplingPlan) {
        Page<Object> page = PageHelper.startPage(searchBaseSamplingPlan.getStartPage(),searchBaseSamplingPlan.getPageSize());
        List<BaseSamplingPlanDto> list = baseSamplingPlanService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSamplingPlan));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtSamplingPlan>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSamplingPlan searchBaseSamplingPlan) {
        Page<Object> page = PageHelper.startPage(searchBaseSamplingPlan.getStartPage(),searchBaseSamplingPlan.getPageSize());
        List<BaseHtSamplingPlan> list = baseHtSamplingPlanService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSamplingPlan));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseSamplingPlan searchBaseSamplingPlan){
    List<BaseSamplingPlanDto> list = baseSamplingPlanService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSamplingPlan));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "抽样方案导出信息", "抽样方案信息", BaseSamplingPlanDto.class, "抽样方案.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
