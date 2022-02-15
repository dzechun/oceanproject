package com.fantechs.provider.guest.wanbao.controller;

import com.fantechs.common.base.general.dto.mes.sfc.PalletWorkByManualOperationDto;
import com.fantechs.common.base.general.dto.mes.sfc.ScanByManualOperationDto;
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
    @ApiOperation("栈板作业扫码(人工)")
    public ResponseEntity<ScanByManualOperationDto> scanByManualOperation(
            @ApiParam(value = "条码", required = true) @RequestParam String barcode, @ApiParam(value = "条码", required = true) @RequestParam Long proLineId) {
        ScanByManualOperationDto scanByManualOperation = palletService.scanByManualOperation(barcode, proLineId);
        return ControllerUtil.returnDataSuccess(scanByManualOperation, 1);
    }

    @PostMapping("/scanStackingCode")
    @ApiOperation("栈板作业扫堆垛编码(人工)")
    public ResponseEntity<List<WanbaoStackingDto>> scanStackingCode(
            @ApiParam(value = "堆垛") @RequestParam(required = false) String stackingCode, @ApiParam(value = "产线ID", required = true) @RequestParam Long proLineId) {
        List<WanbaoStackingDto> list = palletService.scanStackingCode(stackingCode, proLineId);
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping("/workByManualOperation")
    @ApiOperation("栈板作业提交(人工)")
    public ResponseEntity workByManualOperation(
            @ApiParam(value = "条码", required = true) @RequestBody PalletWorkByManualOperationDto dto) {
        int i = palletService.workByManualOperation(dto);
        return ControllerUtil.returnCRUD(i);
    }
}
