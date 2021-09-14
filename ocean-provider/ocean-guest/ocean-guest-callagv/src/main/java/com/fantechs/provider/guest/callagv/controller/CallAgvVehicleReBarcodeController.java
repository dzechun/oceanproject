package com.fantechs.provider.guest.callagv.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.callagv.CallAgvVehicleBarcodeDTO;
import com.fantechs.common.base.general.dto.callagv.CallAgvVehicleReBarcodeDto;
import com.fantechs.common.base.general.dto.callagv.RequestBarcodeUnboundDTO;
import com.fantechs.common.base.general.dto.callagv.RequestCallAgvStockDTO;
import com.fantechs.common.base.general.entity.callagv.CallAgvVehicleReBarcode;
import com.fantechs.common.base.general.entity.callagv.search.SearchCallAgvVehicleReBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.callagv.service.CallAgvVehicleReBarcodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
public class CallAgvVehicleReBarcodeController {

    @Resource
    private CallAgvVehicleReBarcodeService callAgvVehicleReBarcodeService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated CallAgvVehicleReBarcode callAgvVehicleReBarcode) {
        return ControllerUtil.returnCRUD(callAgvVehicleReBarcodeService.save(callAgvVehicleReBarcode));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(callAgvVehicleReBarcodeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=CallAgvVehicleReBarcode.update.class) CallAgvVehicleReBarcode callAgvVehicleReBarcode) {
        return ControllerUtil.returnCRUD(callAgvVehicleReBarcodeService.update(callAgvVehicleReBarcode));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<CallAgvVehicleReBarcode> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        CallAgvVehicleReBarcode  callAgvVehicleReBarcode = callAgvVehicleReBarcodeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(callAgvVehicleReBarcode,StringUtils.isEmpty(callAgvVehicleReBarcode)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<CallAgvVehicleReBarcodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchCallAgvVehicleReBarcode searchCallAgvVehicleReBarcode) {
        Page<Object> page = PageHelper.startPage(searchCallAgvVehicleReBarcode.getStartPage(),searchCallAgvVehicleReBarcode.getPageSize());
        List<CallAgvVehicleReBarcodeDto> list = callAgvVehicleReBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvVehicleReBarcode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchCallAgvVehicleReBarcode searchCallAgvVehicleReBarcode){
    List<CallAgvVehicleReBarcodeDto> list = callAgvVehicleReBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvVehicleReBarcode));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "CallAgvVehicleReBarcode信息", CallAgvVehicleReBarcodeDto.class, "CallAgvVehicleReBarcode.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("备料作业")
    @PostMapping("callAgvStock")
    public ResponseEntity callAgvStock(@ApiParam(value = "请求对象", required = true) @RequestBody RequestCallAgvStockDTO requestCallAgvStockDTO) {

        try {
            return ControllerUtil.returnCRUD(callAgvVehicleReBarcodeService.callAgvStock(requestCallAgvStockDTO));
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @ApiOperation("AGV配送")
    @PostMapping("/callAgvDistribution")
    public ResponseEntity callAgvDistribution(
            @ApiParam(value = "周转工具（货架）ID", required = true) @RequestParam Long vehicleId,
            @ApiParam(value = "库区ID（type：1-当前库区 2-目的库区 3-目的库区）", required = true) @RequestParam Long warehouseAreaId,
            @ApiParam(value = "AGV配送类型(1-备料完成配送 2-叫料配送 3-空货架返回)", required = true) @RequestParam Integer type) {

        try {
            return ControllerUtil.returnDataSuccess(callAgvVehicleReBarcodeService.callAgvDistribution(vehicleId, warehouseAreaId, type), 1);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @ApiOperation("备料物料解绑")
    @PostMapping("/vehicleBarcodeUnbound")
    public ResponseEntity vehicleBarcodeUnbound(@ApiParam(value = "解绑物料对象列表", required = true) @RequestBody RequestBarcodeUnboundDTO requestBarcodeUnboundDTO) {

        try {
            return ControllerUtil.returnCRUD(callAgvVehicleReBarcodeService.vehicleBarcodeUnbound(requestBarcodeUnboundDTO));
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @ApiOperation("叫料作业查询列表")
    @PostMapping("/findCallAgvVehicleList")
    public ResponseEntity<List<CallAgvVehicleBarcodeDTO>> findCallAgvVehicleList(@ApiParam(value = "查询对象")@RequestBody SearchCallAgvVehicleReBarcode searchCallAgvVehicleReBarcode) {
        Page<Object> page = PageHelper.startPage(searchCallAgvVehicleReBarcode.getStartPage(),searchCallAgvVehicleReBarcode.getPageSize());
        List<CallAgvVehicleBarcodeDTO> list = callAgvVehicleReBarcodeService.findCallAgvVehicleList(ControllerUtil.dynamicConditionByEntity(searchCallAgvVehicleReBarcode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("叫料作业")
    @PostMapping("/CallAgvVehicle")
    public ResponseEntity CallAgvVehicle(
            @ApiParam(value = "周转工具（货架）ID", required = true) @RequestParam Long vehicleId,
            @ApiParam(value = "目标库区ID", required = true) @RequestParam Long warehouseAreaId) {

        try {
            return ControllerUtil.returnCRUD(callAgvVehicleReBarcodeService.CallAgvVehicle(vehicleId, warehouseAreaId));
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
