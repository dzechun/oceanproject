package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.basic.BaseMaterialPackageDto;
import com.fantechs.common.base.general.dto.basic.BasePackageSpecificationDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.BaseLabelCategory;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
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

}