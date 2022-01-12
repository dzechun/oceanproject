package com.fantechs.provider.wms.inner.controller.PDA;

import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStockOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStockOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryDetService;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderDetService;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/6/4
 */
@RestController
@Api(tags = "PDA盘点")
@RequestMapping("/PDAWmsInnerStock")
@Validated
public class PDAWmsInnerStockController {

    @Resource
    private WmsInnerStockOrderService wmsInnerStockOrderService;
    @Resource
    private WmsInnerStockOrderDetService wmsInnerStockOrderDetService;

    @ApiOperation("PDA盘点列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerStockOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerStockOrder searchWmsInnerStockOrder) {
        searchWmsInnerStockOrder.setPda(true);
        Page<Object> page = PageHelper.startPage(searchWmsInnerStockOrder.getStartPage(),searchWmsInnerStockOrder.getPageSize());
        List<WmsInnerStockOrderDto> list = wmsInnerStockOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStockOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("PDA查看盘点明细")
    @PostMapping("/findDetList")
    public ResponseEntity<List<WmsInnerStockOrderDetDto>> findDetList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerStockOrderDet searchWmsInventoryVerificationDet){
        Page<Object> page = PageHelper.startPage(searchWmsInventoryVerificationDet.getStartPage(),searchWmsInventoryVerificationDet.getPageSize());
        List<WmsInnerStockOrderDetDto> list = wmsInnerStockOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInventoryVerificationDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("PDA扫描条码")
    @PostMapping("/scanBarcode")
    public ResponseEntity<BarcodeResultDto> scanBarcode(@ApiParam(value = "盘点明细ID")@RequestParam Long stockOrderDetId,@ApiParam(value = "条码")@RequestParam String barcode){
        return ControllerUtil.returnDataSuccess(wmsInnerStockOrderService.scanBarcode(stockOrderDetId,barcode),1);
    }

    @ApiOperation("PDA盘点增补")
    @PostMapping("/addInnerStockDet")
    public ResponseEntity addInnerStockDet(@RequestBody(required = true) List<AddInnerStockDetDto> addDetList){
        return ControllerUtil.returnCRUD(wmsInnerStockOrderService.addInnerStockDet(addDetList));
    }

    @ApiOperation("PDA盘点提交")
    @PostMapping("/pdaCommit")
    public ResponseEntity pdaCommit(@ApiParam(value = "盘点明细ID")@RequestParam Long stockOrderDetId,@RequestBody(required = true) List<CommitInnerStockBarcodeDto> barcodeList){
        return ControllerUtil.returnCRUD(wmsInnerStockOrderService.pdaCommit(stockOrderDetId,barcodeList));
    }

    @ApiOperation("PDA盘点确认")
    @PostMapping("/pdaConfirm")
    public ResponseEntity pdaConfirm(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids){
        return ControllerUtil.returnCRUD(wmsInnerStockOrderService.pdaConfirm(ids));
    }
}
