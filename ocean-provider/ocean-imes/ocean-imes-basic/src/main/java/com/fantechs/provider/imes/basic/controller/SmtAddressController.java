package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.dto.basic.SmtAddressDto;
import com.fantechs.common.base.entity.basic.SmtAddress;
import com.fantechs.common.base.entity.basic.search.SearchSmtAddress;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtAddressService;
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
@Api(tags = "地址信息管理")
@RequestMapping("/smtAddress")
@Validated
public class SmtAddressController {

    @Autowired
    private SmtAddressService smtAddressService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtAddress smtAddress) {
        return ControllerUtil.returnCRUD(smtAddressService.save(smtAddress));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtAddressService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtAddress.update.class) SmtAddress smtAddress) {
        return ControllerUtil.returnCRUD(smtAddressService.update(smtAddress));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtAddress> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtAddress  smtAddress = smtAddressService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtAddress,StringUtils.isEmpty(smtAddress)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtAddressDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtAddress searchSmtAddress) {
        Page<Object> page = PageHelper.startPage(searchSmtAddress.getStartPage(),searchSmtAddress.getPageSize());
        List<SmtAddressDto> list = smtAddressService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtAddress));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtAddress searchSmtAddress){
    List<SmtAddressDto> list = smtAddressService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtAddress));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtAddress信息", SmtAddress.class, "SmtAddress.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
