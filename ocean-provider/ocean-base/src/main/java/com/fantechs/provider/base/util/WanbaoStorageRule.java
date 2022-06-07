package com.fantechs.provider.base.util;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseStorageRule;
import com.fantechs.common.base.general.dto.basic.StorageRuleInventry;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseStorageCapacity;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
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
    @Resource
    private BaseProLineMapper baseProLineMapper;

    // 声明对象
    private static WanbaoStorageRule wanbaoStorageRule;

    @PostConstruct // 初始化
    public void init(){
        wanbaoStorageRule = this;
        wanbaoStorageRule.baseWarehouseAreaMapper = this.baseWarehouseAreaMapper;
        wanbaoStorageRule.baseStorageMapper = this.baseStorageMapper;
        wanbaoStorageRule.baseMaterialMapper = this.baseMaterialMapper;
        wanbaoStorageRule.baseStorageCapacityMapper = this.baseStorageCapacityMapper;
        wanbaoStorageRule.baseProLineMapper = this.baseProLineMapper;
    }

    /**
     * 入库规则
     * @param baseStorageRule
     * @return
     */
    public static Long retInStorage(BaseStorageRule baseStorageRule){
        if(StringUtils.isEmpty(baseStorageRule.getLogicId(),baseStorageRule.getMaterialId(),baseStorageRule.getQty(),baseStorageRule.getInventoryStatusId())){
            log.error("参数为空");
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        //根据仓库产线查询库位
        Example example = new Example(BaseStorage.class);
        example.createCriteria().andEqualTo("logicId",baseStorageRule.getLogicId()).andEqualTo("proLineId",baseStorageRule.getProLineId());
        List<BaseStorage> baseStorageList = wanbaoStorageRule.baseStorageMapper.selectByExample(example);
        if(StringUtils.isEmpty(baseStorageList) || baseStorageList.size()<1){
            log.error("未查询到逻辑仓："+baseStorageRule.getLogicId()+"产线："+baseStorageRule.getProLineId());
            return publicStorage(baseStorageRule);
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

        //查询产线是否为A产线
        BaseProLine baseProLine = wanbaoStorageRule.baseProLineMapper.selectByPrimaryKey(baseStorageRule.getProLineId());
        if(StringUtils.isEmpty(baseProLine)){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"获取产线信息失败");
        }
        Map<String ,Object> map = null;
        if(baseProLine.getProCode().equals("A")){
            map = ALineScreenStorageCapacity(baseMaterial,baseStorageRule.getWorkOrderQty(),baseStorageList);
        }else {
            //库容最大容量
            map = screenStorageCapacity(baseMaterial,baseStorageList);
        }
        if(StringUtils.isEmpty(map)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012002.getCode(),"获取最大库容失败");
        }
        BigDecimal capacity = new BigDecimal(map.get("capacity").toString());
        log.error("====================================库容："+capacity+"=============================");
        log.error(JsonUtils.objectToJson(map.get("list")));
        baseStorageList = (List<BaseStorage>) map.get("list");
        if(StringUtils.isEmpty(baseStorageList) || baseStorageList.size()<1){
            log.error("=======================进入匹配空库位2=====================");
            return publicStorage(baseStorageRule);
        }
        //计算可上架库位
        Long storageId = onStorage(baseStorageRule,baseStorageList,capacity);
        if(StringUtils.isEmpty(storageId)){
            log.error("=======================没有可上架库位进入匹配空库位3=====================");
            storageId = publicStorage(baseStorageRule);
        }

        return storageId;
    }

    private static Long publicStorage(BaseStorageRule baseStorageRule){
        //库位爆满 执行获取公共库位
            //根据仓库产线查询库位
            Example example = new Example(BaseStorage.class);
            example.createCriteria().andEqualTo("logicId",baseStorageRule.getLogicId()).andLike("storageCode","C4-%").andIsNull("proLineId");
            List<BaseStorage> baseStorageList = wanbaoStorageRule.baseStorageMapper.selectByExample(example);
            if(StringUtils.isEmpty(baseStorageList) || baseStorageList.size()<1){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"获取C4公共库位失败");
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
//        if(StringUtils.isEmpty(baseStorageRule.getLogicId(),baseStorageRule.getMaterialId(),baseStorageRule.getQty())){
//            throw new BizErrorException(ErrorCodeEnum.GL99990100);
//        }
        if(StringUtils.isEmpty(baseStorageRule.getMaterialId(),baseStorageRule.getQty())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        List<StorageRuleInventry> storageRuleInventries = wanbaoStorageRule.baseStorageMapper.findOutInv(ControllerUtil.dynamicCondition("materialId",baseStorageRule.getMaterialId(),
                "salesBarcode",baseStorageRule.getSalesBarcode(),"poCode",baseStorageRule.getPoCode(),"materialQty",baseStorageRule.getQty()));
        if(StringUtils.isEmpty(storageRuleInventries)||storageRuleInventries.size()<1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"未获取到符合条件的出库库位");
        }
        return  storageRuleInventries.get(0).getStorageId();
    }

    /**
     * 出库规则
     * @return
     */
    public static List<StorageRuleInventry> returnOutStorage(BaseStorageRule baseStorageRule){
        if(StringUtils.isEmpty(baseStorageRule.getMaterialId(),baseStorageRule.getQty())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        List<StorageRuleInventry> storageRuleInventries = wanbaoStorageRule.baseStorageMapper.findOutStorage(ControllerUtil.dynamicCondition("materialId",baseStorageRule.getMaterialId(),
                "salesBarcode",baseStorageRule.getSalesBarcode(),"poCode",baseStorageRule.getPoCode(),"materialQty",baseStorageRule.getQty()));
        if(StringUtils.isEmpty(storageRuleInventries)||storageRuleInventries.size()<1){
            throw new BizErrorException(ErrorCodeEnum.GL9999404.getCode(),"未获取到符合条件的出库库位");
        }

        return  storageRuleInventries;
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
            String code = baseMaterial.getMaterialCode().substring(5,8);
            if(Integer.parseInt(code)<=99){
                capacity = baseStorageCapacity.getTypeACapacity();
                log.error("A库容");
                log.error(JsonUtils.objectToJson(list));
                list = list.stream().filter(x->x.getMaterialStoreType()==1).collect(Collectors.toList());
                log.error(JsonUtils.objectToJson(list));
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
     * A产线筛选库位类型
     * @return
     */
    private static Map<String,Object> ALineScreenStorageCapacity(BaseMaterial baseMaterial,BigDecimal workOrderQty, List<BaseStorage> list){
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
        int[] src = new int[]{baseStorageCapacity.getTypeACapacity().intValue(), baseStorageCapacity.getTypeBCapacity().intValue(), baseStorageCapacity.getTypeCCapacity().intValue(), baseStorageCapacity.getTypeDCapacity().intValue()};
        int x = getApproximate(workOrderQty.intValue(), src);
        if ((workOrderQty.compareTo(baseStorageCapacity.getTypeACapacity())==1 &&
                workOrderQty.compareTo(baseStorageCapacity.getTypeBCapacity())==1 &&
                workOrderQty.compareTo(baseStorageCapacity.getTypeCCapacity())==1 &&
                workOrderQty.compareTo(baseStorageCapacity.getTypeDCapacity())==1) ||
                (BigDecimal.valueOf(x).compareTo(baseStorageCapacity.getTypeACapacity()) == 0)) {
            capacity = baseStorageCapacity.getTypeACapacity();
            list = list.stream().filter(y -> y.getMaterialStoreType() == 1).collect(Collectors.toList());
        } else if (BigDecimal.valueOf(x).compareTo(baseStorageCapacity.getTypeBCapacity()) == 0) {
            capacity = baseStorageCapacity.getTypeBCapacity();
            list = list.stream().filter(y -> y.getMaterialStoreType() == 2).collect(Collectors.toList());
        } else if (BigDecimal.valueOf(x).compareTo(baseStorageCapacity.getTypeCCapacity()) == 0) {
            capacity = baseStorageCapacity.getTypeCCapacity();
            list = list.stream().filter(y -> y.getMaterialStoreType() == 3).collect(Collectors.toList());
        } else if (BigDecimal.valueOf(x).compareTo(baseStorageCapacity.getTypeDCapacity()) == 0) {
            capacity = baseStorageCapacity.getTypeDCapacity();
            list = list.stream().filter(y -> y.getMaterialStoreType() == 4).collect(Collectors.toList());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("capacity",capacity);
        map.put("list",list);
        return map;
    }

    /**
     * 获取最接近的数
     * @param x
     * @param src
     * @return
     */
    private static int getApproximate (int x, int[] src) {
        if (src == null) {
            return -1;
        }
        if (src.length == 1) {
            return src[0];
        }
        int minDifference = Math.abs(src[0] - x);
        int minIndex = 0;
        for (int i = 1; i < src.length; i++) {
            int temp = Math.abs(src[i] - x);
            if (temp < minDifference) {
                minIndex = i;
                minDifference = temp;
            }
        }
        return src[minIndex];
    }

    /**
     * 计算可上架库位
     * @param list
     * @param capacity
     * @return
     */
    private static Long onStorage(BaseStorageRule baseStorageRule,List<BaseStorage> list,BigDecimal capacity){
        Long storageId= null;
        //用库位查询在库货品库存
        List<Long> scLongs = wanbaoStorageRule.baseStorageMapper.screen(list,capacity,baseStorageRule.getQty());
        //查询批次相同库位 物料+库位+销售编码+PO+库龄小于30天
        List<StorageRuleInventry> storageRuleInventries = wanbaoStorageRule.baseStorageMapper.findInv(scLongs,baseStorageRule.getMaterialId(),
                baseStorageRule.getSalesBarcode(),baseStorageRule.getPoCode(),baseStorageRule.getInventoryStatusId());
        log.error("====================================库容："+capacity+"=============================");
        log.error("最近上架库位"+JsonUtils.objectToJson(storageRuleInventries));
        List<StorageRuleInventry> alikeList = storageRuleInventries.stream().filter(x->(capacity.subtract(x.getMaterialQty()))
                .compareTo(baseStorageRule.getQty())>-1)
                .sorted(Comparator.comparing(StorageRuleInventry::getMaterialQty))
                .collect(Collectors.toList());
        log.error("未满仓库位："+JsonUtils.objectToJson(alikeList));
        if(alikeList.size()>0){
            storageId = alikeList.get(0).getStorageId();
            return storageId;
        }
        //筛选空库位 根据上架动线号升序
        if(StringUtils.isNotEmpty(storageRuleInventries) || storageRuleInventries.size()>0){
            for (StorageRuleInventry storageRuleInventry : storageRuleInventries) {
                list = list.stream().filter(y->y.getStorageId().longValue()!=storageRuleInventry.getStorageId().longValue()).collect(Collectors.toList());
            }
            list = list.stream().sorted(Comparator.comparing(BaseStorage::getPutawayMoveLineNo)).collect(Collectors.toList());
        }
        log.error("筛选空库位：======="+JsonUtils.objectToJson(list));
        if(StringUtils.isEmpty(storageId) && list.size()>0){
            //筛选没有库存的库位
            List<Long> longs= wanbaoStorageRule.baseStorageMapper.findEmptyStorage(list);
            if(StringUtils.isNotEmpty(longs) || longs.size()>0){
                //通过物料及库位查询是否拥有已分配未上架的库位占用 相同货品占用库位放入一起
                //先通过货品及可分配库位查询是否存在未上架的库位
                log.error("查询条件============="+JsonUtils.objectToJson(longs));
                List<Long> strLongs = wanbaoStorageRule.baseStorageMapper.findJobOrderStorageInMaterial(baseStorageRule.getMaterialId(),baseStorageRule.getPoCode(),baseStorageRule.getSalesBarcode(),longs);
                log.error("查询上架占用库位=============="+JsonUtils.objectToJson(strLongs));
                if(strLongs.size()<1){
                    longs = wanbaoStorageRule.baseStorageMapper.findJobOrderStorage(longs);
                }else {
                    longs = strLongs;
                }
                if(longs.size()>0){
                    storageId = longs.get(0);
                }
            }
        }
        return storageId;
    }

    public static void main(String[] args) {
//        String ss = "396101060025";
//        System.out.println(Integer.parseInt(ss.substring(5,8)));
        //int[] src = new int[]{25,100,30,20,49,51};
        String code = "391100470238".substring(5,8);
        System.out.println(Integer.parseInt(code)<=99);
        System.out.println(code);
    }
}
