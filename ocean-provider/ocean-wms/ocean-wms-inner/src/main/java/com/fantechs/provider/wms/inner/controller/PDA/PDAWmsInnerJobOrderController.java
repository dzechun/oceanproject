package com.fantechs.provider.wms.inner.controller.PDA;

import com.fantechs.common.base.general.dto.wms.inner.SaveHaveInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.SaveInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
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
        searchWmsInnerJobOrder.setOrderStatusList(bytes);
        List<WmsInnerJobOrderDto> list = wmsInnerJobOrderService.findList(searchWmsInnerJobOrder);
        return ControllerUtil.returnDataSuccess(list, StringUtils.isEmpty(list)?0:1);
    }

    @ApiOperation("PDA上架作业明细列表")
    @PostMapping("/findDetList")
    public ResponseEntity<List<WmsInnerJobOrderDetDto>> findDetList(@RequestBody(required = false) SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet){
//        List<Byte> bytes= new ArrayList<>();
//        bytes.add((byte)3);
//        searchWmsInnerJobOrderDet.setLineStatusList(bytes);
        List<WmsInnerJobOrderDetDto> list = wmsInnerJobOrderDetService.findList(searchWmsInnerJobOrderDet);
        return ControllerUtil.returnDataSuccess(list,StringUtils.isEmpty(list)?0:1);
    }

    @ApiOperation("先单后作业 检验条码")
    @PostMapping("/checkBarcodeHaveOrder")
    public ResponseEntity<Map<String,Object>> checkBarcodeHaveOrder(@ApiParam(value = "是否系统条码(0 否 1 是)")@RequestParam @NotBlank(message = "是否系统条码不能为空") String ifSysBarcode,
                                                                    @ApiParam(value = "作业单主表id")@RequestParam Long orderId,
                                                                    @ApiParam(value = "明细ID")@RequestParam Long orderDetId,
                                                                    @ApiParam(value = "条码")@RequestParam String barCode){
        Map<String,Object> qty = wmsInnerJobOrderService.checkBarcodeHaveOrder(ifSysBarcode,orderId,orderDetId,barCode);
        return ControllerUtil.returnDataSuccess(qty,StringUtils.isEmpty(qty)?0:1);
    }

    @ApiOperation("PDA先单后作业提交")
    @PostMapping("/saveHaveInnerJobOrder")
    public ResponseEntity<WmsInnerJobOrderDet> saveHaveInnerJobOrder(@RequestBody(required = true) List<SaveHaveInnerJobOrderDto> list){
        WmsInnerJobOrderDet wmsInnerJobOrderDet=wmsInnerJobOrderService.saveHaveInnerJobOrder(list);
        return ControllerUtil.returnDataSuccess(wmsInnerJobOrderDet,StringUtils.isEmpty(wmsInnerJobOrderDet)?0:1);
    }

    @ApiOperation("PDA先作业后单 检验条码")
    @PostMapping("/checkBarcodeNotOrder")
    public ResponseEntity<Map<String,Object>> checkBarcodeNotOrder(@ApiParam(value = "是否系统条码(0 否 1 是)")@RequestParam @NotBlank(message = "是否系统条码不能为空") String ifSysBarcode,
                                                                    @ApiParam(value = "条码")@RequestParam String barCode){
        Map<String,Object> qty = wmsInnerJobOrderService.checkBarcodeNotOrder(ifSysBarcode,barCode);
        return ControllerUtil.returnDataSuccess(qty,StringUtils.isEmpty(qty)?0:1);
    }

    @ApiOperation("PDA先作业后单 提交")
    @PostMapping("/saveInnerJobOrder")
    public ResponseEntity<WmsInnerJobOrder> saveInnerJobOrder(@RequestBody(required = true) List<SaveInnerJobOrderDto> list){
        WmsInnerJobOrder wmsInnerJobOrder=wmsInnerJobOrderService.saveInnerJobOrder(list);
        return ControllerUtil.returnDataSuccess(wmsInnerJobOrder,StringUtils.isEmpty(wmsInnerJobOrder)?0:1);
    }

    @ApiOperation(" PDA先作业后单 提交完成")
    @PostMapping("/updateInnerJobOrderFinish")
    public ResponseEntity updateInnerJobOrderFinish(@ApiParam(value = "对象ID",required = true) Long jobOrderId){
        return ControllerUtil.returnCRUD(wmsInnerJobOrderService.updateInnerJobOrderFinish(jobOrderId));
    }

    @ApiOperation(" PDA删除已扫描条码")
    @PostMapping("/updateBarcodeStatus")
    public ResponseEntity updateBarcodeStatus(@ApiParam(value = "条码",required = true) @RequestParam @NotBlank(message="barcode 不能为空") String barcode){
        return ControllerUtil.returnCRUD(wmsInnerJobOrderService.updateBarcodeStatus(barcode));
    }

    /*@ApiOperation("条码校验")
    @PostMapping("/checkBarcode")
    public ResponseEntity<Map<String,Object>> checkBarcode(@RequestBody Map<String ,Object> map){
        String barCode = map.get("barCode").toString();
        Long jobOrderDetId = Long.parseLong(map.get("jobOrderDetId").toString());
        Map<String,Object> qty = wmsInnerJobOrderService.checkBarcode(barCode,jobOrderDetId);
        return ControllerUtil.returnDataSuccess(qty,StringUtils.isEmpty(qty)?0:1);
    }

    @ApiOperation("单一确认")
    @ApiIgnore
    @PostMapping("/singleReceiving")
    public ResponseEntity singleReceiving(@RequestBody(required = true) List<WmsInnerJobOrderDet> wmsInPutawayOrderDets){
        return ControllerUtil.returnCRUD(wmsInnerJobOrderService.singleReceiving(wmsInPutawayOrderDets));
    }*/

    /**
     * ========================================拣货========================================
     */

    @ApiOperation("PDA拣货确认/提交")
    @PostMapping("/pickingOrder")
    public ResponseEntity<WmsInnerJobOrderDet> pickingOrder(@ApiParam("条码，多个条码用逗号隔开")@RequestParam String barCode,
                                                            @RequestParam @NotNull(message = "id不能为空") Long jobOrderDetId,
                                                            @RequestParam String storageCode,@RequestParam BigDecimal qty){
        WmsInnerJobOrderDet wmsInnerJobOrderDet = pickingOrderService.scanAffirmQty(barCode,storageCode,qty, jobOrderDetId);
        return ControllerUtil.returnDataSuccess(wmsInnerJobOrderDet,StringUtils.isEmpty(wmsInnerJobOrderDet)?1:0);
    }
    @ApiOperation("/PDA拣货作业条码扫码校验")
    @PostMapping("/pickCheckBarcode")
    public ResponseEntity<Map<String,Object>> checkBarcodeToPick(@ApiParam(value = "条码")@RequestParam String barCode,
                                                                 @ApiParam(value = "明细id")@RequestParam Long jobOrderDetId){
        Map<String,Object> qty = pickingOrderService.checkBarcode(barCode,jobOrderDetId);
        return ControllerUtil.returnDataSuccess(qty,StringUtils.isEmpty(qty)?0:1);
    }
}
