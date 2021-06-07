package com.fantechs.provider.client.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlJobOrderDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.client.dto.PtlJobOrderDTO;
import com.fantechs.provider.client.server.ElectronicTagStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by lfz on 2020/11/27.
 */
@RestController
@Api(tags = "电子标签控制器")
public class ElectronicTagStorageController {
    @Autowired
    private ElectronicTagStorageService electronicTagStorageService;

    @PostMapping(value = "/createPtlJobOrder")
    @ApiOperation(value = "生成任务单", notes = "生成分拣单")
    public ResponseEntity createPtlJobOrder(@RequestBody @Validated List<PtlJobOrderDTO> ptlJobOrderDTOList) {
        try {
            return ControllerUtil.returnCRUD(electronicTagStorageService.createPtlJobOrder(ptlJobOrderDTOList));
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @GetMapping(value = "/sendElectronicTagStorage")
    @ApiOperation(value = "激活（拣取发送亮灯）", notes = "激活（拣取发送亮灯）")
    public ResponseEntity<PtlJobOrder> sendElectronicTagStorage(
            @ApiParam(value = "任务单Id", required = true) @RequestParam Long jobOrderId,
            @ApiParam(value = "仓库区域Id")@RequestParam(required = false) Long warehouseAreaId) {
        try {
            if (StringUtils.isEmpty(warehouseAreaId)) {
                warehouseAreaId = Long.valueOf(0);
            }
            PtlJobOrder ptlJobOrder = electronicTagStorageService.sendElectronicTagStorage(jobOrderId, warehouseAreaId);
            return ControllerUtil.returnDataSuccess("操作成功", ptlJobOrder);
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @GetMapping(value = "/writeBackPtlJobOrder")
    @ApiOperation(value = "回写PTL作业任务单（F-完成 E-缺货异常）", notes = "回写PTL作业任务单（F-完成 E-异常）")
    public ResponseEntity<PtlJobOrderDto> writeBackPtlJobOrder(
            @ApiParam(value = "任务单Id", required = true) @RequestParam Long jobOrderId,
            @ApiParam(value = "状态（F-完成 E-异常）", required = true) @RequestParam String status) {
        try {
            PtlJobOrderDto ptlJobOrderDto = electronicTagStorageService.writeBackPtlJobOrder(jobOrderId, status);
            return ControllerUtil.returnDataSuccess("操作成功", ptlJobOrderDto);
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @GetMapping(value = "/sendElectronicTagStorageLightTest")
    @ApiOperation(value = "发送电子标签亮灯/灭灯测试", notes = "发送电子标签亮灯/灭灯测试")
    public ResponseEntity<String> sendElectronicTagStorageLightTest(
            @RequestParam(value = "materialCode") String materialCode,
            @RequestParam(value = "code(1001-亮灯 1002-灭灯)") Integer code) {
        try {
            String result = electronicTagStorageService.sendElectronicTagStorageLightTest(materialCode, code);
            return ControllerUtil.returnSuccess();
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }
}
