package com.fantechs.provider.wanbao.api.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wanbao.api.service.SyncDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping(value = "/wanbaoSyncData")
@Api(tags = "万宝同步数据控制器")
@Slf4j
@Validated
public class WanbaoSyncDataController {

    @Resource
    private SyncDataService syncDataService;

    @ApiOperation(value = "万宝-物料基础信息同步",notes = "万宝-物料基础信息同步")
    @PostMapping("/syncMaterialData")
    public ResponseEntity syncMaterialData(){
        log.info("万宝-物料基础信息同步");
        syncDataService.syncMaterialData();
        return ControllerUtil.returnSuccess();
    }

    @ApiOperation(value = "万宝-工单信息同步",notes = "万宝-工单信息同步")
    @PostMapping("/syncOrderData")
    public ResponseEntity syncOrderData(){
        log.info("万宝-工单信息同步");
        syncDataService.syncOrderData(null);
        return ControllerUtil.returnSuccess();
    }

    @ApiOperation(value = "万宝-根据订单编码同步工单信息",notes = "万宝-根据订单编码同步工单信息")
    @PostMapping("/syncOrderByOrderCode")
    public ResponseEntity syncOrderByOrderCode(@ApiParam(value = "工单号", required = true) @RequestParam @NotNull(message = "工单号不能为空") String workOrderCode){
        log.info("万宝-根据订单编码同步工单信息");
        syncDataService.syncOrderData(workOrderCode);
        return ControllerUtil.returnSuccess();
    }

    @ApiOperation(value = "万宝-销售订单信息同步",notes = "万宝-销售订单信息同步")
    @PostMapping("/syncSaleOrderData")
    public ResponseEntity syncSaleOrderData(){
        log.info("万宝-销售订单信息同步");
        syncDataService.syncSaleOrderData();
        return ControllerUtil.returnSuccess();
    }

    @ApiOperation(value = "万宝-出货通知单信息同步",notes = "万宝-出货通知单信息同步")
    @PostMapping("/syncOutDeliveryData")
    public ResponseEntity syncOutDeliveryData(){
        log.info("万宝-出货通知单信息同步");
        syncDataService.syncOutDeliveryData();
        return ControllerUtil.returnSuccess();
    }

    @ApiOperation(value = "万宝-产品条码信息同步",notes = "万宝-产品条码信息同步")
    @PostMapping("/syncBarcodeData")
    public ResponseEntity syncBarcodeData(){
        log.info("万宝-产品条码信息同步");
        syncDataService.syncBarcodeData(true);
        return ControllerUtil.returnSuccess();
    }

    @ApiOperation(value = "万宝-PQMS所有数据同步",notes = "万宝-PQMS所有数据同步")
    @PostMapping("/syncAllBarcodeData")
    public ResponseEntity syncAllBarcodeData(){
        log.info("万宝-PQMS所有数据同步");
        syncDataService.syncBarcodeData(false);
        return ControllerUtil.returnSuccess();
    }
}
