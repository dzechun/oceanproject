package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.basic.BaseMaterialPackageDto;
import com.fantechs.common.base.general.dto.basic.BasePackageSpecificationDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderMaterialRePDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderProcessReWoDto;
import com.fantechs.common.base.general.dto.om.OmSalesCodeReSpcDto;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcBarcodeOperationMapper {
    MesPmWorkOrder workOrderDetail(@Param("workOrderId") Long workOrderId);

    MesPmWorkOrder findWorkOrderDetail(@Param("workOrderId") long  workOrderId);

    BaseLabelCategory findLabelCategoryCount(@Param("labelCategoryId")long labelCategoryId);

    List<BaseBarcodeRuleSpec> findSpec(@Param("barcodeRuleId")Long barcodeRuleId);

    List<BasePackageSpecificationDto> findByMaterialProcess(Map<String, Object> map);

    List<BaseMaterialPackageDto> findList(Map<String, Object> map);

    BaseBarcodeRule findBaseBarcodeRule(@Param("barcodeRuleId") Long barcodeRuleId);

    List<BaseRouteProcess> findListRouteProcess(@Param(value="routeId")Long routeId);

    BaseProcess findBaseProcess(@Param("processId")Long processId);

    BaseStation findBaseStation(@Param("stationId")Long stationId);

    BaseWorkshopSection findBaseWorkshopSection(@Param("sectionId")Long sectionId);

    BaseProLine findBaseProLine(@Param("proLineId")Long proLineId);

    List<BaseRouteProcess> findConfigureRout(@Param("routeId")Long routeId);

    MesPmWorkOrder findMesPmWorkOrder(@Param("workOrderId")Long workOrderId);

    List<MesPmWorkOrderProcessReWoDto> findPmWorkOrderProcessReWoList(Map<String, Object> map);

    List<MesPmWorkOrderMaterialRePDto> findWorkOrderMaterialReP(@Param("workOrderProcessReWoId")Long workOrderProcessReWoId);

    List<BaseSignature> findSignatureList(Map<String, Object> map);

    OmSalesCodeReSpcDto findSalesCodeReSpc(Map<String, Object> map);
}