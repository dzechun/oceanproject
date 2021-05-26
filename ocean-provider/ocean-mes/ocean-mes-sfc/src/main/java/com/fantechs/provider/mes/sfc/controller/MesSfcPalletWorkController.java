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
            @ApiParam(value = "条码", required = true) @RequestBody RequestPalletWorkScanDto requestPalletWorkScanDto) throws Exception {

        PalletWorkScanDto palletWorkScanDto = mesSfcPalletWorkService.palletWorkScanBarcode(requestPalletWorkScanDto);
        return ControllerUtil.returnDataSuccess(palletWorkScanDto, 1);
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
            @ApiParam(value = "栈板表ID列表", required = true) @RequestBody List<Long> palletIdList,
            @ApiParam(value = "打印条码（0-否 1-是）", required = true) @RequestParam byte printBarcode) throws Exception {


        int i = mesSfcPalletWorkService.submitNoFullPallet(palletIdList, printBarcode);
        return ControllerUtil.returnCRUD(i);
    }

    @GetMapping("/updatePalletType")
    @ApiOperation("判断是否可以修改栈板作业配置")
    public ResponseEntity<Boolean> updatePalletType(@ApiParam(value = "工位Id", required = true) @RequestParam Long stationId) {

        Boolean b = mesSfcPalletWorkService.updatePalletType(stationId);
        return ControllerUtil.returnDataSuccess(b, 1);
    }

    @GetMapping("/updateNowPackageSpecQty")
    @ApiOperation("修改栈板包装规格数量")
    public ResponseEntity updateNowPackageSpecQty(
            @ApiParam(value = "产品栈板ID", required = true) @RequestParam Long productPalletId,
            @ApiParam(value = "包装规格数量", required = true) @RequestParam Double nowPackageSpecQty) throws Exception {


        int i = mesSfcPalletWorkService.updateNowPackageSpecQty(productPalletId, nowPackageSpecQty);
        return ControllerUtil.returnCRUD(i);
    }

    /**
     * 修改栈板状态为已转移
     * @param productPalletId
     * @return
     */
    @PostMapping("/updateMoveStatus")
    @ApiOperation("修改栈板状态为已转移")
    public ResponseEntity updateMoveStatus(@ApiParam(value = "产品栈板ID", required = true) @RequestParam Long productPalletId){
        return ControllerUtil.returnCRUD(mesSfcPalletWorkService.updateMoveStatus(productPalletId));
    }

}
