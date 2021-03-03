package com.fantechs.provider.api.imes.basic;

import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.dto.basic.SmtProductBomDto;
import com.fantechs.common.base.dto.basic.SmtWorkShopDto;
import com.fantechs.common.base.entity.basic.*;
import com.fantechs.common.base.entity.basic.search.*;
import com.fantechs.common.base.general.entity.mes.pm.SmtProcessListProcess;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderBarcodePool;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(name = "ocean-imes-basic")
public interface BasicFeignApi {

    @PostMapping("/smtStorage/detail")
    @ApiOperation(value = "获取储位信息", notes = "获取电子标签控制器和储位信息")
    ResponseEntity<SmtStorage> detail(@ApiParam(value = "id", required = true) @RequestParam(value = "id") Long id);

    @PostMapping(value = "/smtMaterial/findList")
    @ApiOperation(value = "获取物料信息", notes = "获取物料信息")
    ResponseEntity<List<SmtMaterial>> findSmtMaterialList(@ApiParam(value = "查询对象") @RequestBody SearchSmtMaterial searchSmtMaterial);

    @ApiOperation("根据条件查询物料对应储位信息列表")
    @PostMapping("/smtStorageMaterial/findList")
    ResponseEntity<List<SmtStorageMaterial>> findStorageMaterialList(@ApiParam(value = "查询对象") @RequestBody SearchSmtStorageMaterial searchSmtStorageMaterial);

    @ApiOperation("批量新增物料信息")
    @PostMapping("/smtMaterial/addList")
    ResponseEntity addList(@ApiParam(value = "物料信息集合") @RequestBody List<SmtMaterial> smtMaterials);

    @ApiOperation("批量更新物料信息")
    @PostMapping("/smtMaterial/batchUpdateByCode")
    ResponseEntity batchUpdateByCode(@ApiParam(value = "物料信息集合", required = true) @RequestBody List<SmtMaterial> smtMaterials);

    @ApiOperation("根据条件查询物料信息列表")
    @PostMapping("/smtMaterial/findList")
    ResponseEntity<List<SmtMaterial>> findList(@ApiParam(value = "查询对象") @RequestBody SearchSmtMaterial searchSmtMaterial);

    @ApiOperation(value = "批量更新储位", notes = "批量更新")
    @PostMapping("/smtStorage/batchUpdate")
    ResponseEntity batchUpdate(@ApiParam(value = "储位集合", required = true) @RequestBody List<SmtStorage> smtStorages);

    @ApiOperation(value = "批量新增储位", notes = "批量新增")
    @PostMapping("/smtStorage/batchSave")
    ResponseEntity batchAdd(@ApiParam(value = "储位集合", required = true) @RequestBody List<SmtStorage> smtStorages);

    @ApiOperation("根据条件查询储位信息列表")
    @PostMapping("/smtStorage/findList")
    ResponseEntity<List<SmtStorage>> findList(@ApiParam(value = "查询对象") @RequestBody SearchSmtStorage searchSmtStorage);

    @ApiOperation("根据仓库编码批量更新")
    @PostMapping("/smtWarehouse/batchUpdateByCode")
    ResponseEntity batchUpdateWarehouseByCode(@ApiParam(value = "编码必传") @RequestBody List<SmtWarehouse> smtWarehouses);

    @ApiOperation("批量新增仓库信息")
    @PostMapping("/smtWarehouse/batchSave")
    ResponseEntity batchSave(@ApiParam(value = "批量新增") @RequestBody List<SmtWarehouse> smtWarehouses);

    @ApiOperation("根据条件查询仓库信息列表")
    @PostMapping("/smtWarehouse/findList")
    ResponseEntity<List<SmtWarehouse>> findList(@ApiParam(value = "查询对象") @RequestBody SearchSmtWarehouse searchSmtWarehouse);


    @ApiOperation("根据条件查询线别")
    @PostMapping("/smtProLine/findList")
    ResponseEntity<List<SmtProLine>> selectProLines(@RequestBody(required = false) SearchSmtProLine searchSmtProLine);

    @ApiOperation("根据ID获取储位物料")
    @PostMapping("/smtStorageMaterial/detail")
    ResponseEntity<SmtStorageMaterial> detailStorageMaterial(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("根据条件查询产品BOM")
    @PostMapping("/smtProductBom/findList")
    ResponseEntity<List<SmtProductBomDto>> findProductBomList(@RequestBody SearchSmtProductBom searchSmtProductBom);

    @ApiOperation("获取工序的详情")
    @PostMapping("/smtProcess/detail")
    ResponseEntity<SmtProcess> processDetail(@ApiParam(value = "工序ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("获取工序的详情")
    @PostMapping("/workshopSection/detail")
    ResponseEntity<SmtWorkshopSection> sectionDetail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);


    @ApiOperation("获取产品型号详情")
    @PostMapping("/smtProductModel/detail")
    ResponseEntity<SmtProductModel> productModelDetail(@ApiParam(value = "型号ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @PostMapping(value = "/smtMaterial/detail")
    @ApiOperation(value = "获取物料详情信息", notes = "获取物料详情信息")
    ResponseEntity<SmtMaterial> materialDetail(@ApiParam(value = "物料ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("批量更新物料信息")
    @PostMapping("/smtMaterial/batchUpdate")
    ResponseEntity batchUpdateSmtMaterial(@ApiParam(value = "物料信息集合") @RequestBody List<SmtMaterial> smtMaterials);


    @ApiOperation("查询产品工艺路线")
    @GetMapping("/smtRoute/findConfigureRout")
    ResponseEntity<List<SmtRouteProcess>> findConfigureRout(@ApiParam(value = "routeId", required = true) @RequestParam Long routeId);

    @ApiOperation("厂别信息列表")
    @PostMapping("/smtWorkShop/findList")
    ResponseEntity<List<SmtFactoryDto>> findFactoryList(@ApiParam(value = "查询对象")@RequestBody SearchSmtFactory searchSmtFactory);

    @ApiOperation("查询工艺路线信息列表")
    @PostMapping("/smtRoute/findList")
    ResponseEntity<List<SmtRoute>> findRouteList(@ApiParam(value = "查询对象")@RequestBody SearchSmtRoute searchSmtRoute);

    @ApiOperation("根据条件查询工序信息列表")
    @PostMapping("/smtProcess/findList")
    ResponseEntity<List<SmtProcess>> findProcessList(@ApiParam(value = "查询对象")@RequestBody(required = false) SearchSmtProcess searchSmtProcess);

    @ApiOperation("查询车间信息列表")
    @PostMapping("/smtWorkShop/findList")
    ResponseEntity<List<SmtWorkShopDto>> findWorkShopList(@ApiParam(value = "查询对象")@RequestBody SearchSmtWorkShop searchSmtWorkShop);
}
