package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.dto.apply.SmtBarcodeRuleSetDetDto;
import com.fantechs.common.base.entity.apply.SmtBarcodeRuleSetDet;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRuleSetDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.SmtBarcodeRuleSetDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by wcz on 2020/11/10.
 */
@RestController
@Api(tags = "条码规则关联集合")
@RequestMapping("/smtBarcodeRuleSetDet")
@Validated
public class SmtBarcodeRuleSetDetController {

    @Autowired
    private SmtBarcodeRuleSetDetService smtBarcodeRuleSetDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtBarcodeRuleSetDet smtBarcodeRuleSetDet) {
        return ControllerUtil.returnCRUD(smtBarcodeRuleSetDetService.save(smtBarcodeRuleSetDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtBarcodeRuleSetDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtBarcodeRuleSetDet.update.class) SmtBarcodeRuleSetDet smtBarcodeRuleSetDet) {
        return ControllerUtil.returnCRUD(smtBarcodeRuleSetDetService.update(smtBarcodeRuleSetDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtBarcodeRuleSetDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtBarcodeRuleSetDet  smtBarcodeRuleSetDet = smtBarcodeRuleSetDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtBarcodeRuleSetDet,StringUtils.isEmpty(smtBarcodeRuleSetDet)?0:1);
    }

    @ApiOperation("绑定条码规则")
    @PostMapping("/bindBarcodeRule")
    public ResponseEntity bindBarcodeRule(@ApiParam(value = "条码规则集合ID",required = true)@RequestParam @NotNull(message = "条码规则集合ID不能为空") Long barcodeRuleSetId,
                                         @ApiParam(value = "条码规则ID",required = true)@RequestBody @NotNull(message = "barcodeRuleIds不能为空") List<Long> barcodeRuleIds) {
        return ControllerUtil.returnCRUD(smtBarcodeRuleSetDetService.bindBarcodeRule(barcodeRuleSetId,barcodeRuleIds));
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtBarcodeRuleSetDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtBarcodeRuleSetDet searchSmtBarcodeRuleSetDet) {
        Page<Object> page = PageHelper.startPage(searchSmtBarcodeRuleSetDet.getStartPage(),searchSmtBarcodeRuleSetDet.getPageSize());
        List<SmtBarcodeRuleSetDetDto> list = smtBarcodeRuleSetDetService.findList(searchSmtBarcodeRuleSetDet);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
