package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.sfc.PalletWorkScanDto;
import com.fantechs.common.base.general.dto.mes.sfc.RequestPalletWorkScanDto;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.mes.sfc.service.MesSfcPalletWorkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/mesSfcPalletWork")
@Api(tags = "栈板作业", basePath = "/mesSfcPalletWork")
public class MesSfcPalletWorkController {

    @Resource
    MesSfcPalletWorkService mesSfcPalletWorkService;

    @PostMapping("/palletWorkScanBarcode")
    @ApiOperation("栈板作业扫码")
    public ResponseEntity palletWorkScanBarcode(
            @ApiParam(value = "条码", required = true) @RequestBody RequestPalletWorkScanDto requestPalletWorkScanDto) {

        try {
            int i = mesSfcPalletWorkService.palletWorkScanBarcode(requestPalletWorkScanDto);
            return ControllerUtil.returnCRUD(i);
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @GetMapping("/palletWorkScan")
    @ApiOperation("栈板作业扫码列表")
    public ResponseEntity<List<PalletWorkScanDto>> palletWorkScan() {

        List<PalletWorkScanDto> palletWorkScanDtoList = mesSfcPalletWorkService.palletWorkScan();
        return ControllerUtil.returnDataSuccess(palletWorkScanDtoList, palletWorkScanDtoList.size());
    }

    @GetMapping("/findPalletCarton")
    @ApiOperation("获取栈板码绑定的箱码")
    public ResponseEntity<List<String>> findPalletCarton(
            @ApiParam(value = "栈板码", required = true) @RequestParam String palletCode) {

        List<String> cartonCodeList = mesSfcPalletWorkService.findPalletCarton(palletCode);
        return ControllerUtil.returnDataSuccess(cartonCodeList, cartonCodeList.size());
    }

    @PostMapping("/submitNoFullPallet")
    @ApiOperation("未满栈板提交")
    public ResponseEntity submitNoFullPallet(
            @ApiParam(value = "栈板码列表", required = true) @RequestBody List<String> palletCodeList) {

        try {
            int i = mesSfcPalletWorkService.submitNoFullPallet(palletCodeList);
            return ControllerUtil.returnCRUD(i);
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @GetMapping("/updatePalletType")
    @ApiOperation("判断是否可以修改栈板作业配置（同一工单改为同一料号）")
    public ResponseEntity<Boolean> updatePalletType(
            @ApiParam(value = "产线Id", required = true) @RequestParam Long proLineId,
            @ApiParam(value = "工位Id", required = true) @RequestParam Long stationId) {

        Boolean b = mesSfcPalletWorkService.updatePalletType(proLineId, stationId);
        return ControllerUtil.returnDataSuccess(b, 1);
    }
}
