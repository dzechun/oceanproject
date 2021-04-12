package com.fantechs.provider.api.imes.basic;

import com.fantechs.common.base.general.dto.basic.BaseFactoryDto;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkShopDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.response.ResponseEntity;
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
    ResponseEntity<BaseStorage> detail(@ApiParam(value = "id", required = true) @RequestParam(value = "id") Long id);

    @PostMapping(value = "/smtMaterial/findList")
    @ApiOperation(value = "获取物料信息", notes = "获取物料信息")
    ResponseEntity<List<BaseMaterial>> findSmtMaterialList(@ApiParam(value = "查询对象") @RequestBody SearchBaseMaterial searchBaseMaterial);

    @ApiOperation("根据条件查询物料对应储位信息列表")
    @PostMapping("/smtStorageMaterial/findList")
    ResponseEntity<List<BaseStorageMaterial>> findStorageMaterialList(@ApiParam(value = "查询对象") @RequestBody SearchBaseStorageMaterial searchBaseStorageMaterial);

    @ApiOperation("批量新增物料信息")
    @PostMapping("/smtMaterial/addList")
    ResponseEntity addList(@ApiParam(value = "物料信息集合") @RequestBody List<BaseMaterial> baseMaterials);

    @ApiOperation("批量更新物料信息")
    @PostMapping("/smtMaterial/batchUpdateByCode")
    ResponseEntity batchUpdateByCode(@ApiParam(value = "物料信息集合", required = true) @RequestBody List<BaseMaterial> baseMaterials);

    @ApiOperation("根据条件查询物料信息列表")
    @PostMapping("/smtMaterial/findList")
    ResponseEntity<List<BaseMaterial>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseMaterial searchBaseMaterial);

    @ApiOperation(value = "批量更新储位", notes = "批量更新")
    @PostMapping("/smtStorage/batchUpdate")
    ResponseEntity batchUpdate(@ApiParam(value = "储位集合", required = true) @RequestBody List<BaseStorage> baseStorages);

    @ApiOperation(value = "批量新增储位", notes = "批量新增")
    @PostMapping("/smtStorage/batchSave")
    ResponseEntity batchAdd(@ApiParam(value = "储位集合", required = true) @RequestBody List<BaseStorage> baseStorages);

    @ApiOperation("根据条件查询储位信息列表")
    @PostMapping("/smtStorage/findList")
    ResponseEntity<List<BaseStorage>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseStorage searchBaseStorage);

    @ApiOperation("根据仓库编码批量更新")
    @PostMapping("/smtWarehouse/batchUpdateByCode")
    ResponseEntity batchUpdateWarehouseByCode(@ApiParam(value = "编码必传") @RequestBody List<BaseWarehouse> baseWarehouses);

    @ApiOperation("批量新增仓库信息")
    @PostMapping("/smtWarehouse/batchSave")
    ResponseEntity batchSave(@ApiParam(value = "批量新增") @RequestBody List<BaseWarehouse> baseWarehouses);

    @ApiOperation("根据条件查询仓库信息列表")
    @PostMapping("/smtWarehouse/findList")
    ResponseEntity<List<BaseWarehouse>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseWarehouse searchBaseWarehouse);


    @ApiOperation("根据条件查询线别")
    @PostMapping("/smtProLine/findList")
    ResponseEntity<List<BaseProLine>> selectProLines(@RequestBody(required = false) SearchBaseProLine searchBaseProLine);

    @ApiOperation("根据ID获取储位物料")
    @PostMapping("/smtStorageMaterial/detail")
    ResponseEntity<BaseStorageMaterial> detailStorageMaterial(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("根据条件查询产品BOM")
    @PostMapping("/smtProductBom/findList")
    ResponseEntity<List<BaseProductBomDto>> findProductBomList(@RequestBody SearchBaseProductBom searchBaseProductBom);

    @ApiOperation("获取工序的详情")
    @PostMapping("/smtProcess/detail")
    ResponseEntity<BaseProcess> processDetail(@ApiParam(value = "工序ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("获取工序的详情")
    @PostMapping("/workshopSection/detail")
    ResponseEntity<BaseWorkshopSection> sectionDetail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);


    @ApiOperation("获取产品型号详情")
    @PostMapping("/smtProductModel/detail")
    ResponseEntity<BaseProductModel> productModelDetail(@ApiParam(value = "型号ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @PostMapping(value = "/smtMaterial/detail")
    @ApiOperation(value = "获取物料详情信息", notes = "获取物料详情信息")
    ResponseEntity<BaseMaterial> materialDetail(@ApiParam(value = "物料ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("批量更新物料信息")
    @PostMapping("/smtMaterial/batchUpdate")
    ResponseEntity batchUpdateSmtMaterial(@ApiParam(value = "物料信息集合") @RequestBody List<BaseMaterial> baseMaterials);


    @ApiOperation("查询产品工艺路线")
    @GetMapping("/smtRoute/findConfigureRout")
    ResponseEntity<List<BaseRouteProcess>> findConfigureRout(@ApiParam(value = "routeId", required = true) @RequestParam Long routeId);

    @ApiOperation("厂别信息列表")
    @PostMapping("/smtFactory/findList")
    ResponseEntity<List<BaseFactoryDto>> findFactoryList(@ApiParam(value = "查询对象")@RequestBody SearchBaseFactory searchBaseFactory);

    @ApiOperation("查询工艺路线信息列表")
    @PostMapping("/smtRoute/findList")
    ResponseEntity<List<BaseRoute>> findRouteList(@ApiParam(value = "查询对象")@RequestBody SearchBaseRoute searchBaseRoute);

    @ApiOperation("根据条件查询工序信息列表")
    @PostMapping("/smtProcess/findList")
    ResponseEntity<List<BaseProcess>> findProcessList(@ApiParam(value = "查询对象")@RequestBody(required = false) SearchBaseProcess searchBaseProcess);

    @ApiOperation("查询车间信息列表")
    @PostMapping("/smtWorkShop/findList")
    ResponseEntity<List<BaseWorkShopDto>> findWorkShopList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWorkShop searchBaseWorkShop);

    @ApiOperation("查询部门信息列表")
    @PostMapping("/smtDept/findList")
    ResponseEntity<List<BaseDept>> selectDepts(@ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchBaseDept searchBaseDept);

    @ApiOperation("获取区域详情")
    @PostMapping("/smtWarehouseArea/detail")
    ResponseEntity<BaseWarehouseArea> warehouseAreaDetail(@ApiParam(value = "区域ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("列表")
    @PostMapping("/smtSupplier/findList")
    ResponseEntity<List<BaseSupplier>> findSupplierList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSupplier searchBaseSupplier);
}
