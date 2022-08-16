package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSetDetDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSetDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSetDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseBarcodeRuleSetDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by wcz on 2020/11/10.
 */
@RestController
@Api(tags = "条码规则关联集合")
@RequestMapping("/baseBarcodeRuleSetDet")
@Validated
public class BaseBarcodeRuleSetDetController {

    @Resource
    private BaseBarcodeRuleSetDetService baseBarcodeRuleSetDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseBarcodeRuleSetDet baseBarcodeRuleSetDet) {
        return ControllerUtil.returnCRUD(baseBarcodeRuleSetDetService.save(baseBarcodeRuleSetDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseBarcodeRuleSetDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseBarcodeRuleSetDet.update.class) BaseBarcodeRuleSetDet baseBarcodeRuleSetDet) {
        return ControllerUtil.returnCRUD(baseBarcodeRuleSetDetService.update(baseBarcodeRuleSetDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseBarcodeRuleSetDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseBarcodeRuleSetDet baseBarcodeRuleSetDet = baseBarcodeRuleSetDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseBarcodeRuleSetDet,StringUtils.isEmpty(baseBarcodeRuleSetDet)?0:1);
    }

    @ApiOperation("绑定条码规则")
    @PostMapping("/bindBarcodeRule")
    public ResponseEntity bindBarcodeRule(@ApiParam(value = "条码规则集合ID",required = true)@RequestParam @NotNull(message = "条码规则集合ID不能为空") Long barcodeRuleSetId,
                                         @ApiParam(value = "条码规则ID",required = true)@RequestBody @NotNull(message = "barcodeRuleIds不能为空") List<Long> barcodeRuleIds) {
        return ControllerUtil.returnCRUD(baseBarcodeRuleSetDetService.bindBarcodeRule(barcodeRuleSetId,barcodeRuleIds));
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseBarcodeRuleSetDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBarcodeRuleSetDet searchBaseBarcodeRuleSetDet) {
        Page<Object> page = PageHelper.startPage(searchBaseBarcodeRuleSetDet.getStartPage(), searchBaseBarcodeRuleSetDet.getPageSize());
        List<BaseBarcodeRuleSetDetDto> list = baseBarcodeRuleSetDetService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBarcodeRuleSetDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
