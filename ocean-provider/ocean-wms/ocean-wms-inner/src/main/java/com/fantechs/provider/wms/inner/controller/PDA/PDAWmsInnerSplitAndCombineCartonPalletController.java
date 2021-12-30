package com.fantechs.provider.wms.inner.controller.PDA;

import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerSplitAndCombineCartonPalletInfoDto;
import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerSplitAndCombineCheckBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.PDAWmsInnerSplitAndCombinePrintDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDetDto;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.PDAWmsInnerSplitAndCombineCartonPalletService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * Created by leifengzhi on 2021/12/21.
 */
@RestController
@Api(tags = "PDA分合包箱/栈板作业")
@RequestMapping("/PDAWmsInnerSplitAndCombineCartonPallet")
@Validated
public class PDAWmsInnerSplitAndCombineCartonPalletController {

    @Resource
    private PDAWmsInnerSplitAndCombineCartonPalletService pdaWmsInnerSplitAndCombineCartonPalletService;

    @ApiOperation(value = "扫描包箱(栈板)码获取包箱(栈板)信息",notes = "获取包箱/栈板信息")
    @PostMapping("/getCartonPalletInfo")
    public ResponseEntity<PDAWmsInnerSplitAndCombineCartonPalletInfoDto> getCartonPalletInfo(@ApiParam(value = "箱码/栈板码",required = true)@RequestParam @NotBlank(message="箱码/栈板码不能为空") String barcode,
                                                                                             @ApiParam(value = "类型（1-包箱 2-栈板）",required = true)@RequestParam @NotNull(message="类型不能为空") Byte type) {
        PDAWmsInnerSplitAndCombineCartonPalletInfoDto cartonPalletInfo = pdaWmsInnerSplitAndCombineCartonPalletService.getCartonPalletInfo(barcode, type);
        return ControllerUtil.returnDataSuccess(cartonPalletInfo, StringUtils.isEmpty(cartonPalletInfo)?0:1);
    }

    @ApiOperation(value = "条码校验",notes = "条码校验")
    @PostMapping("/checkBarcode")
    public ResponseEntity<WmsInnerInventoryDetDto> checkBarcode(@ApiParam(value = "条码信息")@RequestBody PDAWmsInnerSplitAndCombineCheckBarcodeDto pdaWmsInnerSplitAndCombineCheckBarcodeDto) {
        WmsInnerInventoryDetDto wmsInnerInventoryDetDto = pdaWmsInnerSplitAndCombineCartonPalletService.checkBarcode(pdaWmsInnerSplitAndCombineCheckBarcodeDto);
        return ControllerUtil.returnDataSuccess(wmsInnerInventoryDetDto, StringUtils.isEmpty(wmsInnerInventoryDetDto)?0:1);
    }

    @ApiOperation(value = "校验库位",notes = "校验库位")
    @PostMapping("/checkStorageCode")
    public ResponseEntity<BaseStorage> checkStorageCode(@ApiParam(value = "库位编码",required = true)@RequestParam @NotBlank(message="库位编码不能为空") String storageCode) {
        BaseStorage baseStorage = pdaWmsInnerSplitAndCombineCartonPalletService.checkStorageCode(storageCode);
        return ControllerUtil.returnDataSuccess(baseStorage, StringUtils.isEmpty(baseStorage)?0:1);
    }

    @ApiOperation(value = "打印",notes = "打印")
    @PostMapping("/print")
    public ResponseEntity<String> print(@ApiParam(value = "打印信息")@RequestBody PDAWmsInnerSplitAndCombinePrintDto pdaWmsInnerSplitAndCombinePrintDto) {
        String barcode = pdaWmsInnerSplitAndCombineCartonPalletService.print(pdaWmsInnerSplitAndCombinePrintDto);
        return ControllerUtil.returnDataSuccess(barcode, StringUtils.isEmpty(barcode)?0:1);
    }
}
