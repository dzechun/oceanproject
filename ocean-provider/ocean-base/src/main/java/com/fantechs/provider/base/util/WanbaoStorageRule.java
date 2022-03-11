package com.fantechs.provider.base.util;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseStorageRule;
import com.fantechs.common.base.general.dto.basic.StorageRuleInventry;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseStorageCapacity;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        if(StringUtils.isEmpty(baseStorageRule.getLogicId(),baseStorageRule.getMaterialId(),baseStorageRule.getQty())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        //查询erp逻辑仓绑定的库区
//        Example example = new Example(BaseWarehouseArea.class);
//        example.createCriteria().andNotEqualTo("logicId",baseStorageRule.getLogicId());
//        BaseWarehouseArea baseWarehouseArea = wanbaoStorageRule.baseWarehouseAreaMapper.selectOneByExample(example);
//        if(StringUtils.isEmpty(baseStorageRule)){
//            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未获取到ERP逻辑仓绑定的库区");
//        }
        //根据仓库产线查询库位
        Example example = new Example(BaseStorage.class);
        example.createCriteria().andEqualTo("logicId",baseStorageRule.getLogicId()).andEqualTo("proLineId",baseStorageRule.getProLineId());
        List<BaseStorage> baseStorageList = wanbaoStorageRule.baseStorageMapper.selectByExample(example);
        if(StringUtils.isEmpty(baseStorageList) || baseStorageList.size()<1){
            //throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"根据仓库产线获取库位信息失败");
            return ss(baseStorageRule);
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
        Map<String ,Object> map = screenStorageCapacity(baseMaterial,baseStorageList);

        BigDecimal capacity = new BigDecimal(map.get("capacity").toString());
        baseStorageList = (List<BaseStorage>) map.get("list");
        if(StringUtils.isEmpty(baseStorageList) || baseStorageList.size()<1){
            return ss(baseStorageRule);
        }
        //计算可上架库位
        Long storageId = onStorage(baseStorageRule,baseStorageList,capacity);
        if(StringUtils.isEmpty(storageId)){
            storageId = ss(baseStorageRule);
        }

        return storageId;
    }

    private static Long ss(BaseStorageRule baseStorageRule){
        //库位爆满 执行获取公共库位
            //根据仓库产线查询库位
            Example example = new Example(BaseStorage.class);
            example.createCriteria().andLike("storageCode","C4-%").andIsNull("proLineId");
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
            Map<String ,Object> map = screenStorageCapacity(baseMaterial,baseStorageList);
            BigDecimal capacity = new BigDecimal(map.get("capacity").toString());
            baseStorageList = (List<BaseStorage>) map.get("list");
            if(StringUtils.isEmpty(baseStorageList) || baseStorageList.size()<1){
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取公共库位失败");
            }
            //计算可上架库位
            Long storageId = onStorage(baseStorageRule,baseStorageList,capacity);
            if(StringUtils.isEmpty(storageId)) {
                throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"未匹配到可上架库位");
            }
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
    private static Map<String, Object> screenStorageCapacity(BaseMaterial baseMaterial, List<BaseStorage> list){
        //根据物料查询库位类型
        String str = baseMaterial.getMaterialCode().substring(0,8);
        Example example = new Example(BaseStorageCapacity.class);
        example.createCriteria().andEqualTo("materialCodePrefix",str);
        BaseStorageCapacity baseStorageCapacity = wanbaoStorageRule.baseStorageCapacityMapper.selectOneByExample(example);
        if(StringUtils.isEmpty(baseStorageCapacity)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"获取库位类型失败");
        }
        //物料前缀39110157 推荐C类  39110089、39110090推荐B类
        BigDecimal capacity = BigDecimal.ZERO;
        if(str.startsWith("39110157")){
            capacity = baseStorageCapacity.getTypeCCapacity();
            list = list.stream().filter(x->x.getMaterialStoreType()==3).collect(Collectors.toList());
        }else if(str.startsWith("39110089") || str.startsWith("39110090") || baseMaterial.getMaterialName().contains("酒柜") || baseMaterial.getMaterialName().contains("饮料柜")){
            capacity = baseStorageCapacity.getTypeBCapacity();
            list = list.stream().filter(x->x.getMaterialStoreType()==2).collect(Collectors.toList());
        }else {
            //根据冰箱升数 大于等于100取B库容 小于等于90取A库容
            String code = baseMaterial.getMaterialCode().substring(6,8);
            if(Integer.parseInt(code)<=99){
                capacity = baseStorageCapacity.getTypeACapacity();
                list = list.stream().filter(x->x.getMaterialStoreType()==1).collect(Collectors.toList());
            }else if(Integer.parseInt(code)>=100){
                capacity = baseStorageCapacity.getTypeBCapacity();
                list = list.stream().filter(x->x.getMaterialStoreType()==2).collect(Collectors.toList());
            }
        }
        Map<String,Object> map = new HashMap<>();
        map.put("capacity",capacity);
        map.put("list",list);
        return map;
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
        List<StorageRuleInventry> storageRuleInventries = wanbaoStorageRule.baseStorageMapper.findInv(list,baseStorageRule.getMaterialId(),
                baseStorageRule.getSalesBarcode(),baseStorageRule.getPoCode());
        List<StorageRuleInventry> alikeList = storageRuleInventries.stream().filter(x->(capacity.subtract(x.getMaterialQty()))
                .compareTo(baseStorageRule.getQty())>-1)
                .sorted(Comparator.comparing(StorageRuleInventry::getMaterialQty))
                .collect(Collectors.toList());
        if(alikeList.size()>0){
            storageId = alikeList.get(0).getStorageId();
        }
        //筛选空库位 根据上架动线号升序
        if(StringUtils.isNotEmpty(storageRuleInventries) || storageRuleInventries.size()>0){
            list = list.stream().filter(x-> x.getStorageId().longValue()!=storageRuleInventries.listIterator().next().getStorageId().longValue()).sorted(Comparator.comparing(BaseStorage::getPutawayMoveLineNo)).collect(Collectors.toList());
        }
        if(StringUtils.isEmpty(storageId) && list.size()>0){
            storageId = list.get(0).getStorageId();
        }
        return storageId;
    }

    public static void main(String[] args) {
        String ss = "391100470226";
        System.out.println(Integer.parseInt(ss.substring(6,8)));
    }
}
