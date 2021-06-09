package com.fantechs.provider.wms.inner.controller.PDA;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStockOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryDetService;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

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

    @ApiOperation("PDA盘点列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerStockOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerStockOrder searchWmsInnerStockOrder) {
        searchWmsInnerStockOrder.setPda(true);
        Page<Object> page = PageHelper.startPage(searchWmsInnerStockOrder.getStartPage(),searchWmsInnerStockOrder.getPageSize());
        List<WmsInnerStockOrderDto> list = wmsInnerStockOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStockOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("扫码条码")
    @PostMapping("/scanBarcode")
    public ResponseEntity<BigDecimal> scanBarcode(@ApiParam(value = "条码")@RequestParam String barcode){
        return ControllerUtil.returnDataSuccess(wmsInnerStockOrderService.scanBarcode(barcode),1);
    }

    @ApiOperation("PDA盘点确认")
    @PostMapping("/PdaAscertained")
    public ResponseEntity PdaAscertained(List<WmsInnerStockOrderDet> wmsInnerStockOrderDets){
        return ControllerUtil.returnCRUD(wmsInnerStockOrderService.PdaAscertained(wmsInnerStockOrderDets));
    }
}
