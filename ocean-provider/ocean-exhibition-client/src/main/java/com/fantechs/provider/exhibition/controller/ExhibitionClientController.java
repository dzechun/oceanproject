package com.fantechs.provider.exhibition.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.exhibition.service.ExhibitionClientService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "展厅客户端")
public class ExhibitionClientController {

    @Autowired
    private ExhibitionClientService exhibitionClientService;

    /**
     * 订单生产
     * @throws Exception
     */
    @PostMapping(value="/makingOrders")
    @ApiOperation(value = "订单生产",notes = "订单生产")
    public ResponseEntity makingOrders(@ApiParam(value = "订单Id", required = true) @RequestParam Long orderId) {
        try {
            return ControllerUtil.returnCRUD(exhibitionClientService.makingOrders(orderId));
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @PostMapping(value = "/agvStockTask")
    @ApiOperation(value = "执行agv任务配送备料单物料")
    public ResponseEntity agvStockTask(
            @ApiParam(value = "备料单Id", required = true) @RequestParam Long stockId,
            @ApiParam(value = "配送类型（1-配送物料 2-配送托盘）", required = true) @RequestParam Integer type) {

        try {
            return ControllerUtil.returnSuccess("操作成功", exhibitionClientService.agvStockTask(stockId, type));
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @PostMapping(value = "/agvStockTaskTest")
    @ApiOperation(value = "执行agv任务配送备料单物料测试")
    public ResponseEntity agvStockTaskTest(@ApiParam(value = "物料编码", required = true) @RequestParam String materialCode) {

        try {
            return ControllerUtil.returnSuccess("操作成功", exhibitionClientService.agvStockTaskTest(materialCode));
        } catch (Exception e) {
            e.printStackTrace();
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.GL99990500.getCode());
        }
    }

    @PostMapping(value = "/agvContinueTask")
    @ApiOperation(value = "继续执行agv任务配送")
    public ResponseEntity agvContinueTask() {

        return ControllerUtil.returnSuccess("操作成功", exhibitionClientService.agvContinueTask());
    }
}
