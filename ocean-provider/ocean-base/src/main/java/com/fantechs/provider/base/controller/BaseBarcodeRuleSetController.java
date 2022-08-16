package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSetDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSet;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBarcodeRuleSet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseBarcodeRuleSetService;
import com.fantechs.provider.base.service.BaseHtBarcodeRuleSetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by wcz on 2020/11/09.
 */
@RestController
@Api(tags = "条码规则集合")
@RequestMapping("/baseBarcodeRuleSet")
@Validated
public class BaseBarcodeRuleSetController {

    @Autowired
    private BaseBarcodeRuleSetService baseBarcodeRuleSetService;
    @Autowired
    private BaseHtBarcodeRuleSetService baseHtBarcodeRuleSetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseBarcodeRuleSet baseBarcodeRuleSet) {
        return ControllerUtil.returnCRUD(baseBarcodeRuleSetService.save(baseBarcodeRuleSet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseBarcodeRuleSetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseBarcodeRuleSet.update.class) BaseBarcodeRuleSet baseBarcodeRuleSet) {
        return ControllerUtil.returnCRUD(baseBarcodeRuleSetService.update(baseBarcodeRuleSet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseBarcodeRuleSet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseBarcodeRuleSet  baseBarcodeRuleSet = baseBarcodeRuleSetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseBarcodeRuleSet,StringUtils.isEmpty(baseBarcodeRuleSet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseBarcodeRuleSetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBarcodeRuleSet searchBaseBarcodeRuleSet) {
        Page<Object> page = PageHelper.startPage(searchBaseBarcodeRuleSet.getStartPage(),searchBaseBarcodeRuleSet.getPageSize());
        List<BaseBarcodeRuleSetDto> list = baseBarcodeRuleSetService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBarcodeRuleSet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtBarcodeRuleSet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBarcodeRuleSet searchBaseBarcodeRuleSet) {
        Page<Object> page = PageHelper.startPage(searchBaseBarcodeRuleSet.getStartPage(),searchBaseBarcodeRuleSet.getPageSize());
        List<BaseHtBarcodeRuleSet> list = baseHtBarcodeRuleSetService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBarcodeRuleSet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchBaseBarcodeRuleSet searchBaseBarcodeRuleSet){
    List<BaseBarcodeRuleSetDto> list = baseBarcodeRuleSetService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBarcodeRuleSet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "条码规则集合", BaseBarcodeRuleSetDto.class, "BaseBarcodeRuleSet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
