package com.fantechs.provider.api.mes.sfc;

import com.fantechs.common.base.general.dto.basic.BaseExecuteResultDto;
import com.fantechs.common.base.general.dto.mes.sfc.*;
import com.fantechs.common.base.general.dto.mes.sfc.Search.*;
import com.fantechs.common.base.general.dto.restapi.RestapiChkLogUserInfoApiDto;
import com.fantechs.common.base.general.dto.wanbao.WanbaoAutoStackingListDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcProductPallet;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.mes.sfc.SearchMesSfcWorkOrderBarcode;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(name = "ocean-mes-sfc")
public interface SFCFeignApi {
    @PostMapping("/rabbit/print")
    ResponseEntity print(@RequestBody PrintDto printDto);

    @PostMapping("/rabbit/QUEUEprint")
    ResponseEntity QUEUEprint(@RequestBody PrintDto printDto,@RequestParam String id);

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

    @ApiOperation("查询产品栈板列表")
    @PostMapping("/mesSfcProductPallet/findList")
    ResponseEntity<List<MesSfcProductPalletDto>> findProductPalletList(@RequestBody SearchMesSfcProductPallet searchMesSfcProductPallet);

    @ApiOperation("查询产品栈板看板数据")
    @PostMapping("/mesSfcProductPallet/getPalletReport")
    ResponseEntity<List<MesSfcPalletReportDto>> getPalletReport();

    @ApiOperation("查询产品包箱列表")
    @PostMapping("/mesSfcProductCarton/findList")
    ResponseEntity<List<MesSfcProductCartonDto>> findProductCartonList(@RequestBody SearchMesSfcProductCarton searchMesSfcProductCarton);

    @ApiOperation("修改栈板状态为已转移")
    @PostMapping("/mesSfcPalletWork/updateMoveStatus")
    ResponseEntity<List<MesSfcProductCartonDto>> updateMoveStatus(@ApiParam(value = "产品栈板ID", required = true) @RequestParam Long productPalletId);

    @ApiOperation("工单条码查询栈板")
    @PostMapping("/mesSfcProductPalletDet/findList")
    ResponseEntity<List<MesSfcProductPalletDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcProductPalletDet searchMesSfcProductPalletDet);

    @ApiOperation("获取栈板详情")
    @PostMapping("/mesSfcProductPallet/detail")
    ResponseEntity<MesSfcProductPallet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id);

    @ApiOperation("产品条码过站记录列表")
    @PostMapping("/mesSfcBarcodeProcessRecord/findList")
    ResponseEntity<List<MesSfcBarcodeProcessRecordDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcBarcodeProcessRecord searchMesSfcBarcodeProcessRecord);

    @ApiOperation("过站作业-用户登录信息校验")
    @PostMapping("/mesSfcScanBarcode/chkLogUserInfo")
    ResponseEntity<BaseExecuteResultDto> chkLogUserInfo(@ApiParam(value = "用户登录参数")@RequestBody RestapiChkLogUserInfoApiDto restapiChkLogUserInfoApiDto);

    @ApiOperation("更新产品条码过站表")
    @PostMapping("/mesSfcBarcodeProcess/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody MesSfcBarcodeProcess mesSfcBarcodeProcess);

    @PostMapping("/mesSfcPalletWork/scanByManualOperation")
    @ApiOperation("栈板作业扫码(人工)")
    ResponseEntity<ScanByManualOperationDto> scanByManualOperation(@ApiParam(value = "条码", required = true) @RequestParam String barcode, @ApiParam(value = "产线ID", required = true) @RequestParam Long proLineId);

    @PostMapping("/mesSfcPalletWork/workByManualOperation")
    @ApiOperation("栈板作业提交(人工)")
    ResponseEntity<Integer> workByManualOperation(@ApiParam(value = "条码", required = true) @RequestBody PalletWorkByManualOperationDto dto);

    @ApiOperation("产品关键物料清单列表")
    @PostMapping("/mesSfcKeyPartRelevance/findList")
    ResponseEntity<List<MesSfcKeyPartRelevanceDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcKeyPartRelevance searchMesSfcKeyPartRelevance);


    @ApiOperation("万宝-同步三星客户条码")
    @PostMapping("/mesSfcWorkOrderBarcode/batchSyncBarcode")
    ResponseEntity batchSyncBarcode(@ApiParam(value = "必传",required = true) @RequestBody BatchSyncBarcodeDto dto);

    @ApiOperation("万宝-查询条码数据")
    @PostMapping("/mesSfcWorkOrderBarcode/syncFindBarcode")
    ResponseEntity<SyncFindBarcodeDto> syncFindBarcode(@ApiParam(value = "必传",required = true) @RequestParam Long labelCategoryId, @ApiParam(value = "条码集合", required = true) @RequestParam List<String> barcodeList);

    @PostMapping("/mesSfcPalletWork/sendMQByStacking")
    @ApiOperation("堆码作业发送MQ")
    ResponseEntity sendMQByStacking(@ApiParam(value = "堆垛号", required = true) @RequestParam String stackCode);

    @PostMapping("/mesSfcPalletWork/workByAuto")
    @ApiOperation("栈板作业提交(人工)")
    ResponseEntity<Integer> workByAuto(@ApiParam(value = "条码", required = true) @RequestBody WanbaoAutoStackingListDto dto);

    @PostMapping("/mesSfcPalletWork/checkBarCode")
    @ApiOperation("堆码作业发送MQ")
    ResponseEntity<Boolean> checkBarCode(@ApiParam(value = "堆垛号", required = true) @RequestBody List<String> barcodeList);
}
