package com.fantechs.provider.imes.apply.controller;

import com.fantechs.common.base.dto.apply.SmtBarcodeRuleDto;
import com.fantechs.common.base.entity.apply.SmtBarcodeRule;
import com.fantechs.common.base.entity.apply.history.SmtHtBarcodeRule;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRule;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.service.SmtBarcodeRuleService;
import com.fantechs.provider.imes.apply.service.SmtHtBarcodeRuleService;
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
 * Created by wcz on 2020/10/26.
 */
@RestController
@Api(tags = "条码规则")
@RequestMapping("/smtBarcodeRule")
@Validated
public class SmtBarcodeRuleController {

    @Autowired
    private SmtBarcodeRuleService smtBarcodeRuleService;
    @Autowired
    private SmtHtBarcodeRuleService smtHtBarcodeRuleService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：barcodeRuleId,barcodeRuleCode,barcodeRuleName,barcodeRule",required = true)@RequestBody @Validated SmtBarcodeRule smtBarcodeRule) {
        return ControllerUtil.returnCRUD(smtBarcodeRuleService.save(smtBarcodeRule));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtBarcodeRuleService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtBarcodeRule.update.class) SmtBarcodeRule smtBarcodeRule) {
        return ControllerUtil.returnCRUD(smtBarcodeRuleService.update(smtBarcodeRule));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtBarcodeRule> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtBarcodeRule  smtBarcodeRule = smtBarcodeRuleService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtBarcodeRule,StringUtils.isEmpty(smtBarcodeRule)?0:1);
    }

    @ApiOperation("条码规则列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtBarcodeRuleDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtBarcodeRule searchSmtBarcodeRule) {
        Page<Object> page = PageHelper.startPage(searchSmtBarcodeRule.getStartPage(),searchSmtBarcodeRule.getPageSize());
        List<SmtBarcodeRuleDto> list = smtBarcodeRuleService.findList(searchSmtBarcodeRule);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("条码规则历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtBarcodeRule>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtBarcodeRule searchSmtBarcodeRule) {
        Page<Object> page = PageHelper.startPage(searchSmtBarcodeRule.getStartPage(),searchSmtBarcodeRule.getPageSize());
        List<SmtHtBarcodeRule> list = smtHtBarcodeRuleService.findList(searchSmtBarcodeRule);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchSmtBarcodeRule searchSmtBarcodeRule){
    List<SmtBarcodeRuleDto> list = smtBarcodeRuleService.findList(searchSmtBarcodeRule);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出条码规则信息", "条码规则信息", SmtBarcodeRuleDto.class, "条码规则信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
