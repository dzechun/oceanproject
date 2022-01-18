package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.ureport.SrmAsnOrder;
import com.fantechs.common.base.general.entity.ureport.search.SearchSrmAsnOrder;
import com.fantechs.common.base.general.entity.ureport.search.SearchSupplierUreport;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.service.AsnOrderFindUreportService;
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
@Api(tags = "ASN查询单查询报表")
@RequestMapping("/asnOrderFindUreport")
@Validated
public class AsnOrderFindUreportController {

    @Resource
    private AsnOrderFindUreportService asnOrderFindUreportService;

    @ApiOperation("ASN查询")
    @PostMapping("/findList")
    public ResponseEntity<List<SrmAsnOrder>> list(@ApiParam(value = "查询条件，请参考Model说明") @RequestBody(required = false) SearchSrmAsnOrder searchSrmAsnOrder) {
        Page<Object> page = PageHelper.startPage(searchSrmAsnOrder.getStartPage(), searchSrmAsnOrder.getPageSize());
        List<SrmAsnOrder> list = asnOrderFindUreportService.findList(ControllerUtil.dynamicConditionByEntity(searchSrmAsnOrder));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }


    @PostMapping("/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void export(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSupplierUreport searchSupplierUreport){
        List<SrmAsnOrder> list = asnOrderFindUreportService.findList(ControllerUtil.dynamicConditionByEntity(searchSupplierUreport));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "ANS查询信息报表", "ANS查询信息报表", SrmAsnOrder.class, "ANS查询信息报表.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
