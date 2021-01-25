package com.fantechs.provider.mes.pm.controller;

import com.fantechs.common.base.general.dto.mes.pm.SmtBarcodeRuleSpecDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRuleSpec;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSpec;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.service.SmtBarcodeRuleSpecService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtBarcodeRuleSpec> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtBarcodeRuleSpec  smtBarcodeRuleSpec = smtBarcodeRuleSpecService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtBarcodeRuleSpec,StringUtils.isEmpty(smtBarcodeRuleSpec)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtBarcodeRuleSpecDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtBarcodeRuleSpec searchSmtBarcodeRuleSpec) {
        Page<Object> page = PageHelper.startPage(searchSmtBarcodeRuleSpec.getStartPage(),searchSmtBarcodeRuleSpec.getPageSize());
        List<SmtBarcodeRuleSpecDto> list = smtBarcodeRuleSpecService.findList(searchSmtBarcodeRuleSpec);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表")
    @PostMapping("/findSpec")
    public ResponseEntity<List<SmtBarcodeRuleSpec>> findSpec(@ApiParam(value = "查询对象")@RequestBody SearchSmtBarcodeRuleSpec searchSmtBarcodeRuleSpec){
        List<SmtBarcodeRuleSpec> list = smtBarcodeRuleSpecService.findSpec(searchSmtBarcodeRuleSpec);
        return ControllerUtil.returnDataSuccess(list,(int)list.size());
    }

}
