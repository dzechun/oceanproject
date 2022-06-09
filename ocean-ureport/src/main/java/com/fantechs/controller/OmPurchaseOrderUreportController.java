package com.fantechs.controller;

import com.fantechs.common.base.general.entity.ureport.OmPurchaseOrderUreport;
import com.fantechs.common.base.general.entity.ureport.search.SearchOmPurchaseOrderUreport;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.service.OmPurchaseOrderUreportService;
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
import java.util.Map;

/**
 * Created by lzw on 2021/11/23.
 */
@RestController
@Api(tags = "采购订单信息查询报表")
@RequestMapping("/omPurchaseOrderUreport")
@Validated
public class OmPurchaseOrderUreportController {

    @Resource
    private OmPurchaseOrderUreportService omPurchaseOrderUreportService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation("采购订单查询")
    @PostMapping("/findList")
    public ResponseEntity<List<OmPurchaseOrderUreport>> list(@ApiParam(value = "查询条件，请参考Model说明") @RequestBody(required = false) SearchOmPurchaseOrderUreport searchOmPurchaseOrderUreport) {
        Page<Object> page = PageHelper.startPage(searchOmPurchaseOrderUreport.getStartPage(), searchOmPurchaseOrderUreport.getPageSize());
        List<OmPurchaseOrderUreport> list = omPurchaseOrderUreportService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseOrderUreport));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }


    @PostMapping("/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void export(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchOmPurchaseOrderUreport searchOmPurchaseOrderUreport){
        List<OmPurchaseOrderUreport> list = omPurchaseOrderUreportService.findList(ControllerUtil.dynamicConditionByEntity(searchOmPurchaseOrderUreport));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "采购订单查询报表", "采购订单查询报表", "采购订单查询报表.xls", response);
    }
}
