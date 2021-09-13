package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.general.dto.restapi.WmsDataExportInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.WmsDataExportInnerJobOrderService;
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
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */
@RestController
@Api(tags = "上架作业单确认回写")
@RequestMapping("/WmsDataExportInnerJobOrder")
@Validated
public class WmsDataExportInnerJobOrderController {

    @Resource
    private WmsDataExportInnerJobOrderService wmsDataExportInnerJobOrderService;

    @ApiOperation("列表")
    @PostMapping("/findExportData")
    public ResponseEntity<List<WmsDataExportInnerJobOrderDto>> findExportData(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerJobOrder searchWmsInnerJobOrder) {
        searchWmsInnerJobOrder.setPageSize(10);
        Page<Object> page = PageHelper.startPage(searchWmsInnerJobOrder.getStartPage(),searchWmsInnerJobOrder.getPageSize());
        List<WmsDataExportInnerJobOrderDto> list = wmsDataExportInnerJobOrderService.findExportData(ControllerUtil.dynamicConditionByEntity(searchWmsInnerJobOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("入库单回传")
    @PostMapping("/writeDeliveryDetails")
    public ResponseEntity<String> writeDeliveryDetails(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= WmsInnerJobOrder.update.class) WmsInnerJobOrder wmsInnerJobOrder) {
        String result = wmsDataExportInnerJobOrderService.writeDeliveryDetails(wmsInnerJobOrder);
        return ControllerUtil.returnDataSuccess(result,1);
    }


}
