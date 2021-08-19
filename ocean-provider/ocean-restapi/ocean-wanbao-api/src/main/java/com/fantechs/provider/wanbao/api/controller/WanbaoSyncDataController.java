package com.fantechs.provider.wanbao.api.controller;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wanbao.api.service.SyncDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


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
        syncDataService.syncMaterialData();
        return ControllerUtil.returnSuccess();
    }

    @ApiOperation(value = "万宝-v",notes = "万宝-工单信息同步")
    @PostMapping("/syncOrderData")
    public ResponseEntity syncOrderData(){
        syncDataService.syncOrderData();
        return ControllerUtil.returnSuccess();
    }

    @ApiOperation(value = "万宝-销售订单信息同步",notes = "万宝-销售订单信息同步")
    @PostMapping("/syncSaleOrderData")
    public ResponseEntity syncSaleOrderData(){
        syncDataService.syncSaleOrderData();
        return ControllerUtil.returnSuccess();
    }

    @ApiOperation(value = "万宝-出货通知单信息同步",notes = "万宝-出货通知单信息同步")
    @PostMapping("/syncOutDeliveryData")
    public ResponseEntity syncOutDeliveryData(){
        syncDataService.syncOutDeliveryData();
        return ControllerUtil.returnSuccess();
    }
}
