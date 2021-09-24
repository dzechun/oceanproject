package com.fantechs.provider.guest.eng.controller;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.guest.eng.service.EngReportStockOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */
@RestController
@Api(tags = "返写盘点单")
@RequestMapping("/reportStockOrder")
@Validated
public class EngReportStockOrderController {

    @Resource
    private EngReportStockOrderService engReportStockOrderService;

    @ApiOperation("返写盘点单")
    @PostMapping("/writeQty")
    public ResponseEntity<String> writePackingLists(@ApiParam(value = "对象，Id必传",required = true)@RequestBody WmsInnerStockOrder wmsInnerStockOrder) {
        String result = engReportStockOrderService.reportStockOrder(wmsInnerStockOrder);
        return ControllerUtil.returnDataSuccess(result,1);
    }


}
