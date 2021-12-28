package com.fantechs.provider.guest.callagv.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.callagv.*;
import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleReBarcode;
import com.fantechs.common.base.general.entity.callagv.search.SearchCallAgvStorageMaterial;
import com.fantechs.common.base.general.entity.callagv.search.SearchCallAgvVehicleReBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.callagv.service.CallAgvVehicleReBarcodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@Api(tags = "木林森货架与条码控制器")
@RequestMapping("/callAgvVehicleReBarcode")
@Validated
@Slf4j
public class CallAgvVehicleReBarcodeController {

    @Resource
    private CallAgvVehicleReBarcodeService callAgvVehicleReBarcodeService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：", required = true) @RequestBody @Validated CallAgvVehicleReBarcode callAgvVehicleReBarcode) {
        return ControllerUtil.returnCRUD(callAgvVehicleReBarcodeService.save(callAgvVehicleReBarcode));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(callAgvVehicleReBarcodeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = CallAgvVehicleReBarcode.update.class) CallAgvVehicleReBarcode callAgvVehicleReBarcode) {
        return ControllerUtil.returnCRUD(callAgvVehicleReBarcodeService.update(callAgvVehicleReBarcode));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<CallAgvVehicleReBarcode> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        CallAgvVehicleReBarcode callAgvVehicleReBarcode = callAgvVehicleReBarcodeService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(callAgvVehicleReBarcode, StringUtils.isEmpty(callAgvVehicleReBarcode) ? 0 : 1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<CallAgvVehicleReBarcodeDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchCallAgvVehicleReBarcode searchCallAgvVehicleReBarcode) {
        Page<Object> page = PageHelper.startPage(searchCallAgvVehicleReBarcode.getStartPage(), searchCallAgvVehicleReBarcode.getPageSize());
        List<CallAgvVehicleReBarcodeDto> list = callAgvVehicleReBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvVehicleReBarcode));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel", notes = "导出excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchCallAgvVehicleReBarcode searchCallAgvVehicleReBarcode) {
        List<CallAgvVehicleReBarcodeDto> list = callAgvVehicleReBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvVehicleReBarcode));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出信息", "CallAgvVehicleReBarcode信息", CallAgvVehicleReBarcodeDto.class, "CallAgvVehicleReBarcode.xls", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BizErrorException(e);
        }
    }

    @ApiOperation("备料作业")
    @PostMapping("/callAgvStock")
    public ResponseEntity<List<CallAgvVehicleReBarcode>> callAgvStock(@ApiParam(value = "请求对象", required = true) @RequestBody RequestCallAgvStockDTO requestCallAgvStockDTO) throws Exception {

        List<CallAgvVehicleReBarcode> callAgvVehicleReBarcodeList = callAgvVehicleReBarcodeService.callAgvStock(requestCallAgvStockDTO);
        return ControllerUtil.returnDataSuccess(callAgvVehicleReBarcodeList, callAgvVehicleReBarcodeList.size());
    }

    @ApiOperation("AGV配送")
    @PostMapping("/callAgvDistribution")
    public ResponseEntity callAgvDistribution(
            @ApiParam(value = "周转工具（货架）ID", required = true) @RequestParam Long vehicleId,
            @ApiParam(value = "库区ID（type：1-当前库区 2-目的库区 3-目的库区）", required = true) @RequestParam Long warehouseAreaId,
            @ApiParam(value = "目标库位配送点ID") @RequestParam(required = false, defaultValue = "0") Long storageTaskPointId,
            @ApiParam(value = "AGV配送类型(1-备料完成配送 2-叫料配送 3-空货架返回)", required = true) @RequestParam Integer type,
            @ApiParam(value = "是否判断电梯等待任务") @RequestParam(required = false, defaultValue = "true") Boolean isSchedulingTask) throws Exception {

        return ControllerUtil.returnDataSuccess(callAgvVehicleReBarcodeService.callAgvDistribution(vehicleId, warehouseAreaId, storageTaskPointId, type, isSchedulingTask), 1);
    }

    @ApiOperation("AGV配送对外接口")
    @PostMapping("/callAgvDistributionRest")
    public ResponseEntity callAgvDistributionRest(@ApiParam(value = "请求对象", required = true) @RequestBody CallAgvDistributionRestDto callAgvDistributionRestDto) throws Exception {

        return ControllerUtil.returnDataSuccess(callAgvVehicleReBarcodeService.callAgvDistributionRest(callAgvDistributionRestDto), 1);
    }

    @ApiOperation("备料物料解绑")
    @PostMapping("/vehicleBarcodeUnbound")
    public ResponseEntity vehicleBarcodeUnbound(@ApiParam(value = "解绑物料对象列表", required = true) @RequestBody RequestBarcodeUnboundDTO requestBarcodeUnboundDTO) throws Exception {

        return ControllerUtil.returnCRUD(callAgvVehicleReBarcodeService.vehicleBarcodeUnbound(requestBarcodeUnboundDTO));
    }

    @ApiOperation("叫料作业查询列表")
    @PostMapping("/findCallAgvVehicleList")
    public ResponseEntity<List<CallAgvVehicleBarcodeDTO>> findCallAgvVehicleList(@ApiParam(value = "查询对象") @RequestBody SearchCallAgvVehicleReBarcode searchCallAgvVehicleReBarcode) {
        Page<Object> page = PageHelper.startPage(searchCallAgvVehicleReBarcode.getStartPage(), searchCallAgvVehicleReBarcode.getPageSize());
        List<CallAgvVehicleBarcodeDTO> list = callAgvVehicleReBarcodeService.findCallAgvVehicleList(ControllerUtil.dynamicConditionByEntity(searchCallAgvVehicleReBarcode));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @ApiOperation("货架移位")
    @PostMapping("/vehicleDisplacement")
    public ResponseEntity vehicleDisplacement(
            @ApiParam(value = "周转工具（货架）ID", required = true) @RequestParam Long vehicleId,
            @ApiParam(value = "库位配送点ID") @RequestParam(required = false, defaultValue = "0") Long storageTaskPointId,
            @ApiParam(value = "AGV配送类型(1-移出货架 2-移入货架（库位配送点ID必须要有）)", required = true) @RequestParam Integer type) throws Exception {

        return ControllerUtil.returnDataSuccess(callAgvVehicleReBarcodeService.vehicleDisplacement(vehicleId, storageTaskPointId, type), 1);
    }

    @ApiOperation("物料移库")
    @PostMapping("materialTransfer")
    public ResponseEntity materialTransfer(@ApiParam(value = "请求对象", required = true) @RequestBody RequestCallAgvStockDTO requestCallAgvStockDTO) throws Exception {

        return ControllerUtil.returnCRUD(callAgvVehicleReBarcodeService.materialTransfer(requestCallAgvStockDTO));
    }

    @ApiOperation("AGV库存汇总")
    @PostMapping("/agvWarehouseAreaMaterialSummary")
    public ResponseEntity<List<CallAgvWarehouseAreaMaterialDto>> agvWarehouseAreaMaterialSummary(@ApiParam(value = "查询对象") @RequestBody SearchCallAgvStorageMaterial SearchCallAgvStorageMaterial) {
        Page<Object> page = PageHelper.startPage(SearchCallAgvStorageMaterial.getStartPage(), SearchCallAgvStorageMaterial.getPageSize());
        List<CallAgvWarehouseAreaMaterialDto> list = callAgvVehicleReBarcodeService.agvWarehouseAreaMaterialSummary(SearchCallAgvStorageMaterial);
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @PostMapping(value = "/exportAgvWarehouseAreaMaterialSummary")
    @ApiOperation(value = "导出AGV库存汇总excel", notes = "导出AGV库存汇总excel", produces = "application/octet-stream")
    public void exportAgvWarehouseAreaMaterialSummaryExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchCallAgvStorageMaterial SearchCallAgvStorageMaterial) {
        List<CallAgvWarehouseAreaMaterialDto> list = callAgvVehicleReBarcodeService.agvWarehouseAreaMaterialSummary(SearchCallAgvStorageMaterial);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出AGV库存汇总信息", "AGV库存汇总信息", CallAgvWarehouseAreaMaterialDto.class, "AGV库存汇总信息.xls", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BizErrorException(e);
        }
    }

    @ApiOperation("AGV库存明细")
    @PostMapping("/agvStorageMaterialDetail")
    public ResponseEntity<List<CallAgvStorageMaterialDto>> agvStorageMaterialDetail(@ApiParam(value = "查询对象") @RequestBody SearchCallAgvStorageMaterial SearchCallAgvStorageMaterial) {
        Page<Object> page = PageHelper.startPage(SearchCallAgvStorageMaterial.getStartPage(), SearchCallAgvStorageMaterial.getPageSize());
        List<CallAgvStorageMaterialDto> list = callAgvVehicleReBarcodeService.agvStorageMaterialDetail(SearchCallAgvStorageMaterial);
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @PostMapping(value = "/exportAgvStorageMaterialDetail")
    @ApiOperation(value = "导出AGV库存明细excel", notes = "导出AGV库存明细excel", produces = "application/octet-stream")
    public void exportAgvStorageMaterialDetailExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchCallAgvStorageMaterial SearchCallAgvStorageMaterial) {
        List<CallAgvStorageMaterialDto> list = callAgvVehicleReBarcodeService.agvStorageMaterialDetail(SearchCallAgvStorageMaterial);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出AGV库存明细信息", "AGV库存明细信息", CallAgvStorageMaterialDto.class, "AGV库存明细.xls", response);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new BizErrorException(e);
        }
    }

    @ApiOperation("无货架扫码入库")
    @PostMapping("barcodeInWarehouseArea")
    public ResponseEntity BarcodeInWarehouseArea(@ApiParam(value = "请求对象", required = true) @RequestBody BarcodeWarehouseAreaDto barcodeWarehouseAreaDto) throws Exception {

        return ControllerUtil.returnCRUD(callAgvVehicleReBarcodeService.barcodeInWarehouseArea(barcodeWarehouseAreaDto));
    }

    @ApiOperation("无货架扫码出库")
    @PostMapping("barcodeOutWarehouseArea")
    public ResponseEntity BarcodeOutWarehouseArea(@ApiParam(value = "请求对象", required = true) @RequestBody List<Long> warehouseAreaReBarcodeIdList) throws Exception {

        return ControllerUtil.returnCRUD(callAgvVehicleReBarcodeService.barcodeOutWarehouseArea(warehouseAreaReBarcodeIdList));
    }
}
