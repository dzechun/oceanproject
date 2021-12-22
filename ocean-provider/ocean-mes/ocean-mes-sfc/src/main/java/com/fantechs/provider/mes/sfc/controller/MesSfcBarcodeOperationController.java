package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.sfc.CloseCartonDto;
import com.fantechs.common.base.general.dto.mes.sfc.PdaCartonRecordDto;
import com.fantechs.common.base.general.dto.mes.sfc.PdaCartonWorkDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductCarton;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeOperationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 生产管理-PDA包箱作业
 *
 * @author bgkun
 * @date 2021-04-15
 */
@RestController
@Api(tags = "生产管理-PDA包箱作业")
@RequestMapping("/mesSfcBarcodeOperation")
public class MesSfcBarcodeOperationController {


    @Resource
    MesSfcBarcodeOperationService mesSfcBarcodeOperationService;

    @ApiOperation("PDA包箱作业-查询上次作业数据")
    @PostMapping("/findLastCarton")
    public ResponseEntity<PdaCartonRecordDto> findLastCarton(@ApiParam(value = "工序ID", required = true) @RequestParam @NotNull(message = "processId不能为空") Long processId,
                                                             @ApiParam(value = "工位ID", required = true) @RequestParam @NotNull(message = "stationId不能为空") Long stationId,
                                                             @ApiParam(value = "包箱类型(1：工单包箱，2：料号包箱)", required = true) @RequestParam @NotNull(message = "packType不能为空") String packType) {
        return ControllerUtil.returnSuccess("", mesSfcBarcodeOperationService.findLastCarton(processId, stationId, packType));
    }

    @ApiOperation("PDA包箱作业")
    @PostMapping("/cartonOperation")
    public ResponseEntity cartonOperation(@ApiParam(value = "包箱扫条码", required = true) @RequestBody PdaCartonWorkDto dto) throws Exception {
        return ControllerUtil.returnDataSuccess("", mesSfcBarcodeOperationService.pdaCartonWork(dto));
    }

    @ApiOperation("PDA包箱作业-修改包箱规格数量")
    @PostMapping("/updateCartonDescNum")
    public ResponseEntity updateCartonDescNum(@ApiParam(value = "包箱ID", required = true) @RequestParam @NotNull(message = "productCartonId不能为空") Long productCartonId,
                                              @ApiParam(value = "包箱规格数量", required = true) @RequestParam @NotNull(message = "cartonDescNum不能为空") BigDecimal cartonDescNum,
                                              @ApiParam(value = "包箱类型(1：工单包箱，2：料号包箱)", required = true) @RequestParam @NotNull(message = "packType不能为空") String packType,
                                              @ApiParam(value = "是否打印", required = true) @RequestParam Boolean print,
                                              @ApiParam(value = "打印名称", required = true) @RequestParam String printName,
                                              @ApiParam(value = "工序", required = true) @RequestParam Long processId) {
        return ControllerUtil.returnCRUD(mesSfcBarcodeOperationService.updateCartonDescNum(productCartonId, cartonDescNum, packType, print, printName, processId));
    }

    @ApiOperation("PDA包箱作业-修改同一工单同一个料号配置")
    @PostMapping("/checkCartonCloseStatus")
    public ResponseEntity<Boolean> checkCartonCloseStatus(@ApiParam(value = "stationId", required = true) @RequestParam @NotNull(message = "stationId不能为空") Long stationId) {
        List<MesSfcProductCarton> sfcProductCartonList = mesSfcBarcodeOperationService.findCartonByStationId(stationId);
        if (sfcProductCartonList.isEmpty()) {
            return ControllerUtil.returnDataSuccess(true, 1);
        } else {
            return ControllerUtil.returnFail(ErrorCodeEnum.PDA40012027);
        }
    }

    @ApiOperation("未满箱提交关箱")
    @PostMapping("/closeCarton")
    public ResponseEntity closeCarton(@ApiParam(value = "关箱", required = true) @RequestBody CloseCartonDto dto) throws Exception{
        return ControllerUtil.returnCRUD(mesSfcBarcodeOperationService.closeCarton(dto));
    }

}
