package com.fantechs.provider.wms.inner.controller.PDA;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.PickingOrderService;
import com.fantechs.provider.wms.inner.service.WmsInnerJobOrderDetService;
import com.fantechs.provider.wms.inner.service.WmsInnerJobOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/5/7
 */
@RestController
@Api(tags = "PDA上架作业")
@RequestMapping("/PDAWmsInnerJobOrder")
@Validated
public class PDAWmsInnerJobOrderController {
    @Resource
    private WmsInnerJobOrderService wmsInnerJobOrderService;
    @Resource
    private WmsInnerJobOrderDetService wmsInnerJobOrderDetService;
    @Resource
    private PickingOrderService pickingOrderService;

    @ApiOperation("PDA上架作业单列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerJobOrderDto>> findList(@RequestBody(required = false) SearchWmsInnerJobOrder searchWmsInnerJobOrder){
        List<Byte> bytes= new ArrayList<>();
        bytes.add((byte)3);
        bytes.add((byte)4);
        //PDA拣货不展示已完成单据
        if(searchWmsInnerJobOrder.getJobOrderType()!=(byte)4){
            bytes.add((byte)6);
        }
        searchWmsInnerJobOrder.setOrderStatusList(bytes);
        searchWmsInnerJobOrder.setIsPallet((byte)1);
        List<WmsInnerJobOrderDto> list = wmsInnerJobOrderService.findList(searchWmsInnerJobOrder);
        return ControllerUtil.returnDataSuccess(list, StringUtils.isEmpty(list)?0:1);
    }

    @ApiOperation("PDA上架作业明细列表")
    @PostMapping("/findDetList")
    public ResponseEntity<List<WmsInnerJobOrderDetDto>> findDetList(@RequestBody(required = false) SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet){
        List<Byte> bytes= new ArrayList<>();
        bytes.add((byte)3);
        bytes.add((byte)4);
        searchWmsInnerJobOrderDet.setOrderStatusList(bytes);
        List<WmsInnerJobOrderDetDto> list = wmsInnerJobOrderDetService.findList(searchWmsInnerJobOrderDet);
        return ControllerUtil.returnDataSuccess(list,StringUtils.isEmpty(list)?0:1);
    }

    @ApiOperation("PDA扫码库位上架")
    @PostMapping("/scanStorageBackQty")
    public ResponseEntity<WmsInnerJobOrderDet> scanStorageBackQty(@ApiParam(value = "库位编码")@RequestParam @NotBlank(message = "库位编码不能为空") String storageCode,
                                                                  @ApiParam(value = "明细id")@RequestParam Long jobOrderDetId,
                                                                  @ApiParam(value = "确认数量")@RequestParam BigDecimal qty){
        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderService.scanStorageBackQty(storageCode,jobOrderDetId,qty);
        return ControllerUtil.returnDataSuccess(wmsInnerJobOrderDet,StringUtils.isEmpty(wmsInnerJobOrderDet)?0:1);
    }

    @ApiOperation("条码校验")
    @PostMapping("/checkBarcode")
    public ResponseEntity<Map<String,Object>> checkBarcode(@ApiParam(value = "条码")@RequestParam String barCode,
                                                   @ApiParam(value = "明细id")@RequestParam Long jobOrderDetId){
        Map<String,Object> qty = wmsInnerJobOrderService.checkBarcode(barCode,jobOrderDetId);
        return ControllerUtil.returnDataSuccess(qty,StringUtils.isEmpty(qty)?0:1);
    }

    @ApiOperation("单一确认")
    @ApiIgnore
    @PostMapping("/singleReceiving")
    public ResponseEntity singleReceiving(@RequestBody(required = true) List<WmsInnerJobOrderDet> wmsInPutawayOrderDets){
        return ControllerUtil.returnCRUD(wmsInnerJobOrderService.singleReceiving(wmsInPutawayOrderDets));
    }

    @ApiOperation("PDA拣货确认")
    @PostMapping("/pickingOrder")
    public ResponseEntity<WmsInnerJobOrderDet> pickingOrder(@ApiParam("条码")@RequestParam String barCode,
                                       @RequestParam @NotNull(message = "id不能为空") Long jobOrderDetId){
        WmsInnerJobOrderDet wmsInnerJobOrderDet = pickingOrderService.scanAffirmQty(barCode, jobOrderDetId);
        return ControllerUtil.returnDataSuccess(wmsInnerJobOrderDet,StringUtils.isEmpty(wmsInnerJobOrderDet)?1:0);
    }

    @ApiOperation("PDA激活关闭栈板")
    @PostMapping("/activation")
    public ResponseEntity activation(@RequestParam Long jobOrderId){
        return ControllerUtil.returnCRUD(wmsInnerJobOrderService.activation(jobOrderId));
    }
}
