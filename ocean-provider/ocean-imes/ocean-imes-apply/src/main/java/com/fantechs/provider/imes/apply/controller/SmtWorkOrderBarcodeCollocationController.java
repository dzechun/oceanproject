package com.fantechs.provider.imes.apply.controller;

import com.fantechs.common.base.dto.apply.SmtWorkOrderBarcodeCollocationDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBarcodeCollocation;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrderBarcodeCollocation;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.apply.service.SmtWorkOrderBarcodeCollocationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by Mr.Lei on 2020/11/21.
 */
@RestController
@Api(tags = "工单条码配置")
@RequestMapping("/smtWorkOrderBarcodeCollocation")
@Validated
public class SmtWorkOrderBarcodeCollocationController {

    @Autowired
    private SmtWorkOrderBarcodeCollocationService smtWorkOrderBarcodeCollocationService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtWorkOrderBarcodeCollocation smtWorkOrderBarcodeCollocation) {
        return ControllerUtil.returnCRUD(smtWorkOrderBarcodeCollocationService.save(smtWorkOrderBarcodeCollocation));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtWorkOrderBarcodeCollocationService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtWorkOrderBarcodeCollocation.update.class) SmtWorkOrderBarcodeCollocation smtWorkOrderBarcodeCollocation) {
        return ControllerUtil.returnCRUD(smtWorkOrderBarcodeCollocationService.update(smtWorkOrderBarcodeCollocation));
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtWorkOrderBarcodeCollocationDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkOrderBarcodeCollocation searchSmtWorkOrderBarcodeCollocation) {
        Page<Object> page = PageHelper.startPage(searchSmtWorkOrderBarcodeCollocation.getStartPage(),searchSmtWorkOrderBarcodeCollocation.getPageSize());
        List<SmtWorkOrderBarcodeCollocationDto> list = smtWorkOrderBarcodeCollocationService.findList(searchSmtWorkOrderBarcodeCollocation);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
}
