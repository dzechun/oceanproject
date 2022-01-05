package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseHtInAndOutRuleDetDto;
import com.fantechs.common.base.general.dto.basic.BaseInAndOutRuleDetDto;
import com.fantechs.common.base.general.entity.basic.BaseInAndOutRuleDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInAndOutRuleDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseInAndOutRuleDetService;
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
 * Created by mr.lei on 2021/12/30.
 */
@RestController
@Api(tags = "出入库规则明细")
@RequestMapping("/baseInAndOutRuleDet")
@Validated
public class BaseInAndOutRuleDetController {

    @Resource
    private BaseInAndOutRuleDetService baseInAndOutRuleDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseInAndOutRuleDet baseInAndOutRuleDet) {
        return ControllerUtil.returnCRUD(baseInAndOutRuleDetService.save(baseInAndOutRuleDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseInAndOutRuleDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseInAndOutRuleDet.update.class) BaseInAndOutRuleDet baseInAndOutRuleDet) {
        return ControllerUtil.returnCRUD(baseInAndOutRuleDetService.update(baseInAndOutRuleDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseInAndOutRuleDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseInAndOutRuleDet  baseInAndOutRuleDet = baseInAndOutRuleDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseInAndOutRuleDet,StringUtils.isEmpty(baseInAndOutRuleDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseInAndOutRuleDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInAndOutRuleDet searchBaseInAndOutRuleDet) {
        Page<Object> page = PageHelper.startPage(searchBaseInAndOutRuleDet.getStartPage(),searchBaseInAndOutRuleDet.getPageSize());
        List<BaseInAndOutRuleDetDto> list = baseInAndOutRuleDetService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInAndOutRuleDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<BaseInAndOutRuleDetDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchBaseInAndOutRuleDet searchBaseInAndOutRuleDet) {
        List<BaseInAndOutRuleDetDto> list = baseInAndOutRuleDetService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInAndOutRuleDet));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtInAndOutRuleDetDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInAndOutRuleDet searchBaseInAndOutRuleDet) {
        Page<Object> page = PageHelper.startPage(searchBaseInAndOutRuleDet.getStartPage(),searchBaseInAndOutRuleDet.getPageSize());
        List<BaseHtInAndOutRuleDetDto> list = baseInAndOutRuleDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseInAndOutRuleDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
