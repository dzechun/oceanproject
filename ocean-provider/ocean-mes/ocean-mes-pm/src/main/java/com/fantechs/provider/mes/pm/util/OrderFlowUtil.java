package com.fantechs.provider.mes.pm.util;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrderFlowDto;
import com.fantechs.common.base.general.entity.basic.BaseOrderFlow;
import com.fantechs.common.base.utils.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 单据流工具类
 *
 * @author lzw
 */
public class OrderFlowUtil {

    public static BaseOrderFlow getOrderFlow(List<BaseOrderFlowDto> baseOrderFlowDtos, Long materialId, Long supplierId){
        HashMap<Byte, List<BaseOrderFlow>> collect = baseOrderFlowDtos.stream().collect(Collectors.groupingBy(BaseOrderFlow::getOrderFlowDimension, HashMap::new, Collectors.toList()));

        List<BaseOrderFlow> materialOrderFlows = collect.get((byte)3);
        if(StringUtils.isNotEmpty(materialId)&&StringUtils.isNotEmpty(materialOrderFlows)) {
            for (BaseOrderFlow materialOrderFlow : materialOrderFlows) {
                if (materialId.equals(materialOrderFlow.getMaterialId())) {
                    return materialOrderFlow;
                }
            }
        }

        List<BaseOrderFlow> supplierOrderFlows = collect.get((byte)2);
        if (StringUtils.isNotEmpty(supplierId)&&StringUtils.isNotEmpty(supplierOrderFlows)) {
            for (BaseOrderFlow supplierOrderFlow : supplierOrderFlows) {
                if (supplierId.equals(supplierOrderFlow.getSupplierId())) {
                    return supplierOrderFlow;
                }
            }
        }

        List<BaseOrderFlow> commonOrderFlows = collect.get((byte)1);
        if(StringUtils.isEmpty(commonOrderFlows)){
            throw new BizErrorException("未找到当前单据配置的下游单据");
        }else {
            return commonOrderFlows.get(0);
        }
    }
}
