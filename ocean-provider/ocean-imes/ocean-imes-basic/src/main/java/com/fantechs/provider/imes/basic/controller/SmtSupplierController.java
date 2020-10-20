package com.fantechs.provider.imes.basic.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtSupplier;
import com.fantechs.common.base.entity.basic.search.SearchSmtSupplier;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.service.SmtSupplierService;
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
 * Created by Mr.Lei on 2020/09/27.
 */
@RestController
@Api(tags = "供应商信息")
@RequestMapping("/smtSupplier")
@Validated
public class SmtSupplierController {

    @Autowired
    private SmtSupplierService smtSupplierService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：supplierCode、supplierCode",required = true)@RequestBody @Validated SmtSupplier smtSupplier) {
        return ControllerUtil.returnCRUD(smtSupplierService.save(smtSupplier));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtSupplierService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value = SmtSupplier.update.class) SmtSupplier smtSupplier) {
        return ControllerUtil.returnCRUD(smtSupplierService.update(smtSupplier));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtSupplier> detail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id) {
        SmtSupplier  smtSupplier = smtSupplierService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(smtSupplier,StringUtils.isEmpty(smtSupplier)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtSupplier>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtSupplier searchSmtSupplier) {
        Page<Object> page = PageHelper.startPage(searchSmtSupplier.getStartPage(),searchSmtSupplier.getPageSize());
        List<SmtSupplier> list = smtSupplierService.findList(searchSmtSupplier);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出供应商excel",notes = "导出供应商excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtSupplier searchSmtSupplier){
    List<SmtSupplier> list = smtSupplierService.findList(searchSmtSupplier);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "供应商信息导出", "供应商信息", SmtSupplier.class, "供应商.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
