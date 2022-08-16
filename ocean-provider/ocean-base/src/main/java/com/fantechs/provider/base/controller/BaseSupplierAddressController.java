package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.entity.basic.BaseSupplierAddress;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplierAddress;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseSupplierAddressService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/01/05.
 */
@RestController
@Api(tags = "供应商与地址关联管理")
@RequestMapping("/baseSupplierAddress")
@Validated
public class BaseSupplierAddressController {

    @Resource
    private BaseSupplierAddressService baseSupplierAddressService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseSupplierAddress baseSupplierAddress) {
        return ControllerUtil.returnCRUD(baseSupplierAddressService.save(baseSupplierAddress));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseSupplierAddressService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= BaseSupplierAddress.update.class) BaseSupplierAddress baseSupplierAddress) {
        return ControllerUtil.returnCRUD(baseSupplierAddressService.update(baseSupplierAddress));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseSupplierAddress> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseSupplierAddress baseSupplierAddress = baseSupplierAddressService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseSupplierAddress,StringUtils.isEmpty(baseSupplierAddress)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSupplierAddress>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSupplierAddress searchBaseSupplierAddress) {
        Page<Object> page = PageHelper.startPage(searchBaseSupplierAddress.getStartPage(), searchBaseSupplierAddress.getPageSize());
        List<BaseSupplierAddress> list = baseSupplierAddressService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseSupplierAddress));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

}
