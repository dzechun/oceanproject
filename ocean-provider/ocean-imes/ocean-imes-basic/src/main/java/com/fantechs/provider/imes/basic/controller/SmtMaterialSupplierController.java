package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.dto.basic.SmtMaterialSupplierDto;
import com.fantechs.common.base.entity.basic.SmtMaterialSupplier;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterialSupplier;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtMaterialSupplierService;
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
 * Created by wcz on 2020/11/03.
 */
@RestController
@Api(tags = "物料编码关联客户料号")
@RequestMapping("/smtMaterialSupplier")
@Validated
public class SmtMaterialSupplierController {

    @Autowired
    private SmtMaterialSupplierService smtMaterialSupplierService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：materialSupplierId,materialId,materialSupplierCode,supplierId",required = true)@RequestBody @Validated SmtMaterialSupplier smtMaterialSupplier) {
        return ControllerUtil.returnCRUD(smtMaterialSupplierService.save(smtMaterialSupplier));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtMaterialSupplierService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtMaterialSupplier.update.class) SmtMaterialSupplier smtMaterialSupplier) {
        return ControllerUtil.returnCRUD(smtMaterialSupplierService.update(smtMaterialSupplier));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtMaterialSupplier> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtMaterialSupplier  smtMaterialSupplier = smtMaterialSupplierService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtMaterialSupplier,StringUtils.isEmpty(smtMaterialSupplier)?0:1);
    }

    @ApiOperation("物料编码关联客户料号列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtMaterialSupplierDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtMaterialSupplier searchSmtMaterialSupplier) {
        Page<Object> page = PageHelper.startPage(searchSmtMaterialSupplier.getStartPage(),searchSmtMaterialSupplier.getPageSize());
        List<SmtMaterialSupplierDto> list = smtMaterialSupplierService.findList(searchSmtMaterialSupplier);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
                            @RequestBody(required = false) SearchSmtMaterialSupplier searchSmtMaterialSupplier){
    List<SmtMaterialSupplierDto> list = smtMaterialSupplierService.findList(searchSmtMaterialSupplier);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出物料编码关联客户料号信息", "物料编码关联客户料号信息", SmtMaterialSupplierDto.class, "物料编码关联客户料号.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
