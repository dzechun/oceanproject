package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSampleTransitionRuleDto;
import com.fantechs.common.base.general.entity.basic.BaseSampleTransitionRule;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSampleTransitionRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSampleTransitionRule;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtSampleTransitionRuleService;
import com.fantechs.provider.base.service.BaseSampleTransitionRuleService;
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
 * Created by leifengzhi on 2021/04/20.
 */
@RestController
@Api(tags = "抽样转移规则")
@RequestMapping("/baseSampleTransitionRule")
@Validated
public class BaseSampleTransitionRuleController {

    @Resource
    private BaseSampleTransitionRuleService baseSampleTransitionRuleService;

    @Resource
    private BaseHtSampleTransitionRuleService baseHtSampleTransitionRuleService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseSampleTransitionRule baseSampleTransitionRule) {
        return ControllerUtil.returnCRUD(baseSampleTransitionRuleService.save(baseSampleTransitionRule));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSampleTransitionRuleService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseSampleTransitionRule.update.class) BaseSampleTransitionRule baseSampleTransitionRule) {
        return ControllerUtil.returnCRUD(baseSampleTransitionRuleService.update(baseSampleTransitionRule));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSampleTransitionRule> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseSampleTransitionRule  baseSampleTransitionRule = baseSampleTransitionRuleService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseSampleTransitionRule,StringUtils.isEmpty(baseSampleTransitionRule)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSampleTransitionRuleDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSampleTransitionRule searchBaseSampleTransitionRule) {
        Page<Object> page = PageHelper.startPage(searchBaseSampleTransitionRule.getStartPage(),searchBaseSampleTransitionRule.getPageSize());
        List<BaseSampleTransitionRuleDto> list = baseSampleTransitionRuleService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSampleTransitionRule));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtSampleTransitionRule>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSampleTransitionRule searchBaseSampleTransitionRule) {
        Page<Object> page = PageHelper.startPage(searchBaseSampleTransitionRule.getStartPage(),searchBaseSampleTransitionRule.getPageSize());
        List<BaseHtSampleTransitionRule> list = baseHtSampleTransitionRuleService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSampleTransitionRule));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseSampleTransitionRule searchBaseSampleTransitionRule){
    List<BaseSampleTransitionRuleDto> list = baseSampleTransitionRuleService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSampleTransitionRule));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "抽样转移规则导出信息", "抽样转移规则信息", BaseSampleTransitionRuleDto.class, "抽样转移规则.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
