package com.fantechs.provider.imes.apply.controller;

import com.fantechs.common.base.entity.apply.SmtBarcodeRuleSpec;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRuleSpec;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.service.SmtBarcodeRuleSpecService;
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
 * Created by wcz on 2020/11/07.
 */
@RestController
@Api(tags = "条码规则配置")
@RequestMapping("/smtBarcodeRuleSpec")
@Validated
public class SmtBarcodeRuleSpecController {

    @Autowired
    private SmtBarcodeRuleSpecService smtBarcodeRuleSpecService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtBarcodeRuleSpec smtBarcodeRuleSpec) {
        return ControllerUtil.returnCRUD(smtBarcodeRuleSpecService.save(smtBarcodeRuleSpec));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtBarcodeRuleSpecService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtBarcodeRuleSpec.update.class) SmtBarcodeRuleSpec smtBarcodeRuleSpec) {
        return ControllerUtil.returnCRUD(smtBarcodeRuleSpecService.update(smtBarcodeRuleSpec));
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchSave")
    public ResponseEntity batchSave(@ApiParam(value = "必传：",required = true)@RequestBody List<SmtBarcodeRuleSpec> smtBarcodeRuleSpecs) {
        return ControllerUtil.returnCRUD(smtBarcodeRuleSpecService.batchSave(smtBarcodeRuleSpecs));
    }


    @ApiOperation("修改")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "对象，Ids必传",required = true)@RequestBody
                                          @Validated(value=SmtBarcodeRuleSpec.update.class) List<SmtBarcodeRuleSpec> smtBarcodeRuleSpecs) {
        return ControllerUtil.returnCRUD(smtBarcodeRuleSpecService.batchUpdate(smtBarcodeRuleSpecs));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtBarcodeRuleSpec> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtBarcodeRuleSpec  smtBarcodeRuleSpec = smtBarcodeRuleSpecService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtBarcodeRuleSpec,StringUtils.isEmpty(smtBarcodeRuleSpec)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtBarcodeRuleSpec>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtBarcodeRuleSpec searchSmtBarcodeRuleSpec) {
        Page<Object> page = PageHelper.startPage(searchSmtBarcodeRuleSpec.getStartPage(),searchSmtBarcodeRuleSpec.getPageSize());
        List<SmtBarcodeRuleSpec> list = smtBarcodeRuleSpecService.findList(searchSmtBarcodeRuleSpec);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
