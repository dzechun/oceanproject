package com.fantechs.provider.api.base;

import com.fantechs.common.base.general.dto.basic.*;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.*;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionItem;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionType;
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
import java.math.BigDecimal;
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

    @ApiOperation(value = "批量新增页签", notes = "批量新增页签")
    @PostMapping("/baseTab/insertList")
    ResponseEntity insertTabList(@ApiParam(value = "必传：", required = true) @RequestBody @Validated List<BaseTab> baseTabs);

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
    ResponseEntity<List<BasePlatePartsDto>> findPlatePartsList(@ApiParam(value = "查询对象") @RequestBody SearchBasePlateParts searchBasePlateParts);

    @ApiOperation("列表")
    @PostMapping("/basePlatePartsDet/findList")
    ResponseEntity<List<BasePlatePartsDetDto>> findPlatePartsDetList(@ApiParam(value = "查询对象") @RequestBody SearchBasePlatePartsDet searchBasePlatePartsDet);

    @ApiOperation("列表")
    @PostMapping("/baseUnitPrice/findList")
    ResponseEntity<List<BaseUnitPriceDto>> findUnitPriceList(@ApiParam(value = "查询对象") @RequestBody SearchBaseUnitPrice searchBaseUnitPrice);

    @ApiOperation("查询产品族信息列表")
    @PostMapping("/baseProductFamily/findList")
    ResponseEntity<List<BaseProductFamilyDto>> findProductFamilyList(@ApiParam(value = "查询对象") @RequestBody SearchBaseProductFamily searchBaseProductFamily);

    @ApiOperation("查询员工工种关系列表")
    @PostMapping("/baseStaffProcess/findList")
    ResponseEntity<List<BaseStaffProcess>> findStaffProcessList(@ApiParam(value = "查询对象") @RequestBody SearchBaseStaffProcess searchBaseStaffProcess);

    @ApiOperation("查询组织信息列表")
    @PostMapping("/baseOrganization/findList")
    ResponseEntity<List<BaseOrganizationDto>> findOrganizationList(@ApiParam(value = "查询对象") @RequestBody SearchBaseOrganization searchBaseOrganization);

    @ApiOperation("列表")
    @PostMapping("/baseWarning/findList")
    ResponseEntity<List<BaseWarningDto>> findBaseWarningList(@ApiParam(value = "查询对象") @RequestBody SearchBaseWarning searchBaseWarning);

    @ApiOperation(value = "获取储位信息", notes = "获取电子标签控制器和储位信息")
    @PostMapping("/baseStorage/detail")
    ResponseEntity<BaseStorage> detail(@ApiParam(value = "id", required = true) @RequestParam(value = "id") Long id);

    @ApiOperation(value = "获取物料信息", notes = "获取物料信息")
    @PostMapping(value = "/baseMaterial/findList")
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

    @ApiOperation("获取详情")
    @PostMapping("/baseWarehouse/detail")
    ResponseEntity<BaseWarehouse> getWarehouseDetail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("根据条件查询线别")
    @PostMapping("/baseProLine/findList")
    ResponseEntity<List<BaseProLine>> selectProLines(@RequestBody(required = false) SearchBaseProLine searchBaseProLine);

    @ApiOperation("查询线别详情")
    @PostMapping("/baseProLine/detail")
    ResponseEntity<BaseProLine> selectProLinesDetail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

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
    @PostMapping("/baseWorkshopSection/detail")
    ResponseEntity<BaseWorkshopSection> sectionDetail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("根据条件查询工段信息列表")
    @PostMapping("/baseWorkshopSection/findList")
    ResponseEntity<List<BaseWorkshopSection>> findWorkshopSectionList(@RequestBody SearchBaseWorkshopSection searchBaseWorkshopSection);


    @ApiOperation("获取产品型号详情")
    @PostMapping("/baseProductModel/detail")
    ResponseEntity<BaseProductModel> productModelDetail(@ApiParam(value = "型号ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @PostMapping(value = "/baseMaterial/detail")
    @ApiOperation(value = "获取物料详情信息", notes = "获取物料详情信息")
    ResponseEntity<BaseMaterial> materialDetail(@ApiParam(value = "物料ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long materialId);

    @ApiOperation("批量更新物料信息")
    @PostMapping("/baseMaterial/batchUpdate")
    ResponseEntity batchUpdateSmtMaterial(@ApiParam(value = "物料信息集合") @RequestBody List<BaseMaterial> baseMaterials);

    @ApiOperation("查询产品工艺路线")
    @GetMapping("/baseRoute/findConfigureRout")
    ResponseEntity<List<BaseRouteProcess>> findConfigureRout(@ApiParam(value = "routeId", required = true) @RequestParam Long routeId);

    @ApiOperation("厂别信息列表")
    @PostMapping("/baseFactory/findList")
    ResponseEntity<List<BaseFactoryDto>> findFactoryList(@ApiParam(value = "查询对象") @RequestBody SearchBaseFactory searchBaseFactory);

    @ApiOperation("查询工艺路线信息列表")
    @PostMapping("/baseRoute/findList")
    ResponseEntity<List<BaseRoute>> findRouteList(@ApiParam(value = "查询对象") @RequestBody SearchBaseRoute searchBaseRoute);

    @ApiOperation("获取工艺路线详情")
    @PostMapping("/baseRoute/detail")
    ResponseEntity<BaseRoute> routeDetail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("根据条件查询工序信息列表")
    @PostMapping("/baseProcess/findList")
    ResponseEntity<List<BaseProcess>> findProcessList(@ApiParam(value = "查询对象") @RequestBody(required = false) SearchBaseProcess searchBaseProcess);

    @ApiOperation("查询车间信息列表")
    @PostMapping("/baseWorkShop/findList")
    ResponseEntity<List<BaseWorkShopDto>> findWorkShopList(@ApiParam(value = "查询对象") @RequestBody SearchBaseWorkShop searchBaseWorkShop);

    @ApiOperation("查询部门信息列表")
    @PostMapping("/baseDept/findList")
    ResponseEntity<List<BaseDept>> selectDepts(@ApiParam(value = "查询条件，请参考Model说明") @RequestBody(required = false) SearchBaseDept searchBaseDept);

    @ApiOperation("获取区域详情")
    @PostMapping("/baseWarehouseArea/detail")
    ResponseEntity<BaseWarehouseArea> warehouseAreaDetail(@ApiParam(value = "区域ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("列表")
    @PostMapping("/baseSupplier/findList")
    ResponseEntity<List<BaseSupplier>> findSupplierList(@ApiParam(value = "查询对象") @RequestBody SearchBaseSupplier searchBaseSupplier);

    @ApiOperation("列表")
    @PostMapping("/baseBarCode/findList")
    ResponseEntity<List<BaseBarCodeDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseBarCode searchBaseBarCode);

    @ApiOperation("根据工单ID和条码内容修改条码状态")
    @PostMapping("/baseBarCode/updateByContent")
    ResponseEntity updateByContent(@ApiParam(value = "查询对象") @RequestBody List<BaseBarCodeDet> baseBarCodeDets);

    @ApiOperation("简单文本邮件")
    @GetMapping("/mail/sendSimpleMail")
    ResponseEntity sendSimpleMail(@ApiParam(value = "接收者邮箱", required = true) @RequestParam @NotNull(message = "接收邮件不能为空") String to,
                                  @ApiParam(value = "标题", required = true) @RequestParam String subject,
                                  @ApiParam(value = "内容", required = true) @RequestParam String content);

    @ApiOperation("HTML 文本邮件")
    @GetMapping("/mail/sendHtmlMail")
    ResponseEntity sendHtmlMail(@ApiParam(value = "接收者邮箱", required = true) @RequestParam @NotNull(message = "接收邮件不能为空") String to,
                                @ApiParam(value = "标题", required = true) @RequestParam String subject,
                                @ApiParam(value = "内容", required = true) @RequestParam String content);

    @ApiOperation("获取标签信息列表")
    @PostMapping("/baseLabel/findList")
    ResponseEntity<List<BaseLabelDto>> findLabelList(@ApiParam(value = "查询对象") @RequestBody SearchBaseLabel searchBaseLabel);

    @ApiOperation("根据ID集合获取标签信息列表")
    @PostMapping("/baseLabel/findListByIDs")
    ResponseEntity<List<BaseLabel>> findListByIDs(@ApiParam(value = "查询对象") @RequestBody List<Long> ids);

    @ApiOperation("获取标签类别信息列表")
    @PostMapping("/baseLabelCategory/findList")
    ResponseEntity<List<BaseLabelCategoryDto>> findLabelCategoryList(@ApiParam(value = "查询对象") @RequestBody SearchBaseLabelCategory searchBaseLabelCategory);

    @ApiOperation("根据ID集合获取标签类别列表")
    @PostMapping("/baseLabelCategory/findListByIds")
    ResponseEntity<List<BaseLabelCategory>> findLabelCategoryListByIds(@ApiParam(value = "查询对象") @RequestBody List<Long> ids);

    @ApiOperation("根据ID集合获取标签类别列表")
    @PostMapping("/baseLabelCategory/detail")
    ResponseEntity<BaseLabelCategory> findLabelCategoryDetail(@ApiParam(value = "标签分类id", required = true) @RequestParam @NotNull(message = "labelCategoryId不能为空") Long id);

    @ApiOperation("打印")
    @PostMapping("/rabbit/print")
    ResponseEntity print(@RequestBody PrintDto printDto);

    @ApiOperation("列表")
    @PostMapping("/baseBarcodeRuleSpec/findSpec")
    ResponseEntity<List<BaseBarcodeRuleSpec>> findSpec(@ApiParam(value = "查询对象") @RequestBody SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec);

    @ApiOperation(value = "获取最大流水号")
    @PostMapping("/baseBarcodeRule/generateMaxCode")
    ResponseEntity<String> generateMaxCode(
            @ApiParam(value = "条码规则集合") @RequestBody List<BaseBarcodeRuleSpec> list,
            @ApiParam(value = "最大条码数") @RequestParam(required = false) String maxCode);

    @ApiOperation("查询条码规则详情")
    @PostMapping("/baseBarcodeRule/detail")
    ResponseEntity<BaseBarcodeRule> baseBarcodeRuleDetail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("查询条码规则列表")
    @PostMapping("/baseBarcodeRule/findList")
    ResponseEntity<List<BaseBarcodeRuleDto>> findBarcodeRulList(@ApiParam(value = "查询对象") @RequestBody SearchBaseBarcodeRule searchBaseBarcodeRule);

    @ApiOperation("标签分类ID集合查询条码规则列表")
    @PostMapping("/baseBarcodeRule/findListByBarcodeRuleCategoryIds")
    ResponseEntity<List<BaseBarcodeRuleDto>> findListByBarcodeRuleCategoryIds(@ApiParam(value = "标签分类ID集合") @RequestBody List<Long> ids);

    @ApiOperation("条码规则集合列表")
    @PostMapping("/baseBarcodeRuleSet/findList")
    ResponseEntity<List<BaseBarcodeRuleSetDto>> findBarcodeRuleSetList(@ApiParam(value = "查询对象") @RequestBody SearchBaseBarcodeRuleSet searchBaseBarcodeRuleSet);

    @ApiOperation("条码规则关联集合列表")
    @PostMapping("/baseBarcodeRuleSetDet/findList")
    ResponseEntity<List<BaseBarcodeRuleSetDetDto>> findBarcodeRuleSetDetList(@ApiParam(value = "查询对象") @RequestBody SearchBaseBarcodeRuleSetDet searchBaseBarcodeRuleSetDet);

    @ApiOperation("生成条码")
    @PostMapping("/baseBarcodeRule/generateCode")
    ResponseEntity<String> generateCode(
            @ApiParam(value = "条码规则集合") @RequestBody List<BaseBarcodeRuleSpec> list,
            @ApiParam(value = "最大条码数") @RequestParam(required = false) String maxCode,
            @ApiParam(value = "产品料号、生产线别、客户料号") @RequestParam(required = false) String code,
            @ApiParam(value = "执行函数参数") @RequestParam(required = false) String params);

    @ApiOperation("获取检验项目列表")
    @PostMapping("/baseInspectionItem/findList")
    ResponseEntity<List<BaseInspectionItemDto>> findInspectionItemList(@ApiParam(value = "查询对象") @RequestBody SearchQmsInspectionItem searchQmsInspectionItem);

    @ApiOperation("获取检验类型列表")
    @PostMapping("/baseInspectionType/findList")
    ResponseEntity<List<BaseInspectionTypeDto>> findInspectionTypeList(@ApiParam(value = "查询对象") @RequestBody SearchQmsInspectionType searchQmsInspectionType);

    @ApiOperation("减可放托盘数")
    @PostMapping("/baseStorage/minusSurplusCanPutSalver")
    ResponseEntity minusSurplusCanPutSalver(@ApiParam(value = "库位id", required = true) @RequestParam @NotNull(message = "库位id") Long storageId,
                                            @ApiParam(value = "待减可放托盘数", required = true) @RequestParam @NotNull(message = "待减可放托盘数不能为空") Integer num);

    @ApiOperation("加可放托盘数")
    @PostMapping("/baseStorage/plusSurplusCanPutSalver")
    ResponseEntity plusSurplusCanPutSalver(@ApiParam(value = "库位id", required = true) @RequestParam @NotNull(message = "库位id") Long storageId,
                                           @ApiParam(value = "待加可放托盘数", required = true) @RequestParam @NotNull(message = "待减可放托盘数不能为空") Integer num);

    @ApiOperation("物料关联标签信息")
    @PostMapping("/baseLabelMaterial/findList")
    ResponseEntity<List<BaseLabelMaterialDto>> findLabelMaterialList(@ApiParam(value = "查询对象") @RequestBody SearchBaseLabelMaterial searchBaseLabelMaterial);

    @ApiOperation("获取包装规格信息详情")
    @PostMapping("/basePackageSpecification/detail")
    ResponseEntity<BasePackageSpecification> BasePackageSpecificationDetail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("获取包装规格信息列表")
    @PostMapping("/basePackageSpecification/findByMaterialProcess")
    ResponseEntity<List<BasePackageSpecificationDto>> findBasePackageSpecificationList(@ApiParam(value = "查询对象") @RequestBody SearchBasePackageSpecification searchBasePackageSpecification);

    @ApiOperation("获取工位详情")
    @PostMapping("/baseStation/detail")
    ResponseEntity<BaseStation> findStationDetail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("根据条件查询工位列表")
    @PostMapping("/baseStation/findList")
    ResponseEntity<List<BaseStation>> findBaseStationList(@ApiParam(value = "查询对象") @RequestBody SearchBaseStation searchBaseStation);

    @ApiOperation(value = "生成条码-Map")
    @PostMapping("/baseBarcodeRule/newGenerateCode")
    ResponseEntity<String> newGenerateCode(
            @ApiParam(value = "条码规则集合") @RequestBody List<BaseBarcodeRuleSpec> list,
            @ApiParam(value = "最大条码数") @RequestParam(required = false) String maxCode,
            @ApiParam(value = "产品料号、生产线别、客户料号") @RequestParam(required = false) Map<String, Object> map,
            @ApiParam(value = "执行函数参数") @RequestParam(required = false) String params);

    @ApiOperation("物料编码关联客户料号列表")
    @PostMapping("/baseMaterialSupplier/findList")
    ResponseEntity<List<BaseMaterialSupplierDto>> findBaseMaterialSupplierList(@ApiParam(value = "查询对象") @RequestBody SearchBaseMaterialSupplier searchBaseMaterialSupplier);

    @ApiOperation("根据条件查询货主列表")
    @PostMapping("/baseMaterialOwner/findList")
    ResponseEntity<List<BaseMaterialOwnerDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseMaterialOwner searchBaseMaterialOwner);

    @ApiOperation("根据条件查询收货人信息")
    @PostMapping("/baseConsignee/findList")
    ResponseEntity<List<BaseConsignee>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseConsignee searchBaseConsignee);

    @ApiOperation("查询对应抽样方案的AC和RE和样本数")
    @PostMapping("/baseSampleProcess/getAcReQty")
    ResponseEntity<BaseSampleProcess> getAcReQty(@ApiParam(value = "抽样过程id",required = true) @RequestParam @NotNull(message="抽样过程id不能为空")Long sampleProcessId,
                                                        @ApiParam(value = "单据数量",required = true) @RequestParam @NotNull(message="单据数量不能为空") BigDecimal orderQty);

    @ApiOperation("根据条件查询物料特征码信息列表")
    @PostMapping("/baseSignature/findList")
    ResponseEntity<List<BaseSignature>> findSignatureList(@ApiParam(value = "查询对象")@RequestBody SearchBaseSignature searchBaseSignature);

    @ApiOperation("接口新增或修改供应商（客户）信息")
    @PostMapping("/baseSupplier/saveByApi")
    ResponseEntity saveByApi(@ApiParam(value = "必传：supplierCode、supplierName",required = true)@RequestBody @Validated BaseSupplier baseSupplier);

    @ApiOperation("根据条件查询产品关键事项列表")
    @PostMapping("/baseProductionKeyIssues/findList")
    ResponseEntity<List<BaseProductionKeyIssues>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProductionKeyIssues searchBaseProductionKeyIssues);

    @ApiOperation("接口新增或修改产品bom信息")
    @PostMapping("/baseProductBom/addOrUpdate")
    ResponseEntity<BaseProductBom> addOrUpdate(@ApiParam(value = "必传：productBomCode、materialId",required = true)@RequestBody @Validated BaseProductBom baseProductBom);

    @ApiOperation("接口新增或修改产品bom详情表信息")
    @PostMapping("/baseProductBomDet/addOrUpdate")
    ResponseEntity<BaseProductBomDet> addOrUpdate(@ApiParam(value = "必传：productBomCode、materialId",required = true)@RequestBody @Validated BaseProductBomDet bseProductBomDet);

    @ApiOperation("查询产品bom详情表信息")
    @PostMapping("/baseProductBomDet/findList")
    ResponseEntity<List<BaseProductBomDet>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseProductBomDet searchBaseProductBomDet);

    @ApiOperation("批量删除bom详情表信息")
    @PostMapping("/baseProductBomDet/batchApiDelete")
    ResponseEntity batchApiDelete(@ApiParam(value = "抽样过程id",required = true) @RequestParam @NotNull(message="productBomId不能为空") Long productBomId);

    @ApiOperation("根据条件查询生产线信息列表")
    @PostMapping("/baseProLine/findList")
    ResponseEntity<List<BaseProLine>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseProLine searchBaseProLine);

    @ApiOperation("获取详情")
    @PostMapping("/baseProLine/detail")
    ResponseEntity<BaseProLine> getProLineDetail(@ApiParam(value = "ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id);

    @ApiOperation("新增或修改产线表信息")
    @PostMapping("/baseProLine/addOrUpdate")
    ResponseEntity<BaseProLine> addOrUpdate(@ApiParam(value = "必传：baseProLine",required = true)@RequestBody @Validated BaseProLine baseProLine);

    @ApiOperation("新增或修改工段表信息")
    @PostMapping("/baseWorkshopSection/addOrUpdate")
    ResponseEntity<BaseWorkshopSection> addOrUpdate(@ApiParam(value = "必传：baseWorkshopSection",required = true)@RequestBody @Validated BaseWorkshopSection baseWorkshopSection);

    @ApiOperation("新增或修改工序表信息")
    @PostMapping("/baseProcess/addOrUpdate")
    ResponseEntity<BaseProcess> addOrUpdate(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseProcess baseProcess);

    @ApiOperation(value = "新增或修改工艺路线表信息",notes = "新增或修改工艺路线表信息")
    @PostMapping("/baseRoute/addOrUpdate")
    ResponseEntity<BaseRoute> addOrUpdate(@ApiParam(value = "必传：routeCode、organizationId",required = true)@RequestBody @Validated BaseRoute baseRoute);

    @ApiOperation(value = "新增或修改产品工艺路线表信息",notes = "新增或修改产品工艺路线表信息")
    @PostMapping("/baseProductProcessRoute/addOrUpdate")
    ResponseEntity<BaseProductProcessRoute> addOrUpdate(@ApiParam(value = "必传：materialId、routeId",required = true)@RequestBody @Validated BaseProductProcessRoute baseProductProcessRoute);

    @ApiOperation(value = "新增或修改工序类别表信息",notes = "新增或修改工序类别表信息")
    @PostMapping("/baseProcessCategory/addOrUpdate")
    ResponseEntity<BaseProcessCategory> addOrUpdate(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseProcessCategory baseProcessCategory);

    @ApiOperation(value = "新增或修改不良代码类别表信息",notes = "新增或修改不良代码类别表信息")
    @PostMapping("/baseBadnessCategory/saveByApi")
    ResponseEntity<BaseBadnessCategory> saveByApi(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseBadnessCategory baseBadnessCategory);

    @ApiOperation(value = "新增或修改不良代码原因表信息",notes = "新增或修改不良代码原因表信息")
    @PostMapping("/baseBadnessCause/saveByApi")
    ResponseEntity<BaseBadnessCause> saveByApi(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseBadnessCause bseBadnessCause);

    @ApiOperation("检验标准列表")
    @PostMapping("/baseInspectionStandard/findList")
    ResponseEntity<List<BaseInspectionStandard>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInspectionStandard searchBaseInspectionStandard);

    @ApiOperation("不良现象列表")
    @PostMapping("/baseBadnessPhenotype/findList")
    ResponseEntity<List<BaseBadnessPhenotypeDto>> findBadnessPhenotypeDtoList(@ApiParam(value = "查询对象")@RequestBody SearchBaseBadnessPhenotype searchBaseBadnessPhenotype);


    @ApiOperation("检验方式列表")
    @PostMapping("/baseInspectionWay/findList")
    ResponseEntity<List<BaseInspectionWay>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInspectionWay searchBaseInspectionWay);
    @ApiOperation("工作人员列表")
    @PostMapping("/baseWorker/findList")
    ResponseEntity<List<BaseWorkerDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseWorker searchBaseWorker);

    @ApiOperation("批量添加部门信息")
    @PostMapping("/baseDept/batchAdd")
    ResponseEntity<List<BaseDept>> batchAddDept(@ApiParam(value = "保存对象")@RequestBody List<BaseDept> baseDepts);

    @ApiOperation("批量添加产线信息")
    @PostMapping("/baseProLine/batchAdd")
    ResponseEntity<List<BaseProLine>> batchAddLine(@ApiParam(value = "保存对象")@RequestBody List<BaseProLine> baseProLines);

    @ApiOperation("批量添加产线信息")
    @PostMapping("/baseWorkShop/batchAdd")
    ResponseEntity<List<BaseWorkShop>>  batchAddWorkshop(@ApiParam(value = "保存对象")@RequestBody List<BaseWorkShop> baseWorkShops);

    @ApiOperation("上架分配规则")
    @PostMapping("/baseStorage/JobRule")
    ResponseEntity<List<StorageRuleDto>> JobRule(@RequestBody JobRuleDto jobRuleDto);

    @ApiOperation("列表")
    @PostMapping("/baseInventoryStatus/findList")
    ResponseEntity<List<BaseInventoryStatus>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInventoryStatus searchBaseInventoryStatus);

    @ApiOperation("治具增加使用次数")
    @PostMapping("/eamJigBarcode/plusCurrentUsageTime")
    ResponseEntity plusCurrentUsageTime(@ApiParam(value = "治具条码id", required = true) @RequestParam @NotNull(message = "治具条码id") Long jigBarcodeId,
                                           @ApiParam(value = "治具使用次数", required = true) @RequestParam @NotNull(message = "治具使用次数不能为空") Integer num);
}
