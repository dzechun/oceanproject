package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.sfc.PdaCartonWorkDto;
import com.fantechs.common.base.general.dto.mes.sfc.RequestPalletWorkScanDto;
import com.fantechs.common.base.general.dto.mes.sfc.ScanBarcodeDto;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseStation;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStation;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeOperationService;
import com.fantechs.provider.mes.sfc.service.MesSfcPalletWorkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 读头接口
 *
 * @author bgkun
 * @date 2021-11-23
 */
@RestController
@Api(tags = "读头接口")
@RequestMapping("/scanBarcode")
public class ScanBarcodeController {

    @Resource
    MesSfcBarcodeOperationService mesSfcBarcodeOperationService;
    @Resource
    MesSfcPalletWorkService mesSfcPalletWorkService;
    @Resource
    BaseFeignApi baseFeignApi;

    @ApiOperation("读头-扫码作业")
    @PostMapping("/doScan")
    public ResponseEntity doScan(@ApiParam(value = "条码", required = true) @RequestBody ScanBarcodeDto scanBarcodeDto) throws Exception {

        if ("1".equals(scanBarcodeDto.getType())){
            // 包箱
            PdaCartonWorkDto dto = new PdaCartonWorkDto();
            dto.setBarCode(scanBarcodeDto.getBarCode());
            dto.setStationId(scanBarcodeDto.getStationId());
            dto.setProcessId(scanBarcodeDto.getProcessId());
            dto.setProLineId(scanBarcodeDto.getProLineId());
            dto.setAnnex(false);
            dto.setCheckOrNot(false);
            dto.setPrint(false);
            dto.setPackType("1");
            mesSfcBarcodeOperationService.pdaCartonWork(dto);
        }else if ("2".equals(scanBarcodeDto.getType())){
            // 栈板
            RequestPalletWorkScanDto dto = new RequestPalletWorkScanDto();
            dto.setStationId(scanBarcodeDto.getStationId());
            dto.setProcessId(scanBarcodeDto.getProcessId());
            dto.setProLineId(scanBarcodeDto.getProLineId());
            dto.setBarcode(scanBarcodeDto.getBarCode());
            dto.setCheckdaliyOrder((byte) 0);
            dto.setPrintBarcode((byte) 0);
            dto.setPalletType((byte) 2);
            mesSfcPalletWorkService.palletWorkScanBarcode(dto);
        }else if ("3".equals(scanBarcodeDto.getType())){
            // 出库

        }else {
            return ControllerUtil.returnFail(ErrorCodeEnum.GL9999404);
        }

        return ControllerUtil.returnSuccess();
    }

    @ApiOperation("读头-获取工序、产线、工位基础数据")
    @GetMapping("/getBasics")
    public ResponseEntity getBasics(){
        SearchBaseProcess searchBaseProcess = new SearchBaseProcess();
        searchBaseProcess.setPageSize(9999);
        List<BaseProcess> processList = baseFeignApi.findProcessList(searchBaseProcess).getData();

        SearchBaseStation searchBaseStation = new SearchBaseStation();
        searchBaseStation.setPageSize(9999);
        List<BaseStation> stationList = baseFeignApi.findBaseStationList(searchBaseStation).getData();

        SearchBaseProLine searchBaseProLine = new SearchBaseProLine();
        searchBaseProLine.setPageSize(9999);
        List<BaseProLine> proLineList = baseFeignApi.findList(searchBaseProLine).getData();

        Map<String, Object> map = new HashMap<>();
        map.put("process", processList);
        map.put("station", stationList);
        map.put("proLine", proLineList);
        return ControllerUtil.returnDataSuccess("成功", map);
    }
}
