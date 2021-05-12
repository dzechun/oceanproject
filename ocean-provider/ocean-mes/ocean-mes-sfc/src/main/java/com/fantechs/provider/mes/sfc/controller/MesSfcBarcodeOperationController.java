package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.entity.mes.sfc.*;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
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


    @Autowired
    MesSfcBarcodeOperationService mesSfcBarcodeOperationService;
    @Autowired
    MesSfcProductCartonService mesSfcProductCartonService;

    @ApiOperation("PDA投产作业")
    @PostMapping("/pdaPutIntoProduction")
    public ResponseEntity pdaPutIntoProduction(@ApiParam(value = "PDA作业对象") @RequestBody PdaPutIntoProductionDto vo) {
        return ControllerUtil.returnCRUD(mesSfcBarcodeOperationService.pdaPutIntoProduction(vo));
    }


    @ApiOperation("PDA包箱作业-查询上次作业数据")
    @PostMapping("/findLastCarton")
    public ResponseEntity<PdaCartonRecordDto> findLastCarton(@ApiParam(value = "processId", required = true) @RequestParam @NotNull(message = "processId不能为空") Long processId,
                                                             @ApiParam(value = "stationId", required = true) @RequestParam @NotNull(message = "stationId不能为空") Long stationId) {
        return ControllerUtil.returnSuccess("成功", mesSfcBarcodeOperationService.findLastCarton(processId, stationId));
    }

    @ApiOperation("PDA包箱作业-条码提交")
    @PostMapping("/cartonOperation")
    public ResponseEntity<PdaCartonRecordDto> cartonOperation(@ApiParam(value = "包箱扫条码", required = true) @RequestBody PdaCartonDto vo) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        try {
            // 未满箱提交，关箱
            if (vo.getCloseOrNot()) {
                if (StringUtils.isEmpty(vo.getProductCartonId())) {
                    throw new BizErrorException(ErrorCodeEnum.PDA40012025);
                }
                MesSfcProductCarton mesSfcProductCarton = mesSfcProductCartonService.selectByKey(vo.getProductCartonId());
                mesSfcProductCarton.setCloseStatus((byte) 1);
                mesSfcProductCarton.setCloseCartonUserId(user.getUserId());
                mesSfcProductCarton.setCloseCartonTime(new Date());
                mesSfcProductCarton.setModifiedUserId(user.getUserId());
                mesSfcProductCarton.setModifiedTime(new Date());
                return ControllerUtil.returnCRUD(mesSfcProductCartonService.update(mesSfcProductCarton));
            }

            return ControllerUtil.returnSuccess("成功", mesSfcBarcodeOperationService.cartonOperation(vo));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BizErrorException(ex);
        }
    }

    @ApiOperation("PDA包箱作业-附件码提交")
    @PostMapping("/cartonAnnexOperation")
    public ResponseEntity<PdaCartonRecordDto> cartonAnnexOperation(@ApiParam(value = "包箱扫附件条码", required = true) @RequestBody PdaCartonAnnexDto vo) {
        try{
            // 构造返回值
            return ControllerUtil.returnSuccess("成功", mesSfcBarcodeOperationService.cartonAnnexOperation(vo));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new BizErrorException(ex);
        }
    }

    @ApiOperation("PDA包箱作业-修改包箱规格数量")
    @PostMapping("/updateCartonDescNum")
    public ResponseEntity updateCartonDescNum(@ApiParam(value = "包箱ID", required = true) @RequestParam @NotNull(message = "productCartonId不能为空") Long productCartonId,
                                              @ApiParam(value = "包箱规格数量", required = true) @RequestParam @NotNull(message = "cartonDescNum不能为空") BigDecimal cartonDescNum) {
        return ControllerUtil.returnCRUD(mesSfcBarcodeOperationService.updateCartonDescNum(productCartonId, cartonDescNum));
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

}
