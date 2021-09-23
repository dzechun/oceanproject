package com.fantechs.provider.guest.eng.controller;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.guest.eng.service.EngReportDeliveryOrderOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */
@RestController
@Api(tags = "出库回传接口")
@RequestMapping("/reportDeliveryOrderOrder")
@Validated
public class EngReportDeliveryOrderOrderController {

    @Resource
    private EngReportDeliveryOrderOrderService engReportDeliveryOrderOrderService;

    @ApiOperation("返写领料出库")
    @PostMapping("/writeQty")
    public ResponseEntity<String> writePackingLists(WmsInnerJobOrder wmsInnerJobOrder) {
        String result = engReportDeliveryOrderOrderService.reportDeliveryOrderOrder(wmsInnerJobOrder);
        return ControllerUtil.returnDataSuccess(result,1);
    }


}
