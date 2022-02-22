package com.fantechs.common.base.general.dto.basic;

import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseRoute;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * 万宝-同步订单查询基础数据集合DTO
 */
@Data
public class WanbaoBaseBySyncOrderDto implements Serializable {
    List<BaseProLine> proLineList;
    List<BaseMaterialDto> materialDtoList;
    List<BaseRoute> routeList;
    List<BaseBarcodeRuleSetDto> barcodeRuleSetDtoList;
    List<BaseRouteProcess> routeProcessList;
}
