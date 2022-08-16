package com.fantechs.provider.wms.inner.util;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerInventoryMapper;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Resource
    private BaseFeignApi baseFeignApi;

    //声明对象
    private static OutInventoryRule outInventoryRule;

    @PostConstruct
    private void init(){
        outInventoryRule = this;
        outInventoryRule.baseFeignApi = this.baseFeignApi;
        outInventoryRule.wmsInnerInventoryMapper = this.wmsInnerInventoryMapper;
    }

    /**
     * 出库规则 （先进先出）
     * @param warehouseId 仓库id
     * @param materialId 产品id
     * @param batchCode 批次号
     * @param productionDate 生成日期
     * @param inventoryStatusId 库存状态
     * @return 可出库库存
     */
    public static List<WmsInnerInventory> jobMainRule(Long warehouseId,Long materialId,String batchCode,String productionDate,Long inventoryStatusId){
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
        if(StringUtils.isNotEmpty(inventoryStatusId)){
            criteria.andEqualTo("inventoryStatusId",inventoryStatusId);
        }
        criteria.andEqualTo("jobStatus",1);
        criteria.andEqualTo("orgId",sysUser.getOrganizationId());
        example.setOrderByClause("production_date,packing_qty desc");
        List<WmsInnerInventory> list  = outInventoryRule.wmsInnerInventoryMapper.selectByExample(example);
        //过滤发货库位及收货库位库存
        list = outInventoryRule.filtration(list);
        if(list.size()<1){
            throw new BizErrorException("未查询到匹配的库存");
        }
        return list;
    }

    private List<WmsInnerInventory> filtration(List<WmsInnerInventory> wmsInnerInventories){
        List<WmsInnerInventory> list = new ArrayList<>();
        Map<Long,Byte> map = new HashMap<>();
        for (WmsInnerInventory wmsInnerInventory : wmsInnerInventories) {
            if(map.containsKey(wmsInnerInventory.getStorageId())){
                if(map.get(wmsInnerInventory.getStorageId())==1){
                    list.add(wmsInnerInventory);
                }
            }else{
                //查询库位类型
                SearchBaseStorage searchBaseStorage = new SearchBaseStorage();
                searchBaseStorage.setStorageId(wmsInnerInventory.getStorageId());
                List<BaseStorage> storages = outInventoryRule.baseFeignApi.findList(searchBaseStorage).getData();
                if(StringUtils.isEmpty(storages)){
                    throw new BizErrorException("获取库位信息失败");
                }
                if(storages.get(0).getStorageType()==1){
                    list.add(wmsInnerInventory);
                    map.put(wmsInnerInventory.getStorageId(),(byte)1);
                }
            }
        }
        return list;
    }
}
