package com.fantechs.provider.wms.inner.controller.PDA;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStockOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStockOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.WmsInnerBarcodeOperationService;
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
import java.util.List;
import java.util.Map;

/**
 * @Author
 * @Date 2021/6/4
 */
@RestController
@Api(tags = "PDA条码替换报废控制器")
@RequestMapping("/PDAWmsInnerBarcodeOperation")
@Validated
public class PDAWmsInnerBarcodeOperationController {

    @Resource
    private WmsInnerBarcodeOperationService wmsInnerBarcodeOperationService;

    @ApiOperation("扫描条码")
    @PostMapping("/scanBarcode")
    public ResponseEntity<WmsInnerInventoryDetDto> scanBarcode(@ApiParam(value = "条码")@RequestParam String barcode){
        return ControllerUtil.returnDataSuccess(wmsInnerBarcodeOperationService.scanBarcode(barcode),1);
    }

    @ApiOperation("扫描替换条码")
    @PostMapping("/scanReplaceBarcode")
    public ResponseEntity<WmsInnerInventoryDetDto> scanReplaceBarcode(@ApiParam(value = "条码")@RequestParam String barcode,@ApiParam(value = "替换条码")@RequestParam String replaceBarcode){
        return ControllerUtil.returnDataSuccess(wmsInnerBarcodeOperationService.scanReplaceBarcode(barcode,replaceBarcode),1);
    }

    @ApiOperation("替换条码提交")
    @PostMapping("/replaceCommit")
    public ResponseEntity replaceCommit(@ApiParam(value = "厂内条码")@RequestParam String barcode,@ApiParam(value = "替换厂内条码")@RequestParam String replaceBarcode){
        return ControllerUtil.returnCRUD(wmsInnerBarcodeOperationService.replaceCommit(barcode,replaceBarcode));
    }

    @ApiOperation("扫描报废条码")
    @PostMapping("/scanCrapBarcode")
    public ResponseEntity<WmsInnerInventoryDetDto> scanCrapBarcode(@ApiParam(value = "条码")@RequestParam String barcode){
        return ControllerUtil.returnDataSuccess(wmsInnerBarcodeOperationService.scanCrapBarcode(barcode),1);
    }

    @ApiOperation("报废条码提交")
    @PostMapping("/crapCommit")
    public ResponseEntity crapCommit(@ApiParam(value = "厂内条码")@RequestParam String barcode){
        return ControllerUtil.returnCRUD(wmsInnerBarcodeOperationService.crapCommit(barcode));
    }

}
