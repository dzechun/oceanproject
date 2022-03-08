package com.fantechs.provider.base.util;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseStorageRule;
import com.fantechs.common.base.general.dto.basic.StorageRuleInventry;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseStorageCapacity;
import com.fantechs.common.base.general.entity.basic.BaseWarehouseArea;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseMaterialMapper;
import com.fantechs.provider.base.mapper.BaseStorageCapacityMapper;
import com.fantechs.provider.base.mapper.BaseStorageMapper;
import com.fantechs.provider.base.mapper.BaseWarehouseAreaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author mr.lei
 * @Date 2022/3/4
 */
@Component
@Slf4j
public class WanbaoStorageRule {

    @Resource
    private BaseWarehouseAreaMapper baseWarehouseAreaMapper;
    @Resource
    private BaseStorageMapper baseStorageMapper;
    @Resource
    private BaseMaterialMapper baseMaterialMapper;
    @Resource
    private BaseStorageCapacityMapper baseStorageCapacityMapper;

    // 声明对象
    private static WanbaoStorageRule wanbaoStorageRule;

    @PostConstruct // 初始化
    public void init(){
        wanbaoStorageRule = this;
        wanbaoStorageRule.baseWarehouseAreaMapper = this.baseWarehouseAreaMapper;
        wanbaoStorageRule.baseStorageMapper = this.baseStorageMapper;
        wanbaoStorageRule.baseMaterialMapper = this.baseMaterialMapper;
        wanbaoStorageRule.baseStorageCapacityMapper = this.baseStorageCapacityMapper;
    }

    /**
     * 入库规则
     * @param baseStorageRule
     * @return
     */
    public static Long retInStorage(BaseStorageRule baseStorageRule){
        if(StringUtils.isEmpty(baseStorageRule.getLogicId(),baseStorageRule.getMaterialId(),baseStorageRule.getSalesBarcode(),baseStorageRule.getQty(),baseStorageRule.getPoCode())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        //查询erp逻辑仓绑定的库区
        Example example = new Example(BaseWarehouseArea.class);
        example.createCriteria().andNotEqualTo("logicId",baseStorageRule.getLogicId());
        BaseWarehouseArea baseWarehouseArea = wanbaoStorageRule.baseWarehouseAreaMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(baseStorageRule)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未获取到ERP逻辑仓绑定的库区");
        }
        //根据仓库产线查询库位
        example = new Example(BaseStorage.class);
        example.createCriteria().andNotEqualTo("warehouseId",baseWarehouseArea.getWarehouseId()).andEqualTo("proLineId",baseStorageRule.getProLineId());
        List<BaseStorage> baseStorageList = wanbaoStorageRule.baseStorageMapper.selectByExample(example);
        if(StringUtils.isEmpty(baseStorageList) || baseStorageList.size()<1){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"根据仓库产线获取库位信息失败");
        }
        //是否MC产品
        BaseMaterial baseMaterial = wanbaoStorageRule.baseMaterialMapper.selectByPrimaryKey(baseStorageRule.getMaterialId());
        if(StringUtils.isEmpty(baseMaterial)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"获取物料信息失败");
        }
        if(baseMaterial.getMaterialName().contains("/MC/")){
            //包含MC则筛选底垫的库位
            baseStorageList = baseStorageList.stream().filter(x->x.getIsHeelpiece()==2).collect(Collectors.toList());
        }
        //库容最大容量
        BigDecimal capacity = screenStorageCapacity(baseMaterial);

        //计算可上架库位
        Long storageId = onStorage(baseStorageRule,baseStorageList,capacity);
        return storageId;
    }

    /**
     * 出库规则
     * @return
     */
    public static Long retOutStorage(BaseStorageRule baseStorageRule){
        if(StringUtils.isEmpty(baseStorageRule.getLogicId(),baseStorageRule.getMaterialId(),baseStorageRule.getSalesBarcode(),baseStorageRule.getQty(),baseStorageRule.getPoCode())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        List<StorageRuleInventry> storageRuleInventries = wanbaoStorageRule.baseStorageMapper.findOutInv(ControllerUtil.dynamicCondition("materialId",baseStorageRule.getMaterialId(),
                "salesBarcode",baseStorageRule.getSalesBarcode(),"poCode",baseStorageRule.getPoCode()));
        if(StringUtils.isEmpty(storageRuleInventries)||storageRuleInventries.size()<1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"未获取到符合条件的出库库位");
        }
        return  storageRuleInventries.get(0).getStorageId();
    }

    /**
     * 筛选库位类型
     * @param baseMaterial
     * @return
     */
    private static BigDecimal screenStorageCapacity(BaseMaterial baseMaterial){
        //根据物料查询库位类型
        Example example = new Example(BaseStorageCapacity.class);
        example.createCriteria().andEqualTo("materialId",baseMaterial.getMaterialId());
        BaseStorageCapacity baseStorageCapacity = wanbaoStorageRule.baseStorageCapacityMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(baseStorageCapacity)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"获取库位类型失败");
        }
        //物料前缀39110157 推荐C类  39110089、39110090推荐B类
        BigDecimal capacity = BigDecimal.ZERO;
        if(baseMaterial.getMaterialCode().startsWith("39110157")){
            capacity = baseStorageCapacity.getTypeCCapacity();
        }else if(baseMaterial.getMaterialCode().startsWith("39110089") || baseMaterial.getMaterialCode().startsWith("39110090") || baseMaterial.getMaterialName().contains("酒柜") || baseMaterial.getMaterialName().contains("饮料柜")){
            capacity = baseStorageCapacity.getTypeBCapacity();
        }else {
            //根据冰箱升数 大于等于100取B库容 小于等于90取A库容
            String code = baseMaterial.getMaterialCode().substring(6,8);
            if(Integer.parseInt(code)<=99){
                capacity = baseStorageCapacity.getTypeACapacity();
            }else if(Integer.parseInt(code)>=100){
                capacity = baseStorageCapacity.getTypeBCapacity();
            }
        }
        return capacity;
    }

    /**
     * 计算可上架库位
     * @param list
     * @param capacity
     * @return
     */
    private static Long onStorage(BaseStorageRule baseStorageRule,List<BaseStorage> list,BigDecimal capacity){
        Long storageId= null;
        //查询批次相同库位 物料+库位+销售编码+PO+库龄小于30天
        List<StorageRuleInventry> storageRuleInventries = wanbaoStorageRule.baseStorageMapper.findInv(ControllerUtil.dynamicCondition("storageIds",list,"materialId",baseStorageRule.getMaterialId(),
                "salesBarcode",baseStorageRule.getSalesBarcode(),"poCode",baseStorageRule.getPoCode()));
        List<StorageRuleInventry> alikeList = storageRuleInventries.stream().filter(x->(capacity.subtract(x.getMaterialQty()))
                .compareTo(baseStorageRule.getQty())>-1)
                .sorted(Comparator.comparing(StorageRuleInventry::getMaterialQty))
                .collect(Collectors.toList());
        if(alikeList.size()>0){
            storageId = alikeList.get(0).getStorageId();
        }
        //筛选空库位 根据上架动线号升序
        list = list.stream().filter(x-> x.getStorageId().longValue()!=storageRuleInventries.listIterator().next().getStorageId().longValue()).sorted(Comparator.comparing(BaseStorage::getPutawayMoveLineNo)).collect(Collectors.toList());
        if(list.size()>0){
            storageId = list.get(0).getStorageId();
        }else {
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"未匹配到空库位");
        }
        return storageId;
    }

    public static void main(String[] args) {
        StorageRuleInventry storageRuleInventry = new StorageRuleInventry();
        storageRuleInventry.setStorageId(Long.getLong("1"));
        StorageRuleInventry storageRuleInventry2 = new StorageRuleInventry();
        storageRuleInventry2.setStorageId(Long.getLong("2"));
        List<StorageRuleInventry> list = new ArrayList<>();
        list.add(storageRuleInventry);
        list.add(storageRuleInventry2);
        BaseStorage baseStorage = new BaseStorage();
        baseStorage.setStorageId(Long.parseLong("1"));
        List<BaseStorage> list1 = new ArrayList<>();
        list1.add(baseStorage);

        list = list.stream().filter(x-> x.getStorageId().longValue()!=list1.listIterator().next().getStorageId().longValue()).collect(Collectors.toList());
        System.out.println(JSON.toJSONString(list));
    }
}
