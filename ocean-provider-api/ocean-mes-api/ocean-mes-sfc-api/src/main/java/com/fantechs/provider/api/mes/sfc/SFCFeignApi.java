package com.fantechs.provider.api.mes.sfc;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ocean-mes-sfc")
public interface SFCFeignApi {
    @PostMapping("/rabbit/print")
    ResponseEntity print(@RequestBody PrintDto printDto);

    @ApiOperation("列表")
    @PostMapping("/mesSfcBarcodeProcess/findList")
    ResponseEntity<List<MesSfcBarcodeProcessDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess);

    @ApiOperation("补打列表")
    @PostMapping("/mesSfcWorkOrderBarcode/findList")
    ResponseEntity<List<MesSfcWorkOrderBarcodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcWorkOrderBarcode searchMesSfcWorkOrderBarcode);

    @ApiOperation("查询条码")
    @PostMapping("/mesSfcBarcodeProcess/findBarcode")
    ResponseEntity<List<MesSfcBarcodeProcess>> findBarcode(@RequestBody  SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess);

    @ApiOperation("条码查询")
    @PostMapping("/mesSfcWorkOrderBarcode/findBarcode")
    public ResponseEntity<MesSfcWorkOrderBarcode> findBarcode(@RequestParam String barcode);
}
