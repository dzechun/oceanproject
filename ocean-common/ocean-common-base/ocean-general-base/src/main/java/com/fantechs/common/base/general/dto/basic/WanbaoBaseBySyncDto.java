package com.fantechs.common.base.general.dto.basic;

import com.fantechs.common.base.general.entity.basic.*;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 万宝-同步订单查询基础数据集合DTO
 */
@Data
public class WanbaoBaseBySyncDto implements Serializable {
    List<BaseProLine> proLineList;
    List<BaseMaterialDto> materialDtoList;
    List<BaseRoute> routeList;
    List<BaseBarcodeRuleSetDto> barcodeRuleSetDtoList;
    List<BaseRouteProcess> routeProcessList;
    List<BaseProductModel> productModelList;
    List<BaseTabDto> baseTabDtoList;
    List<BaseSupplier> baseSupplierList;
    List<BaseMaterialOwnerDto> materialOwnerDtoList;
    List<BaseProductProcessRoute> processRouteList;
    List<BaseLabelCategoryDto> labelCategoryDtoList;
}
