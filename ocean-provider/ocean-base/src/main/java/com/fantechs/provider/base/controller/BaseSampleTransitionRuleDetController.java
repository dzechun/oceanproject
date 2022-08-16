package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseSampleTransitionRuleDetDto;
import com.fantechs.common.base.general.entity.basic.BaseSampleTransitionRuleDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSampleTransitionRuleDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseSampleTransitionRuleDetService;
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
 * Created by leifengzhi on 2021/04/20.
 */
@RestController
@Api(tags = "抽样转移规则明细")
@RequestMapping("/baseSampleTransitionRuleDet")
@Validated
public class BaseSampleTransitionRuleDetController {

    @Resource
    private BaseSampleTransitionRuleDetService baseSampleTransitionRuleDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseSampleTransitionRuleDet baseSampleTransitionRuleDet) {
        return ControllerUtil.returnCRUD(baseSampleTransitionRuleDetService.save(baseSampleTransitionRuleDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSampleTransitionRuleDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseSampleTransitionRuleDet.update.class) BaseSampleTransitionRuleDet baseSampleTransitionRuleDet) {
        return ControllerUtil.returnCRUD(baseSampleTransitionRuleDetService.update(baseSampleTransitionRuleDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSampleTransitionRuleDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseSampleTransitionRuleDet  baseSampleTransitionRuleDet = baseSampleTransitionRuleDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseSampleTransitionRuleDet,StringUtils.isEmpty(baseSampleTransitionRuleDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSampleTransitionRuleDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSampleTransitionRuleDet searchBaseSampleTransitionRuleDet) {
        Page<Object> page = PageHelper.startPage(searchBaseSampleTransitionRuleDet.getStartPage(),searchBaseSampleTransitionRuleDet.getPageSize());
        List<BaseSampleTransitionRuleDetDto> list = baseSampleTransitionRuleDetService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSampleTransitionRuleDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
