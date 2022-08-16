package com.fantechs.provider.base.controller;

import com.fantechs.common.base.general.dto.basic.*;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.base.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @Auther: bgkun
 * @Date: 2022-02-21
 */
@RestController
@RequestMapping(value = "/wanbaoBase")
@Api(tags = "万宝同步定时任务查询基础数据汇总控制器")
@Slf4j
@Validated
public class WanbaoBaseController {

    // region Service注入

    @Resource
    BaseProLineService proLineService;
    @Resource
    BaseMaterialService materialService;
    @Resource
    BaseRouteService routeService;
    @Resource
    BaseBarcodeRuleSetService barcodeRuleSetService;
    @Resource
    BaseRouteProcessService routeProcessService;
    @Resource
    BaseTabService tabService;
    @Resource
    BaseProductModelService productModelService;
    @Resource
    BaseSupplierService supplierService;
    @Resource
    BaseMaterialOwnerService materialOwnerService;
    @Resource
    BaseProductProcessRouteService productProcessRouteService;
    @Resource
    BaseLabelCategoryService labelCategoryService;

    // endregion

    @ApiOperation("同步工单查询基础数据汇总")
    @PostMapping("/findBySyncOrder")
    public ResponseEntity<WanbaoBaseBySyncDto> findBySyncOrder(){
        List<BaseProLine> proLineList = proLineService.findList(new HashMap<>());
        List<BaseMaterialDto> materialDtoList = materialService.findAll(new HashMap<>());
        List<BaseRoute> routeList = routeService.findList(new HashMap<>());
        List<BaseBarcodeRuleSetDto> barcodeRuleSetDtoList = barcodeRuleSetService.findList(new HashMap<>());
        List<BaseRouteProcess> routeProcessList = routeProcessService.findList(new HashMap<>());

        WanbaoBaseBySyncDto dto = new WanbaoBaseBySyncDto();
        dto.setMaterialDtoList(materialDtoList);
        dto.setBarcodeRuleSetDtoList(barcodeRuleSetDtoList);
        dto.setProLineList(proLineList);
        dto.setRouteList(routeList);
        dto.setRouteProcessList(routeProcessList);
        return ControllerUtil.returnDataSuccess(dto, 1);
    }

    @ApiOperation("同步物料查询基础数据汇总")
    @PostMapping("/findBySyncMaterial")
    public ResponseEntity<WanbaoBaseBySyncDto> findBySyncMaterial(){
        List<BaseMaterialDto> materialDtos = materialService.findAll(new HashMap<>());
        List<BaseTabDto> tabDtos = tabService.findList(new HashMap<>());
        List<BaseBarcodeRuleSetDto> barcodeRuleSetDtos = barcodeRuleSetService.findList(new HashMap<>());

        WanbaoBaseBySyncDto dto = new WanbaoBaseBySyncDto();
        dto.setMaterialDtoList(materialDtos);
        dto.setBaseTabDtoList(tabDtos);
        dto.setBarcodeRuleSetDtoList(barcodeRuleSetDtos);
        return ControllerUtil.returnDataSuccess(dto, 1);
    }

    @ApiOperation("同步销售订单查询基础数据汇总")
    @PostMapping("/findBySyncSaleOrder")
    public ResponseEntity<WanbaoBaseBySyncDto> findBySyncSaleOrder(){
        List<BaseMaterialDto> materialDtos = materialService.findAll(new HashMap<>());
        List<BaseBarcodeRuleSetDto> barcodeRuleSetDtos = barcodeRuleSetService.findList(new HashMap<>());
        List<BaseSupplier> baseSuppliers = supplierService.findAll(new HashMap<>());

        WanbaoBaseBySyncDto dto = new WanbaoBaseBySyncDto();
        dto.setMaterialDtoList(materialDtos);
        dto.setBarcodeRuleSetDtoList(barcodeRuleSetDtos);
        dto.setBaseSupplierList(baseSuppliers);
        return ControllerUtil.returnDataSuccess(dto, 1);
    }

    @ApiOperation("同步销售订单查询基础数据汇总")
    @PostMapping("/findBySyncOutDelivery")
    public ResponseEntity<WanbaoBaseBySyncDto> findBySyncOutDelivery(){
        List<BaseMaterialDto> materialDtos = materialService.findAll(new HashMap<>());
        List<BaseSupplier> baseSuppliers = supplierService.findAll(new HashMap<>());
        List<BaseMaterialOwnerDto> materialOwnerDtos = materialOwnerService.findAll();

        WanbaoBaseBySyncDto dto = new WanbaoBaseBySyncDto();
        dto.setMaterialDtoList(materialDtos);
        dto.setBaseSupplierList(baseSuppliers);
        dto.setMaterialOwnerDtoList(materialOwnerDtos);
        return ControllerUtil.returnDataSuccess(dto, 1);
    }

    @ApiOperation("同步PQMS条码查询基础数据汇总")
    @PostMapping("/findBySyncBarcode")
    public ResponseEntity<WanbaoBaseBySyncDto> findBySyncBarcode(){
        List<BaseProLine> proLineList = proLineService.findList(new HashMap<>());
        List<BaseProductProcessRoute> productProcessRoutes = productProcessRouteService.findList(new HashMap<>());
        List<BaseRouteProcess> baseRouteProcesses = routeProcessService.findList(new HashMap<>());
        List<BaseLabelCategoryDto> labelCategoryDtoList = labelCategoryService.findList(new HashMap<>());

        WanbaoBaseBySyncDto dto = new WanbaoBaseBySyncDto();
        dto.setProLineList(proLineList);
        dto.setProcessRouteList(productProcessRoutes);
        dto.setRouteProcessList(baseRouteProcesses);
        dto.setLabelCategoryDtoList(labelCategoryDtoList);
        return ControllerUtil.returnDataSuccess(dto, 1);
    }
}
