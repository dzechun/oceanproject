package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSpecDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseBarcodeRuleSpecService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by wcz on 2020/11/07.
 */
@RestController
@Api(tags = "条码规则配置")
@RequestMapping("/baseBarcodeRuleSpec")
@Validated
public class BaseBarcodeRuleSpecController {

    @Resource
    private BaseBarcodeRuleSpecService baseBarcodeRuleSpecService;

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseBarcodeRuleSpec> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseBarcodeRuleSpec baseBarcodeRuleSpec = baseBarcodeRuleSpecService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseBarcodeRuleSpec,StringUtils.isEmpty(baseBarcodeRuleSpec)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseBarcodeRuleSpecDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec) {
        Page<Object> page = PageHelper.startPage(searchBaseBarcodeRuleSpec.getStartPage(), searchBaseBarcodeRuleSpec.getPageSize());
        List<BaseBarcodeRuleSpecDto> list = baseBarcodeRuleSpecService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBarcodeRuleSpec));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表")
    @PostMapping("/findSpec")
    public ResponseEntity<List<BaseBarcodeRuleSpec>> findSpec(@ApiParam(value = "查询对象")@RequestBody SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec){
        List<BaseBarcodeRuleSpec> list = baseBarcodeRuleSpecService.findSpec(searchBaseBarcodeRuleSpec);
        return ControllerUtil.returnDataSuccess(list,(int)list.size());
    }

    @ApiOperation("函数列表")
    @GetMapping("/findFunction")
    public ResponseEntity<List<String>> findFunction(){
        List<String> ss = baseBarcodeRuleSpecService.findFunction();
        return ControllerUtil.returnDataSuccess(ss,StringUtils.isEmpty(ss)?0:1);
    }

}
