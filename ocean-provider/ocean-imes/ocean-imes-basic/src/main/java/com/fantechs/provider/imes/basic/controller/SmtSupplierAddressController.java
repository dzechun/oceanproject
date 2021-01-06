package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.entity.basic.SmtSupplierAddress;
import com.fantechs.common.base.entity.basic.search.SearchSmtSupplierAddress;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtSupplierAddressService;
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
 * Created by leifengzhi on 2021/01/05.
 */
@RestController
@Api(tags = "供应商与地址关联管理")
@RequestMapping("/smtSupplierAddress")
@Validated
public class SmtSupplierAddressController {

    @Autowired
    private SmtSupplierAddressService smtSupplierAddressService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtSupplierAddress smtSupplierAddress) {
        return ControllerUtil.returnCRUD(smtSupplierAddressService.save(smtSupplierAddress));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtSupplierAddressService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtSupplierAddress.update.class) SmtSupplierAddress smtSupplierAddress) {
        return ControllerUtil.returnCRUD(smtSupplierAddressService.update(smtSupplierAddress));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtSupplierAddress> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtSupplierAddress  smtSupplierAddress = smtSupplierAddressService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtSupplierAddress,StringUtils.isEmpty(smtSupplierAddress)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtSupplierAddress>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtSupplierAddress searchSmtSupplierAddress) {
        Page<Object> page = PageHelper.startPage(searchSmtSupplierAddress.getStartPage(),searchSmtSupplierAddress.getPageSize());
        List<SmtSupplierAddress> list = smtSupplierAddressService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtSupplierAddress));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
