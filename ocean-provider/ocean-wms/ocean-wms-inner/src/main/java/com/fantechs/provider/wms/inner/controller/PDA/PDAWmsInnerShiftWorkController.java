package com.fantechs.provider.wms.inner.controller.PDA;

import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.inner.service.WmsInnerShiftWorkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    public ResponseEntity<List<WmsInnerJobOrderDto>> pdaFindList(@ApiParam(value = "作业单号，非必填") @RequestParam(required = false) String jobOrderCode){
        List<WmsInnerJobOrderDto> innerJobOrderDtos = wmsInnerShiftWorkService.pdaFindList(jobOrderCode);
        return ControllerUtil.returnDataSuccess(innerJobOrderDtos, innerJobOrderDtos.size());
    }

    @ApiOperation("PDA库内移位单明细列表")
    @PostMapping("/pdaFindDetList")
    public ResponseEntity<List<WmsInnerJobOrderDetDto>> pdaFindDetList(@ApiParam(value = "作业单号，非必填", required = true) @RequestParam Long jobOrderId){
        List<WmsInnerJobOrderDetDto> innerJobOrderDetDtos = wmsInnerShiftWorkService.pdaFindDetList(jobOrderId);
        return ControllerUtil.returnDataSuccess(innerJobOrderDetDtos, innerJobOrderDetDtos.size());
    }

    @ApiOperation("PDA库内移位拣货校验")
    @PostMapping("/checkShiftWorkBarcode")
    public ResponseEntity<WmsInnerInventoryDetDto> checkShiftWorkBarcode(@ApiParam(value = "扫码校验DTO", required = true) @RequestBody CheckShiftWorkBarcodeDto dto){
        WmsInnerInventoryDetDto inventoryDetDto = wmsInnerShiftWorkService.checkShiftWorkBarcode(dto);
        return ControllerUtil.returnDataSuccess(inventoryDetDto, 1);
    }

    @ApiOperation("PDA库内移位拣货确认")
    @PostMapping("/saveShiftWorkDetBarcode")
    public ResponseEntity<String> saveShiftWorkDetBarcode(@ApiParam(value = "拣货确认实体", required = true) @RequestBody SaveShiftWorkDetDto dto){
        String jobOrderId = wmsInnerShiftWorkService.saveShiftWorkDetBarcode(dto);
        return ControllerUtil.returnDataSuccess("成功", jobOrderId);
    }

    @ApiOperation("PDA库内移位上架确认")
    @PostMapping("/saveJobOrder")
    public ResponseEntity saveJobOrder(@ApiParam(value = "上架确认实体", required = true) @RequestBody SaveShiftJobOrderDto dto){
        return ControllerUtil.returnCRUD(wmsInnerShiftWorkService.saveJobOrder(dto));
    }
}
