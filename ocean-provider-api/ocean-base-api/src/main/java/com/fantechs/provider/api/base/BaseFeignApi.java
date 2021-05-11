package com.fantechs.provider.api.base;

import com.fantechs.common.base.dto.storage.SmtStoragePalletDto;
import com.fantechs.common.base.entity.storage.SmtStoragePallet;
import com.fantechs.common.base.entity.storage.search.SearchSmtStoragePallet;
import com.fantechs.common.base.general.dto.basic.BaseFactoryDto;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkShopDto;
import com.fantechs.common.base.general.dto.basic.*;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStorageInventoryDetDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStorageInventoryDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionItem;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionType;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventory;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventoryDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStorageInventory;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStorageInventoryDet;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

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

    @ApiOperation("列表")
    @PostMapping("/baseBarCode/findList")
    ResponseEntity<List<BaseBarCodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBarCode searchBaseBarCode);

    @ApiOperation("根据工单ID和条码内容修改条码状态")
    @PostMapping("/baseBarCode/updateByContent")
    ResponseEntity updateByContent(@ApiParam(value = "查询对象")@RequestBody List<BaseBarCodeDet> baseBarCodeDets);

    @ApiOperation("简单文本邮件")
    @GetMapping("/mail/sendSimpleMail")
    ResponseEntity sendSimpleMail(@ApiParam(value = "接收者邮箱",required = true)@RequestParam @NotNull(message = "接收邮件不能为空") String to,
                                  @ApiParam(value = "标题",required = true)@RequestParam String subject,
                                  @ApiParam(value = "内容",required = true)@RequestParam String content);
    @ApiOperation("HTML 文本邮件")
    @GetMapping("/mail/sendHtmlMail")
    ResponseEntity sendHtmlMail(@ApiParam(value = "接收者邮箱",required = true)@RequestParam @NotNull(message = "接收邮件不能为空") String to,
                                @ApiParam(value = "标题",required = true)@RequestParam String subject,
                                @ApiParam(value = "内容",required = true)@RequestParam String content);

    @ApiOperation("获取标签信息列表")
    @PostMapping("/baseLabel/findList")
    ResponseEntity<List<BaseLabelDto>> findLabelList(@ApiParam(value = "查询对象")@RequestBody SearchBaseLabel searchBaseLabel);

    @ApiOperation("根据ID集合获取标签信息列表")
    @PostMapping("/baseLabel/findListByIDs")
    ResponseEntity<List<BaseLabel>> findListByIDs(@ApiParam(value = "查询对象")@RequestBody List<Long> ids);

    @ApiOperation("获取标签类别信息列表")
    @PostMapping("/baseLabelCategory/findList")
    ResponseEntity<List<BaseLabelCategoryDto>> findLabelCategoryList(@ApiParam(value = "查询对象")@RequestBody SearchBaseLabelCategory searchBaseLabelCategory);

    @ApiOperation("根据ID集合获取标签类别列表")
    @PostMapping("/baseLabelCategory/findListByIds")
    ResponseEntity<List<BaseLabelCategory>> findLabelCategoryListByIds(@ApiParam(value = "查询对象")@RequestBody List<Long> ids);

    @ApiOperation("打印")
    @PostMapping("/rabbit/print")
    ResponseEntity print(@RequestBody PrintDto printDto);

    @ApiOperation("列表")
    @PostMapping("/baseBarcodeRuleSpec/findSpec")
    ResponseEntity<List<BaseBarcodeRuleSpec>> findSpec(@ApiParam(value = "查询对象")@RequestBody SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec);

    @ApiOperation(value = "获取最大流水号")
    @PostMapping("/baseBarcodeRule/generateMaxCode")
    ResponseEntity<String> generateMaxCode(
            @ApiParam(value = "条码规则集合")@RequestBody List<BaseBarcodeRuleSpec> list,
            @ApiParam(value = "最大条码数")@RequestParam(required = false) String maxCode);

    @ApiOperation("查询条码规则列表")
    @PostMapping("/baseBarcodeRule/findList")
    ResponseEntity<List<BaseBarcodeRuleDto>> findBarcodeRulList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBarcodeRule searchBaseBarcodeRule);

    @ApiOperation("标签分类ID集合查询条码规则列表")
    @PostMapping("/baseBarcodeRule/findListByBarcodeRuleCategoryIds")
    ResponseEntity<List<BaseBarcodeRuleDto>> findListByBarcodeRuleCategoryIds(@ApiParam(value = "标签分类ID集合")@RequestBody List<Long> ids);

    @ApiOperation("条码规则集合列表")
    @PostMapping("/baseBarcodeRuleSet/findList")
    ResponseEntity<List<BaseBarcodeRuleSetDto>> findBarcodeRuleSetList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBarcodeRuleSet searchBaseBarcodeRuleSet);

    @ApiOperation("条码规则关联集合列表")
    @PostMapping("/baseBarcodeRuleSetDet/findList")
    public ResponseEntity<List<BaseBarcodeRuleSetDetDto>> findBarcodeRuleSetDetList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBarcodeRuleSetDet searchBaseBarcodeRuleSetDet);

    @ApiOperation("生成条码")
    @PostMapping("/baseBarcodeRule/generateCode")
    ResponseEntity<String> generateCode(
            @ApiParam(value = "条码规则集合") @RequestBody List<BaseBarcodeRuleSpec> list,
            @ApiParam(value = "最大条码数") @RequestParam(required = false) String maxCode,
            @ApiParam(value = "产品料号、生产线别、客户料号") @RequestParam(required = false) String code,
            @ApiParam(value = "执行函数参数")@RequestParam (required = false)String params);

    @ApiOperation("获取检验项目列表")
    @PostMapping("/qmsInspectionItem/findList")
    ResponseEntity<List<BaseInspectionItemDto>> findInspectionItemList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionItem searchQmsInspectionItem);

    @ApiOperation("获取检验类型列表")
    @PostMapping("/qmsInspectionType/findList")
    ResponseEntity<List<BaseInspectionTypeDto>> findInspectionTypeList(@ApiParam(value = "查询对象") @RequestBody SearchQmsInspectionType searchQmsInspectionType);


    @ApiOperation("储位库存查询")
    @PostMapping("/smtStorageInventory/findList")
    ResponseEntity<List<WmsInnerStorageInventoryDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchWmsInnerStorageInventory searchWmsInnerStorageInventory);

    @ApiOperation("储位库存新增")
    @PostMapping("/smtStorageInventory/add")
    ResponseEntity<WmsInnerStorageInventory> add(@ApiParam(value = "必传：", required = true) @RequestBody WmsInnerStorageInventory wmsInnerStorageInventory);

    @ApiOperation("储位库存删除")
    @PostMapping("/smtStorageInventory/delete")
    ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids);

    @ApiOperation("储位库存更新")
    @PostMapping("/smtStorageInventory/update")
    ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody WmsInnerStorageInventory wmsInnerStorageInventory);

    @ApiOperation("扣除储位库存")
    @PostMapping("/smtStorageInventory/out")
    ResponseEntity<WmsInnerStorageInventory> out(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerStorageInventory wmsInnerStorageInventory);

    @ApiOperation("储位库存明细新增")
    @PostMapping("/smtStorageInventoryDet/add")
    ResponseEntity add(@ApiParam(value = "必传：", required = true) @RequestBody WmsInnerStorageInventoryDet smtStorageInventoryDet);

    @ApiOperation("储位库存明细列表")
    @PostMapping("/smtStorageInventoryDet/findList")
    ResponseEntity<List<WmsInnerStorageInventoryDetDto>> findStorageInventoryDetList(@ApiParam(value = "查询对象") @RequestBody SearchWmsInnerStorageInventoryDet searchWmsInnerStorageInventoryDet);

    @ApiOperation("储位库存明细修改")
    @PostMapping("/smtStorageInventoryDet/update")
    ResponseEntity updateStorageInventoryDet(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = WmsInnerStorageInventoryDet.update.class) WmsInnerStorageInventoryDet smtStorageInventoryDet);

    @ApiOperation("储位库存明细删除")
    @PostMapping("/smtStorageInventoryDet/delete")
    ResponseEntity deleteStorageInventoryDet(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids);

    @ApiOperation("储位栈板关系表新增")
    @PostMapping("/smtStoragePallet/add")
    ResponseEntity add(@ApiParam(value = "必传：", required = true) @RequestBody SmtStoragePallet smtStoragePallet);

    @ApiOperation("储位与栈板列表")
    @PostMapping("/smtStoragePallet/findList")
    ResponseEntity<List<SmtStoragePalletDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchSmtStoragePallet searchSmtStoragePallet);

    @ApiOperation("储位栈板关系表修改")
    @PostMapping("/smtStoragePallet/update")
    ResponseEntity update(@ApiParam(value = "必传：", required = true) @RequestBody SmtStoragePallet smtStoragePallet);

    @ApiOperation("储位栈板关系表删除")
    @PostMapping("/smtStoragePallet/delete")
    ResponseEntity deleteSmtStoragePallet(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids);

    @ApiOperation("减可放托盘数")
    @PostMapping("/baseStorage/minusSurplusCanPutSalver")
    ResponseEntity minusSurplusCanPutSalver(@ApiParam(value = "库位id", required = true) @RequestParam @NotNull(message = "库位id")Long storageId,
                                            @ApiParam(value = "待减可放托盘数", required = true) @RequestParam @NotNull(message = "待减可放托盘数不能为空") Integer num);

    @ApiOperation("加可放托盘数")
    @PostMapping("/plusSurplusCanPutSalver")
    ResponseEntity plusSurplusCanPutSalver(@ApiParam(value = "库位id", required = true) @RequestParam @NotNull(message = "库位id")Long storageId,
                                                  @ApiParam(value = "待加可放托盘数", required = true) @RequestParam @NotNull(message = "待减可放托盘数不能为空") Integer num);

    @ApiOperation("物料关联标签信息")
    @PostMapping("/baseLabelMaterial/findList")
    ResponseEntity<List<BaseLabelMaterialDto>> findLabelMaterialList(@ApiParam(value = "查询对象")@RequestBody SearchBaseLabelMaterial searchBaseLabelMaterial);

    @ApiOperation(value = "生成条码-Map")
    @PostMapping("/baseBarcodeRule/newGenerateCode")
    ResponseEntity<String> newGenerateCode(
            @ApiParam(value = "条码规则集合")@RequestBody List<BaseBarcodeRuleSpec> list,
            @ApiParam(value = "最大条码数")@RequestParam(required = false) String maxCode,
            @ApiParam(value = "产品料号、生产线别、客户料号")@RequestParam (required = false) Map<String,Object> map,
            @ApiParam(value = "执行函数参数")@RequestParam (required = false)String params);
}
