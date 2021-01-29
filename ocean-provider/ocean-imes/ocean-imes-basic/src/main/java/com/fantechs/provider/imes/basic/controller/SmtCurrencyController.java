package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.dto.basic.SmtCurrencyDto;
import com.fantechs.common.base.entity.basic.SmtCurrency;
import com.fantechs.common.base.entity.basic.search.SearchSmtCurrency;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtCurrencyService;
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
 * Created by leifengzhi on 2020/11/13.
 */
@RestController
@Api(tags = "币别信息管理")
@RequestMapping("/smtCurrency")
@Validated
public class SmtCurrencyController {

    @Autowired
    private SmtCurrencyService smtCurrencyService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：currencyCode,currencyName",required = true)@RequestBody @Validated SmtCurrency smtCurrency) {
        return ControllerUtil.returnCRUD(smtCurrencyService.save(smtCurrency));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtCurrencyService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtCurrency.update.class) SmtCurrency smtCurrency) {
        return ControllerUtil.returnCRUD(smtCurrencyService.update(smtCurrency));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtCurrency> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtCurrency  smtCurrency = smtCurrencyService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtCurrency,StringUtils.isEmpty(smtCurrency)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtCurrencyDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtCurrency searchSmtCurrency) {
        Page<Object> page = PageHelper.startPage(searchSmtCurrency.getStartPage(),searchSmtCurrency.getPageSize());
        List<SmtCurrencyDto> list = smtCurrencyService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtCurrency));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtCurrency searchSmtCurrency){
    List<SmtCurrencyDto> list = smtCurrencyService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtCurrency));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtCurrency信息", SmtCurrencyDto.class, "SmtCurrency.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
