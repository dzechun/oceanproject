package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
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
    public ResponseEntity<PalletWorkScanDto> palletWorkScanBarcode(
            @ApiParam(value = "条码", required = true) @RequestBody RequestPalletWorkScanDto requestPalletWorkScanDto) {

        try {
            PalletWorkScanDto palletWorkScanDto = mesSfcPalletWorkService.palletWorkScanBarcode(requestPalletWorkScanDto);
            return ControllerUtil.returnDataSuccess(palletWorkScanDto, 1);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizErrorException(e);
        }
    }

    @GetMapping("/palletWorkScan")
    @ApiOperation("栈板作业未关闭栈板信息列表")
    public ResponseEntity<List<PalletWorkScanDto>> palletWorkScan(@ApiParam(value = "工位Id", required = true) @RequestParam Long stationId) {

        List<PalletWorkScanDto> palletWorkScanDtoList = mesSfcPalletWorkService.palletWorkScan(stationId);
        return ControllerUtil.returnDataSuccess(palletWorkScanDtoList, palletWorkScanDtoList.size());
    }

    @GetMapping("/findPalletCarton")
    @ApiOperation("获取栈板绑定的箱码列表")
    public ResponseEntity<List<String>> findPalletCarton(
            @ApiParam(value = "产品栈板ID", required = true) @RequestParam Long productPalletId) {

        List<String> cartonCodeList = mesSfcPalletWorkService.findPalletCarton(productPalletId);
        return ControllerUtil.returnDataSuccess(cartonCodeList, cartonCodeList.size());
    }

    @PostMapping("/submitNoFullPallet")
    @ApiOperation("未满栈板提交")
    public ResponseEntity submitNoFullPallet(
            @ApiParam(value = "栈板表ID列表", required = true) @RequestBody List<Long> palletIdList) {

        try {
            int i = mesSfcPalletWorkService.submitNoFullPallet(palletIdList);
            return ControllerUtil.returnCRUD(i);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BizErrorException(e);
        }
    }

    @GetMapping("/updatePalletType")
    @ApiOperation("判断是否可以修改栈板作业配置")
    public ResponseEntity<Boolean> updatePalletType(@ApiParam(value = "工位Id", required = true) @RequestParam Long stationId) {

        Boolean b = mesSfcPalletWorkService.updatePalletType(stationId);
        return ControllerUtil.returnDataSuccess(b, 1);
    }
}
