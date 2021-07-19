package com.fantechs.provider.wms.inner.util;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @Author mr.lei
 * @Date 2021/7/15
 * 出库规则
 */
@Component
public class OutInventoryRule {

    //数据源
    @Resource
    private WmsInnerInventoryMapper wmsInnerInventoryMapper;

    //声明对象
    private static OutInventoryRule outInventoryRule;

    @PostConstruct
    private void init(){
        outInventoryRule = this;
        outInventoryRule.wmsInnerInventoryMapper = this.wmsInnerInventoryMapper;
    }

    /**
     * 出库规则 （先进先出）
     * @param warehouseId 仓库id
     * @param materialId 产品id
     * @param batchCode 批次号
     * @param productionDate 生成日期
     * @return 可出库库存
     */
    public static List<WmsInnerInventory> jobMainRule(Long warehouseId,Long materialId,String batchCode,String productionDate){
        Example example = new Example(WmsInnerInventory.class);
        example.createCriteria().andEqualTo("warehouseId",warehouseId)
                .andEqualTo("materialId",materialId)
                .andEqualTo("batchCode",batchCode)
                .andEqualTo("productionDate",productionDate);
        example.orderBy("production_date,packing_qty desc");
        List<WmsInnerInventory> list  = outInventoryRule.wmsInnerInventoryMapper.selectByExample(example);
        if(list.size()<1){
            throw new BizErrorException("未查询到匹配的库存");
        }
        return list;
    }
}
