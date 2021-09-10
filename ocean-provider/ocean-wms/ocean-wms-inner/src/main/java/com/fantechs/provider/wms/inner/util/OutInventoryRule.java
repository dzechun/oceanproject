package com.fantechs.provider.wms.inner.util;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
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
        SysUser sysUser = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(WmsInnerInventory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("warehouseId",warehouseId)
                .andEqualTo("materialId",materialId)
                .andGreaterThan("packingQty", BigDecimal.ZERO);
        if(StringUtils.isNotEmpty(batchCode)){
            criteria.andEqualTo("batchCode",batchCode);

        }
        if(StringUtils.isNotEmpty(productionDate)){
            criteria.andEqualTo("productionDate",productionDate);
        }
        criteria.andEqualTo("jobStatus",1);
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        example.setOrderByClause("production_date,packing_qty desc");
        List<WmsInnerInventory> list  = outInventoryRule.wmsInnerInventoryMapper.selectByExample(example);
        if(list.size()<1){
            throw new BizErrorException("未查询到匹配的库存");
        }
        return list;
    }
}
