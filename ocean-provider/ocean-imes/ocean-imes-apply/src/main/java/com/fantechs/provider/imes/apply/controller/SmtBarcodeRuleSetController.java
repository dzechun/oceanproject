package com.fantechs.provider.imes.apply.controller;

import com.fantechs.common.base.dto.apply.SmtBarcodeRuleSetDto;
import com.fantechs.common.base.entity.apply.SmtBarcodeRuleSet;
import com.fantechs.common.base.entity.apply.history.SmtHtBarcodeRuleSet;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRuleSet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.service.SmtBarcodeRuleSetService;
import com.fantechs.provider.imes.apply.service.SmtHtBarcodeRuleSetService;
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
@RequestMapping("/smtBarcodeRuleSet")
@Validated
public class SmtBarcodeRuleSetController {

    @Autowired
    private SmtBarcodeRuleSetService smtBarcodeRuleSetService;
    @Autowired
    private SmtHtBarcodeRuleSetService smtHtBarcodeRuleSetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtBarcodeRuleSet smtBarcodeRuleSet) {
        return ControllerUtil.returnCRUD(smtBarcodeRuleSetService.save(smtBarcodeRuleSet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtBarcodeRuleSetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtBarcodeRuleSet.update.class) SmtBarcodeRuleSet smtBarcodeRuleSet) {
        return ControllerUtil.returnCRUD(smtBarcodeRuleSetService.update(smtBarcodeRuleSet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtBarcodeRuleSet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtBarcodeRuleSet  smtBarcodeRuleSet = smtBarcodeRuleSetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtBarcodeRuleSet,StringUtils.isEmpty(smtBarcodeRuleSet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtBarcodeRuleSetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtBarcodeRuleSet searchSmtBarcodeRuleSet) {
        Page<Object> page = PageHelper.startPage(searchSmtBarcodeRuleSet.getStartPage(),searchSmtBarcodeRuleSet.getPageSize());
        List<SmtBarcodeRuleSetDto> list = smtBarcodeRuleSetService.findList(searchSmtBarcodeRuleSet);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<SmtHtBarcodeRuleSet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchSmtBarcodeRuleSet searchSmtBarcodeRuleSet) {
        Page<Object> page = PageHelper.startPage(searchSmtBarcodeRuleSet.getStartPage(),searchSmtBarcodeRuleSet.getPageSize());
        List<SmtHtBarcodeRuleSet> list = smtHtBarcodeRuleSetService.findList(searchSmtBarcodeRuleSet);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchSmtBarcodeRuleSet searchSmtBarcodeRuleSet){
    List<SmtBarcodeRuleSetDto> list = smtBarcodeRuleSetService.findList(searchSmtBarcodeRuleSet);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "条码规则集合", SmtBarcodeRuleSetDto.class, "SmtBarcodeRuleSet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
