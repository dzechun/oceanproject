package com.fantechs.provider.api.base;

import com.fantechs.common.base.general.dto.basic.BaseFactoryDto;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkShopDto;
import com.fantechs.common.base.general.dto.basic.*;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(name = "ocean-base")
public interface BaseFeignApi {

    @ApiOperation("页签信息列表")
    @PostMapping("/baseTab/findList")
    ResponseEntity<List<BaseTabDto>> findTabList(@ApiParam(value = "查询对象") @RequestBody SearchBaseTab searchBaseTab);

    @ApiOperation(value = "新增页签", notes = "新增页签")
    @PostMapping("/baseTab/add")
    ResponseEntity addTab(@ApiParam(value = "必传：", required = true) @RequestBody @Validated BaseTab baseTab);

    @ApiOperation(value = "批量新增页签",notes = "批量新增页签")
    @PostMapping("/baseTab/insertList")
    ResponseEntity insertTabList(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<BaseTab> baseTabs);

    @ApiOperation("删除页签")
    @PostMapping("/baseTab/delete")
    ResponseEntity deleteTab(@ApiParam(value = "页签集合") @RequestBody @Validated List<BaseTab> baseTabs);

    @ApiOperation("修改页签")
    @PostMapping("/baseTab/update")
    ResponseEntity updateTab(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = BaseTab.update.class) BaseTab baseTab);

    @ApiOperation("列表")
    @PostMapping("/baseTeam/findList")
    ResponseEntity<List<BaseTeamDto>> findTeamList(@ApiParam(value = "查询对象") @RequestBody SearchBaseTeam searchBaseTeam);

    @ApiOperation("列表")
    @PostMapping("/basePlateParts/findList")
    ResponseEntity<List<BasePlatePartsDto>> findPlatePartsList(@ApiParam(value = "查询对象")@RequestBody SearchBasePlateParts searchBasePlateParts);

    @ApiOperation("列表")
    @PostMapping("/basePlatePartsDet/findList")
    ResponseEntity<List<BasePlatePartsDetDto>> findPlatePartsDetList(@ApiParam(value = "查询对象")@RequestBody SearchBasePlatePartsDet searchBasePlatePartsDet);

    @ApiOperation("列表")
    @PostMapping("/baseUnitPrice/findList")
    ResponseEntity<List<BaseUnitPriceDto>> findUnitPriceList(@ApiParam(value = "查询对象")@RequestBody SearchBaseUnitPrice searchBaseUnitPrice);

    @ApiOperation("查询产品族信息列表")
    @PostMapping("/baseProductFamily/findList")
    ResponseEntity<List<BaseProductFamilyDto>> findProductFamilyList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductFamily searchBaseProductFamily);

    @ApiOperation("查询员工工种关系列表")
    @PostMapping("/baseStaffProcess/findList")
    ResponseEntity<List<BaseStaffProcess>> findStaffProcessList(@ApiParam(value = "查询对象")@RequestBody SearchBaseStaffProcess searchBaseStaffProcess);

    @ApiOperation("查询组织信息列表")
    @PostMapping("/baseOrganization/findList")
    ResponseEntity<List<BaseOrganizationDto>> findOrganizationList(@ApiParam(value = "查询对象") @RequestBody SearchBaseOrganization searchBaseOrganization);

    @ApiOperation("列表")
    @PostMapping("/baseWarning/findList")
    ResponseEntity<List<BaseWarningDto>> findBaseWarningList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWarning searchBaseWarning);

    @PostMapping("/baseStorage/detail")
    @ApiOperation(value = "获取储位信息", notes = "获取电子标签控制器和储位信息")
    ResponseEntity<BaseStorage> detail(@ApiParam(value = "id", required = true) @RequestParam(value = "id") Long id);

    @PostMapping(value = "/baseMaterial/findList")
    @ApiOperation(value = "获取物料信息", notes = "获取物料信息")
    ResponseEntity<List<BaseMaterial>> findSmtMaterialList(@ApiParam(value = "查询对象") @RequestBody SearchBaseMaterial searchBaseMaterial);

    @ApiOperation("根据条件查询物料对应储位信息列表")
    @PostMapping("/baseStorageMaterial/findList")
    ResponseEntity<List<BaseStorageMaterial>> findStorageMaterialList(@ApiParam(value = "查询对象") @RequestBody SearchBaseStorageMaterial searchBaseStorageMaterial);

    @ApiOperation("批量新增物料信息")
    @PostMapping("/baseMaterial/addList")
    ResponseEntity addList(@ApiParam(value = "物料信息集合") @RequestBody List<BaseMaterial> baseMaterials);

    @ApiOperation("批量更新物料信息")
    @PostMapping("/baseMaterial/batchUpdateByCode")
    ResponseEntity batchUpdateByCode(@ApiParam(value = "物料信息集合", required = true) @RequestBody List<BaseMaterial> baseMaterials);

    @ApiOperation("根据条件查询物料信息列表")
    @PostMapping("/baseMaterial/findList")
    ResponseEntity<List<BaseMaterial>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseMaterial searchBaseMaterial);

    @ApiOperation(value = "批量更新储位", notes = "批量更新")
    @PostMapping("/baseStorage/batchUpdate")
    ResponseEntity batchUpdate(@ApiParam(value = "储位集合", required = true) @RequestBody List<BaseStorage> baseStorages);

    @ApiOperation(value = "批量新增储位", notes = "批量新增")
    @PostMapping("/baseStorage/batchSave")
    ResponseEntity batchAdd(@ApiParam(value = "储位集合", required = true) @RequestBody List<BaseStorage> baseStorages);

    @ApiOperation("根据条件查询储位信息列表")
    @PostMapping("/baseStorage/findList")
    ResponseEntity<List<BaseStorage>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseStorage searchBaseStorage);

    @ApiOperation("根据仓库编码批量更新")
    @PostMapping("/baseWarehouse/batchUpdateByCode")
    ResponseEntity batchUpdateWarehouseByCode(@ApiParam(value = "编码必传") @RequestBody List<BaseWarehouse> baseWarehouses);

    @ApiOperation("批量新增仓库信息")
    @PostMapping("/baseWarehouse/batchSave")
    ResponseEntity batchSave(@ApiParam(value = "批量新增") @RequestBody List<BaseWarehouse> baseWarehouses);

    @ApiOperation("根据条件查询仓库信息列表")
    @PostMapping("/baseWarehouse/findList")
    ResponseEntity<List<BaseWarehouse>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseWarehouse searchBaseWarehouse);


    @ApiOperation("根据条件查询线别")
    @PostMapping("/baseProLine/findList")
    ResponseEntity<List<BaseProLine>> selectProLines(@RequestBody(required = false) SearchBaseProLine searchBaseProLine);

    @ApiOperation("根据ID获取储位物料")
    @PostMapping("/baseStorageMaterial/detail")
    ResponseEntity<BaseStorageMaterial> detailStorageMaterial(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("根据条件查询产品BOM")
    @PostMapping("/baseProductBom/findList")
    ResponseEntity<List<BaseProductBomDto>> findProductBomList(@RequestBody SearchBaseProductBom searchBaseProductBom);

    @ApiOperation("获取工序的详情")
    @PostMapping("/baseProcess/detail")
    ResponseEntity<BaseProcess> processDetail(@ApiParam(value = "工序ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("获取工序的详情")
    @PostMapping("/workshopSection/detail")
    ResponseEntity<BaseWorkshopSection> sectionDetail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);


    @ApiOperation("获取产品型号详情")
    @PostMapping("/baseProductModel/detail")
    ResponseEntity<BaseProductModel> productModelDetail(@ApiParam(value = "型号ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @PostMapping(value = "/baseMaterial/detail")
    @ApiOperation(value = "获取物料详情信息", notes = "获取物料详情信息")
    ResponseEntity<BaseMaterial> materialDetail(@ApiParam(value = "物料ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("批量更新物料信息")
    @PostMapping("/baseMaterial/batchUpdate")
    ResponseEntity batchUpdateSmtMaterial(@ApiParam(value = "物料信息集合") @RequestBody List<BaseMaterial> baseMaterials);

    @ApiOperation("查询产品工艺路线")
    @GetMapping("/baseRoute/findConfigureRout")
    ResponseEntity<List<BaseRouteProcess>> findConfigureRout(@ApiParam(value = "routeId", required = true) @RequestParam Long routeId);

    @ApiOperation("厂别信息列表")
    @PostMapping("/baseFactory/findList")
    ResponseEntity<List<BaseFactoryDto>> findFactoryList(@ApiParam(value = "查询对象")@RequestBody SearchBaseFactory searchBaseFactory);

    @ApiOperation("查询工艺路线信息列表")
    @PostMapping("/baseRoute/findList")
    ResponseEntity<List<BaseRoute>> findRouteList(@ApiParam(value = "查询对象")@RequestBody SearchBaseRoute searchBaseRoute);

    @ApiOperation("根据条件查询工序信息列表")
    @PostMapping("/baseProcess/findList")
    ResponseEntity<List<BaseProcess>> findProcessList(@ApiParam(value = "查询对象")@RequestBody(required = false) SearchBaseProcess searchBaseProcess);

    @ApiOperation("查询车间信息列表")
    @PostMapping("/baseWorkShop/findList")
    ResponseEntity<List<BaseWorkShopDto>> findWorkShopList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWorkShop searchBaseWorkShop);

    @ApiOperation("查询部门信息列表")
    @PostMapping("/baseDept/findList")
    ResponseEntity<List<BaseDept>> selectDepts(@ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchBaseDept searchBaseDept);

    @ApiOperation("获取区域详情")
    @PostMapping("/baseWarehouseArea/detail")
    ResponseEntity<BaseWarehouseArea> warehouseAreaDetail(@ApiParam(value = "区域ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("列表")
    @PostMapping("/baseSupplier/findList")
    ResponseEntity<List<BaseSupplier>> findSupplierList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSupplier searchBaseSupplier);
}
