package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.dto.basic.BatchGenerateCodeDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBarcodeRule;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseBarcodeRuleService;
import com.fantechs.provider.base.service.BaseHtBarcodeRuleService;
import com.fantechs.provider.base.util.BarcodeRuleUtils;
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
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/26.
 */
@RestController
@Api(tags = "条码规则")
@RequestMapping("/baseBarcodeRule")
@Validated
public class BaseBarcodeRuleController {

    @Autowired
    private BaseBarcodeRuleService baseBarcodeRuleService;
    @Autowired
    private BaseHtBarcodeRuleService baseHtBarcodeRuleService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：barcodeRuleId,barcodeRuleCode,barcodeRuleName,barcodeRule",required = true)@RequestBody @Validated BaseBarcodeRule baseBarcodeRule) {
        return ControllerUtil.returnCRUD(baseBarcodeRuleService.save(baseBarcodeRule));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseBarcodeRuleService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseBarcodeRule.update.class) BaseBarcodeRule baseBarcodeRule) {
        return ControllerUtil.returnCRUD(baseBarcodeRuleService.update(baseBarcodeRule));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseBarcodeRule> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseBarcodeRule  baseBarcodeRule = baseBarcodeRuleService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseBarcodeRule,StringUtils.isEmpty(baseBarcodeRule)?0:1);
    }

    @ApiOperation("条码规则列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseBarcodeRuleDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBarcodeRule searchBaseBarcodeRule) {
        Page<Object> page = PageHelper.startPage(searchBaseBarcodeRule.getStartPage(),searchBaseBarcodeRule.getPageSize());
        List<BaseBarcodeRuleDto> list = baseBarcodeRuleService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBarcodeRule));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("条码规则列表")
    @PostMapping("/findListByBarcodeRuleCategoryIds")
    public ResponseEntity<List<BaseBarcodeRule>> findListByBarcodeRuleCategoryIds(@ApiParam(value = "查询对象")@RequestBody List<Long> ids) {
        List<BaseBarcodeRule> list = baseBarcodeRuleService.findListByBarcodeRuleCategoryIds(ids);
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("条码规则历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtBarcodeRule>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBarcodeRule searchBaseBarcodeRule) {
        Page<Object> page = PageHelper.startPage(searchBaseBarcodeRule.getStartPage(),searchBaseBarcodeRule.getPageSize());
        List<BaseHtBarcodeRule> list = baseHtBarcodeRuleService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBarcodeRule));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchBaseBarcodeRule searchBaseBarcodeRule){
    List<BaseBarcodeRuleDto> list = baseBarcodeRuleService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseBarcodeRule));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出条码规则信息", "条码规则信息", BaseBarcodeRuleDto.class, "BaseBarcodeRule.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation(value = "保存",notes = "保存")
    @PostMapping("/preserve")
    public ResponseEntity preserve(@RequestBody @Validated BaseBarcodeRule baseBarcodeRule) {
        return ControllerUtil.returnCRUD(baseBarcodeRuleService.preserve(baseBarcodeRule));
    }

    @ApiOperation(value = "生成条码")
    @PostMapping("/generateCode")
    public ResponseEntity<String> generateCode(
            @ApiParam(value = "条码规则集合")@RequestBody List<BaseBarcodeRuleSpec> list,
            @ApiParam(value = "最大条码数")@RequestParam(required = false) String maxCode,
            @ApiParam(value = "产品料号、生产线别、客户料号")@RequestParam (required = false)String code,
            @ApiParam(value = "执行函数参数")@RequestParam (required = false)String params){
        String analysisCode = BarcodeRuleUtils.analysisCode(list, maxCode, code,params,null);
        return ControllerUtil.returnDataSuccess(analysisCode,1);
    }

    @ApiOperation(value = "批量生成条码")
    @PostMapping("/batchGenerateCode")
    public ResponseEntity<List<String>> batchGenerateCode(@RequestBody BatchGenerateCodeDto dto){
        List<String> barcodeList = BarcodeRuleUtils.batchAnalysisCodeByWanbao(dto.getList(), dto.getCode(), dto.getParams(), null,
                dto.getQty(), dto.getKey());
        return ControllerUtil.returnDataSuccess(barcodeList, barcodeList.size());
    }

    @ApiOperation(value = "生成条码-Map")
    @PostMapping("/newGenerateCode")
    public ResponseEntity<String> newGenerateCode(
            @ApiParam(value = "条码规则集合")@RequestBody List<BaseBarcodeRuleSpec> list,
            @ApiParam(value = "最大条码数")@RequestParam(required = false) String maxCode,
            @ApiParam(value = "产品料号、生产线别、客户料号")@RequestParam (required = false)Map<String,Object> map,
            @ApiParam(value = "执行函数参数")@RequestParam (required = false)String params){
        String analysisCode = BarcodeRuleUtils.analysisCode(list, maxCode, null,params,map);
        return ControllerUtil.returnDataSuccess(analysisCode,1);
    }

    @ApiOperation(value = "获取最大流水号")
    @PostMapping("/generateMaxCode")
    public ResponseEntity<String> generateMaxCode(
            @ApiParam(value = "条码规则集合")@RequestBody List<BaseBarcodeRuleSpec> list,
            @ApiParam(value = "最大条码数")@RequestParam(required = false) String maxCode){
        String analysisCode = BarcodeRuleUtils.getMaxSerialNumber(list, maxCode);
        return ControllerUtil.returnDataSuccess(analysisCode,1);
    }
}
