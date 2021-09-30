package com.fantechs.provider.client.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlJobOrderDto;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.client.dto.PtlJobOrderDTO;
import com.fantechs.provider.client.dto.PtlJobOrderDetPrintDTO;
import com.fantechs.provider.client.dto.ResponseEntityDTO;
import com.fantechs.provider.client.server.ElectronicTagStorageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@Api(tags = "电子标签控制器")
public class ElectronicTagStorageController {
    @Autowired
    private ElectronicTagStorageService electronicTagStorageService;

    @PostMapping(value = "/createPtlJobOrder")
    @ApiOperation(value = "生成任务单", notes = "生成分拣单")
    public ResponseEntityDTO createPtlJobOrder(@RequestBody List<PtlJobOrderDTO> ptlJobOrderDTOList) {
        try {
            return electronicTagStorageService.createPtlJobOrder(ptlJobOrderDTOList);
        } catch (Exception e) {
            ResponseEntityDTO responseEntityDTO = new ResponseEntityDTO();
            responseEntityDTO.setCode(ptlJobOrderDTOList.get(0).getCustomerNo());
            responseEntityDTO.setMessage(e.getMessage());
            responseEntityDTO.setSuccess("e");
            return responseEntityDTO;
        }
    }

    @GetMapping(value = "/sendElectronicTagStorage")
    @ApiOperation(value = "激活（拣取发送亮灯）", notes = "激活（拣取发送亮灯）")
    public ResponseEntity<List<PtlJobOrderDto>> sendElectronicTagStorage(
            @ApiParam(value = "任务单Id列表，多个逗号分隔", required = true) @RequestParam String ids,
            @ApiParam(value = "仓库区域Id")@RequestParam(required = false) Long warehouseAreaId) {
        try {
            if (StringUtils.isEmpty(warehouseAreaId)) {
                warehouseAreaId = Long.valueOf(0);
            }
            List<PtlJobOrderDto> ptlJobOrderDtoList = electronicTagStorageService.sendElectronicTagStorage(ids, warehouseAreaId, 0);
            return ControllerUtil.returnDataSuccess("操作成功", ptlJobOrderDtoList);
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @GetMapping(value = "/writeBackPtlJobOrder")
    @ApiOperation(value = "回写PTL作业任务单", notes = "回写PTL作业任务单")
    public ResponseEntity<PtlJobOrderDto> writeBackPtlJobOrder(
            @ApiParam(value = "任务单Id", required = true) @RequestParam Long jobOrderId,
            @ApiParam(value = "完成类型(1-完成(需要回写) 2-手动完成(不需要回写))", required = true) @RequestParam Integer type) {
        try {
            PtlJobOrderDto ptlJobOrderDto = electronicTagStorageService.writeBackPtlJobOrder(jobOrderId, type);
            return ControllerUtil.returnDataSuccess("操作成功", ptlJobOrderDto);
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @GetMapping("/printPtlJobOrderLabel")
    @ApiOperation(value = "作业任务打印标签", notes = "作业任务打印标签")
    public ResponseEntity<List<PtlJobOrderDetPrintDTO>> printPtlJobOrderLabel(
            @ApiParam(value = "任务单Id列表，多个逗号分隔", required = true) @RequestParam String ids,
            @ApiParam(value = "作业人员Id", required = true) @RequestParam Long workUserId) {
        try {
            List<PtlJobOrderDetPrintDTO> ptlJobOrderDetPrintDTOList = electronicTagStorageService.printPtlJobOrderLabel(ids, workUserId, 0);
            return ControllerUtil.returnDataSuccess(ptlJobOrderDetPrintDTOList, ptlJobOrderDetPrintDTOList.size());
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @GetMapping("/hangUpPtlJobOrderDet")
    @ApiOperation(value = "作业单明细挂起", notes = "作业单明细挂起")
    public ResponseEntity hangUpPtlJobOrderDet(
            @ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        try {
            int i = electronicTagStorageService.hangUpPtlJobOrderDet(ids);
            return ControllerUtil.returnCRUD(i);
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @PostMapping("/cancelPtrlJobOrder")
    @ApiOperation(value = "取消/复核拣货任务", notes = "取消/复核拣货任务")
    public ResponseEntityDTO cancelPtrlJobOrder(@ApiParam(value = "请求参数", required = true) @RequestBody PtlJobOrderDTO ptlJobOrderDTO) {

        try {
            return electronicTagStorageService.cancelPtrlJobOrder(ptlJobOrderDTO);
        } catch (Exception e) {
            ResponseEntityDTO responseEntityDTO = new ResponseEntityDTO();
            responseEntityDTO.setCode(ptlJobOrderDTO.getCustomerNo());
            responseEntityDTO.setMessage(e.getMessage());
            responseEntityDTO.setSuccess("e");
            return responseEntityDTO;
        }
    }

    @GetMapping(value = "/ptlJobOrderLightOff")
    @ApiOperation(value = "作业任务单灭灯", notes = "作业任务单灭灯")
    public ResponseEntity ptlJobOrderLightOff(
            @ApiParam(value = "任务单Id", required = true) @RequestParam Long jobOrderId) {
        try {
            int i = electronicTagStorageService.ptlJobOrderLightOff(jobOrderId);
            return ControllerUtil.returnCRUD(i);
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @GetMapping("/activateAndPrint")
    @ApiOperation(value = "激活任务单并打印", notes = "激活任务单并打印")
    public ResponseEntity activateAndPrint(
            @ApiParam(value = "任务单Id列表，多个逗号分隔", required = true) @RequestParam String ids,
            @ApiParam(value = "作业人员Id", required = true) @RequestParam Long workUserId) {
        try {
            int i = electronicTagStorageService.activateAndPrint(ids, workUserId);
            return ControllerUtil.returnCRUD(i);
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @GetMapping("/getPrinter")
    @ApiOperation(value = "获取当前区域的默认打印用户", notes = "获取当前区域的默认打印用户")
    public ResponseEntity<SysUser> getPrinter() {
        try {
            return ControllerUtil.returnDataSuccess(electronicTagStorageService.getPrinter(), 1);
        } catch (Exception e) {
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }
}
