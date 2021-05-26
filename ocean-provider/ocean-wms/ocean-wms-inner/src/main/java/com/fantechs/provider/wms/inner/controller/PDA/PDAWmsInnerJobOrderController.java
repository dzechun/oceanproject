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
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    @ApiOperation("/PDA上架作业单列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerJobOrderDto>> findList(@RequestBody(required = false) SearchWmsInnerJobOrder searchWmsInnerJobOrder){
        List<Byte> bytes= new ArrayList<>();
        bytes.add((byte)3);
        bytes.add((byte)4);
        searchWmsInnerJobOrder.setOrderStatusList(bytes);
        List<WmsInnerJobOrderDto> list = wmsInnerJobOrderService.findList(searchWmsInnerJobOrder);
        return ControllerUtil.returnDataSuccess(list, StringUtils.isEmpty(list)?0:1);
    }

    @ApiOperation("/PDA上架作业明细列表")
    @PostMapping("/findDetList")
    public ResponseEntity<List<WmsInnerJobOrderDetDto>> findDetList(@RequestBody(required = false) SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet){
        List<Byte> bytes= new ArrayList<>();
        bytes.add((byte)3);
        bytes.add((byte)4);
        searchWmsInnerJobOrderDet.setOrderStatusList(bytes);
        List<WmsInnerJobOrderDetDto> list = wmsInnerJobOrderDetService.findList(searchWmsInnerJobOrderDet);
        return ControllerUtil.returnDataSuccess(list,StringUtils.isEmpty(list)?0:1);
    }

    @ApiOperation("PDA扫码库位返回数量")
    @PostMapping("/scanStorageBackQty")
    public ResponseEntity<WmsInnerJobOrderDet> scanStorageBackQty(@ApiParam(value = "库位编码")@RequestParam @NotNull(message = "库位编码不能为空") String storageCode,
                                                                  @ApiParam(value = "明细id")@RequestParam @NotNull(message = "明细唯一标识不能为空") Long jobOrderDetId){
        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderService.scanStorageBackQty(storageCode,jobOrderDetId);
        return ControllerUtil.returnDataSuccess(wmsInnerJobOrderDet,StringUtils.isEmpty(wmsInnerJobOrderDet)?0:1);
    }

    @ApiOperation("标签校验")
    @ApiIgnore
    @PostMapping("/checkBarcode")
    public ResponseEntity<String> checkBarcode(@ApiParam(value = "条码")@RequestParam String barCode){
        String id = wmsInnerJobOrderService.checkBarcode(barCode);
        return ControllerUtil.returnDataSuccess(id,StringUtils.isEmpty(id)?0:1);
    }

    @ApiOperation("单一确认")
    @ApiIgnore
    @PostMapping("/singleReceiving")
    public ResponseEntity singleReceiving(@RequestBody(required = true) List<WmsInnerJobOrderDet> wmsInPutawayOrderDets){
        return ControllerUtil.returnCRUD(wmsInnerJobOrderService.singleReceiving(wmsInPutawayOrderDets));
    }

    @ApiOperation("PDA拣货确认")
    @PostMapping("/pickingOrder")
    public ResponseEntity pickingOrder(@RequestParam @NotNull(message = "id不能为空") Long jobOrderDetId,String barCode){
        return ControllerUtil.returnCRUD(pickingOrderService.scanAffirmQty(jobOrderDetId,barCode));
    }

    @ApiOperation("PDA激活关闭栈板")
    @PostMapping("/activation")
    public ResponseEntity activation(@RequestParam Long jobOrderId){
        return ControllerUtil.returnCRUD(wmsInnerJobOrderService.activation(jobOrderId));
    }
}
