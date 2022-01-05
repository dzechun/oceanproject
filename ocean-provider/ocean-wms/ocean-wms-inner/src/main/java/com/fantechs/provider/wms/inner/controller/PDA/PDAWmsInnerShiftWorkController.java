package com.fantechs.provider.wms.inner.controller.PDA;

import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchPdaFindListDto;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.WmsInnerShiftWorkService;
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
 * 仓库作业-PDA库内移位作业管理
 */
@RestController
@Api(tags = "仓库作业-PDA库内移位作业管理")
@RequestMapping("/pdaWmsInnerShiftWork")
@Validated
public class PDAWmsInnerShiftWorkController {

    @Resource
    WmsInnerShiftWorkService wmsInnerShiftWorkService;

    @ApiOperation("PDA库内移位单列表")
    @PostMapping("/pdaFindList")
    public ResponseEntity<List<WmsInnerJobOrderDto>> pdaFindList(@ApiParam(value = "作业单号，非必填") @RequestBody SearchPdaFindListDto dto) {
        Page<Object> page = PageHelper.startPage(dto.getStartPage(), dto.getPageSize());
        List<WmsInnerJobOrderDto> list = wmsInnerShiftWorkService.pdaFindList(ControllerUtil.dynamicConditionByEntity(dto));
        return ControllerUtil.returnDataSuccess(list != null && list.size() > 0 ? list : new ArrayList<>(), (int) page.getTotal());
    }

    @ApiOperation("PDA库内移位单明细列表")
    @PostMapping("/pdaFindDetList")
    public ResponseEntity<List<WmsInnerJobOrderDetDto>> pdaFindDetList(@ApiParam(value = "作业单号，非必填", required = true) @RequestBody SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerJobOrderDet.getStartPage(), searchWmsInnerJobOrderDet.getPageSize());
        List<WmsInnerJobOrderDetDto> innerJobOrderDetDtos = wmsInnerShiftWorkService.pdaFindDetList(searchWmsInnerJobOrderDet);
        return ControllerUtil.returnDataSuccess(innerJobOrderDetDtos != null && innerJobOrderDetDtos.size() > 0 ? innerJobOrderDetDtos : new ArrayList<>(), (int) page.getTotal());
    }

    @ApiOperation("PDA库内移位拣货校验")
    @PostMapping("/checkShiftWorkBarcode")
    public ResponseEntity<CheckShiftWorkBarcodeRecordDto> checkShiftWorkBarcode(@ApiParam(value = "扫码校验DTO", required = true) @RequestBody CheckShiftWorkBarcodeDto dto) {
        CheckShiftWorkBarcodeRecordDto recordDto = wmsInnerShiftWorkService.checkShiftWorkBarcode(dto);
        return ControllerUtil.returnDataSuccess(recordDto, 1);
    }

    @ApiOperation("PDA库内移位拣货确认")
    @PostMapping("/saveShiftWorkDetBarcode")
    public ResponseEntity<String> saveShiftWorkDetBarcode(@ApiParam(value = "拣货确认实体", required = true) @RequestBody SaveShiftWorkDetDto dto) {
        String jobOrderId = wmsInnerShiftWorkService.saveShiftWorkDetBarcode(dto);
        return ControllerUtil.returnDataSuccess("成功", jobOrderId);
    }

    @ApiOperation("PDA库内移位上架确认")
    @PostMapping("/saveJobOrder")
    public ResponseEntity saveJobOrder(@ApiParam(value = "上架确认实体", required = true) @RequestBody SaveShiftJobOrderDto dto) {
        return ControllerUtil.returnCRUD(wmsInnerShiftWorkService.saveJobOrder(dto));
    }

    @ApiOperation("PDA批量移位-扫库位码")
    @PostMapping("/scanStorage")
    public ResponseEntity<StorageDto> scanStorage(@ApiParam(value = "批量移位实体", required = true) @RequestBody ScanStorageDto dto){
        return ControllerUtil.returnDataSuccess(wmsInnerShiftWorkService.scanStorage(dto), 1);
    }

    @ApiOperation("PDA批量移位")
    @PostMapping("/batchShiftWork")
    public ResponseEntity batchShiftWork(@ApiParam(value = "批量移位实体", required = true) @RequestBody BatchSiftWorkDto dto){
        return ControllerUtil.returnCRUD(wmsInnerShiftWorkService.batchShiftWork(dto));
    }
}
