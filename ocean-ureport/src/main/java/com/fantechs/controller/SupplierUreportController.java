package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.general.entity.ureport.BaseSupplierInfo;
import com.fantechs.common.base.general.entity.ureport.search.SearchSupplierUreport;
import com.fantechs.service.SupplierUreportService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by lzw on 2021/11/23.
 */
@RestController
@Api(tags = "供应商信息报表")
@RequestMapping("/SupplierUreport")
@Validated
public class SupplierUreportController {

    @Resource
    private SupplierUreportService supplierUreportService;

    @ApiOperation("供应商信息查询")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseSupplierInfo>> list(@ApiParam(value = "查询条件，请参考Model说明") @RequestBody(required = false) SearchSupplierUreport searchSupplierUreport) {
        Page<Object> page = PageHelper.startPage(searchSupplierUreport.getStartPage(), searchSupplierUreport.getPageSize());
        List<BaseSupplierInfo> list = supplierUreportService.findList(ControllerUtil.dynamicConditionByEntity(searchSupplierUreport));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }


    @PostMapping("/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void export(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSupplierUreport searchSupplierUreport){
        List<BaseSupplierInfo> list = supplierUreportService.findList(ControllerUtil.dynamicConditionByEntity(searchSupplierUreport));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "供应商信息", "供应商信息", BaseSupplierInfo.class, "供应商信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}