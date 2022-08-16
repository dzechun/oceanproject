package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.PickingOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * 拣货作业
 * @Author mr.lei
 * @Date 2021/5/10
 */
@RestController
@Api(tags = "拣货作业")
@RequestMapping("/pickingOrder")
@Validated
public class PickingOrderController {

    @Resource
    private PickingOrderService pickingOrderService;

    @ApiOperation("自动分配")
    @PostMapping("/autoDistribution")
    public ResponseEntity autoDistribution(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids){
        return ControllerUtil.returnCRUD(pickingOrderService.autoDistribution(ids));
    }

    @ApiOperation("手动分配")
    @PostMapping("/handDistribution")
    public ResponseEntity handDistribution(@RequestBody List<WmsInnerJobOrderDet> list){
        return ControllerUtil.returnCRUD(pickingOrderService.handDistribution(list));
    }

    @ApiOperation("取消分配")
    @PostMapping("/cancelDistribution")
    public ResponseEntity cancelDistribution(@RequestParam String ids){
        return ControllerUtil.returnCRUD(pickingOrderService.cancelDistribution(ids));
    }

    @ApiOperation("整单确认")
    @PostMapping("/allReceiving")
    public ResponseEntity allReceiving(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids){
        return ControllerUtil.returnCRUD(pickingOrderService.allReceiving(ids));
    }

    @ApiOperation("单一确认")
    @PostMapping("/singleReceiving")
    public ResponseEntity singleReceiving(@RequestBody(required = true) WmsInnerJobOrderDet wmsInnerJobOrderDet){
        List<WmsInnerJobOrderDet> list = new ArrayList<>();
        list.add(wmsInnerJobOrderDet);
        return ControllerUtil.returnCRUD(pickingOrderService.singleReceiving(list));
    }

    @ApiOperation("装车拣货单列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerJobOrderDto>> findList(@RequestBody SearchWmsInnerJobOrder searchWmsInnerJobOrder){
        searchWmsInnerJobOrder.setIsPick(true);
        List<WmsInnerJobOrderDto> list = pickingOrderService.findList(searchWmsInnerJobOrder);
        return ControllerUtil.returnDataSuccess(list, StringUtils.isEmpty(list)?0:1);
    }

    @PostMapping("/retrographyStatus")
    public ResponseEntity retrographyStatus(@RequestBody WmsInnerJobOrderDet wmsInnerJobOrderDet){
        return ControllerUtil.returnCRUD(pickingOrderService.retrographyStatus(wmsInnerJobOrderDet));
    }

    @ApiOperation("调拨出库单快捷发运")
    @PostMapping("/autoOutOrder")
    public ResponseEntity autoOutOrder(@RequestParam Long outDeliveryOrderId,@RequestParam Byte orderTypeId){
        return ControllerUtil.returnCRUD(pickingOrderService.autoOutOrder(outDeliveryOrderId,orderTypeId));
    }

    @PostMapping("/sealOrder")
    @ApiOperation("封单")
    public ResponseEntity sealOrder(@RequestBody(required = false) List<Long> outDeliveryOrderIds,@RequestParam(required = false) Byte type){
        return ControllerUtil.returnCRUD(pickingOrderService.sealOrder(outDeliveryOrderIds,type));
    }

    @GetMapping("/AutoSealOrder")
    @ApiOperation("封单")
    public ResponseEntity AutoSealOrder(@RequestParam(required = false) Byte type){
        return ControllerUtil.returnCRUD(pickingOrderService.sealOrder(null,type));
    }
}
