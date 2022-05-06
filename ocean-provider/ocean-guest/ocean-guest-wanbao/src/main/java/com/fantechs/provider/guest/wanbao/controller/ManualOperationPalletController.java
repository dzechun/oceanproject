package com.fantechs.provider.guest.wanbao.controller;

import com.fantechs.common.base.general.dto.mes.sfc.PalletWorkByManualOperationDto;
import com.fantechs.common.base.general.dto.mes.sfc.ScanByManualOperationDto;
import com.fantechs.common.base.general.dto.mes.sfc.StackingWorkByAutoDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoAutoStackingListDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDto;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.guest.wanbao.service.ManualOperationPalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "万宝-栈板作业（人工堆垛）控制器")
@RequestMapping("/manualOperation")
@Validated
public class ManualOperationPalletController {

    @Resource
    ManualOperationPalletService palletService;

    @PostMapping("/scanByManualOperation")
    @ApiOperation("堆码作业扫条码")
    public ResponseEntity<ScanByManualOperationDto> scanByManualOperation(
            @ApiParam(value = "条码", required = true) @RequestParam String barcode, @ApiParam(value = "条码", required = true) @RequestParam Long proLineId) {
        ScanByManualOperationDto scanByManualOperation = palletService.scanByManualOperation(barcode, proLineId);
        return ControllerUtil.returnDataSuccess(scanByManualOperation, 1);
    }

    @PostMapping("/scanStackingCode")
    @ApiOperation("堆码作业扫堆垛编码(人工)")
    public ResponseEntity<List<WanbaoStackingDto>> scanStackingCode(
            @ApiParam(value = "堆垛") @RequestParam(required = false) String stackingCode, @ApiParam(value = "产线ID", required = true) @RequestParam Long proLineId) {
        List<WanbaoStackingDto> list = palletService.scanStackingCode(stackingCode, proLineId);
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping("/workByManualOperation")
    @ApiOperation("堆码作业提交(人工)")
    public ResponseEntity workByManualOperation(
            @ApiParam(value = "条码", required = true) @RequestBody PalletWorkByManualOperationDto dto) {
        int i = palletService.workByManualOperation(dto);
        return ControllerUtil.returnCRUD(i);
    }

    @PostMapping("/scanStackingCodeByAuto")
    @ApiOperation("堆码作业扫堆垛编码(A线)")
    public ResponseEntity<List<WanbaoStackingDto>> scanStackingCodeByAuto(@ApiParam(value = "条码", required = true) @RequestBody StackingWorkByAutoDto dto) {
        int i = palletService.scanStackingCodeByAuto(dto);
        return ControllerUtil.returnCRUD(i);
    }

    @PostMapping("/findStackingByAuto")
    @ApiOperation("查找空闲并且有条码的堆垛(A线)")
    public ResponseEntity<List<WanbaoAutoStackingListDto>> findStackingByAuto(@ApiParam(value = "产线ID", required = true) @RequestParam Long proLineId) {
        List<WanbaoAutoStackingListDto> list = palletService.findStackingByAuto(proLineId);
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping("/workByAuto")
    @ApiOperation("堆码作业提交(A线)")
    public ResponseEntity workByAuto(
            @ApiParam(value = "条码", required = true) @RequestBody WanbaoAutoStackingListDto dto) {
        int i = palletService.workByAuto(dto);
        return ControllerUtil.returnCRUD(i);
    }

    @PostMapping("/changeStacking")
    @ApiOperation("切换堆垛(A线)")
    public ResponseEntity changeStacking(@ApiParam(value = "原堆垛ID", required = true) @RequestParam Long oldId, @ApiParam(value = "新堆垛ID", required = true) @RequestParam Long newId){
        int i = palletService.changeStacking(oldId, newId);
        return ControllerUtil.returnCRUD(i);
    }

}
