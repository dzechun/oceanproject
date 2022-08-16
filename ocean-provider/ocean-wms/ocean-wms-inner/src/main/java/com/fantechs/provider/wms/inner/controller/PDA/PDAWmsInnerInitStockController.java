package com.fantechs.provider.wms.inner.controller.PDA;

import com.fantechs.common.base.general.dto.wms.inner.InitStockCheckBarCode;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInitStockDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInitStockDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStockBarcode;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStockDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInitStock;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInitStockBarcode;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInitStockDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.WmsInnerInitStockBarcodeService;
import com.fantechs.provider.wms.inner.service.WmsInnerInitStockDetService;
import com.fantechs.provider.wms.inner.service.WmsInnerInitStockService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/12/1
 */
@RestController
@Api(tags = "PDA初始化盘点")
@RequestMapping("/PDAInitStock")
@Validated
public class PDAWmsInnerInitStockController {

    @Resource
    private WmsInnerInitStockService wmsInnerInitStockService;
    @Resource
    private WmsInnerInitStockDetService wmsInnerInitStockDetService;
    @Resource
    private WmsInnerInitStockBarcodeService wmsInnerInitStockBarcodeService;

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerInitStockDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInitStock searchWmsInnerInitStock) {
        List<Byte> byteList = new ArrayList<>();
        byteList.add((byte)1);
        byteList.add((byte)2);
        searchWmsInnerInitStock.setOrderStatusList(byteList);
        Page<Object> page = PageHelper.startPage(searchWmsInnerInitStock.getStartPage(),searchWmsInnerInitStock.getPageSize());
        List<WmsInnerInitStockDto> list = wmsInnerInitStockService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInitStock));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表")
    @PostMapping("/findListDet")
    public ResponseEntity<List<WmsInnerInitStockDetDto>> findListDet(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInitStockDet searchWmsInnerInitStockDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerInitStockDet.getStartPage(),searchWmsInnerInitStockDet.getPageSize());
        List<WmsInnerInitStockDetDto> list = wmsInnerInitStockDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInitStockDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表")
    @PostMapping("/findListDetBarCode")
    public ResponseEntity<List<WmsInnerInitStockBarcode>> findListDetBarCode(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInitStockBarcode searchWmsInnerInitStockBarcode) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerInitStockBarcode.getStartPage(),searchWmsInnerInitStockBarcode.getPageSize());
        List<WmsInnerInitStockBarcode> list = wmsInnerInitStockBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInitStockBarcode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("条码检验")
    @PostMapping("/checkBarCode")
    public ResponseEntity<InitStockCheckBarCode> checkBarCode(@RequestParam String barCode){
        return ControllerUtil.returnDataSuccess(wmsInnerInitStockService.checkBarCode(barCode),1 );
    }

    @ApiOperation("提交")
    @PostMapping("/save")
    public ResponseEntity<WmsInnerInitStockDet> save(@RequestBody WmsInnerInitStockDet wmsInnerInitStockDet){
        return ControllerUtil.returnDataSuccess(wmsInnerInitStockDetService.commit(wmsInnerInitStockDet),1);
    }

    @ApiOperation("完成")
    @PostMapping("/finish")
    public ResponseEntity finish(@RequestParam Long initStockId){
        return ControllerUtil.returnCRUD(wmsInnerInitStockService.finish(initStockId));
    }

    @ApiOperation("条码明细删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@RequestParam Long initStockBarcodeId){
        return ControllerUtil.returnCRUD(wmsInnerInitStockService.deleteBarCode(initStockBarcodeId));
    }
}
