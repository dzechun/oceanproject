package com.fantechs.provider.restapi.imes.service.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.entity.basic.U9.CustGetItemInfo;
import com.fantechs.common.base.entity.basic.U9.CustGetWhInfo;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.restapi.imes.config.ConstantBase;
import com.fantechs.provider.restapi.imes.config.DynamicDataSourceHolder;
import com.fantechs.provider.restapi.imes.mapper.CustGetItemInfoFromU9Mapper;
import com.fantechs.provider.restapi.imes.mapper.CustGetWharehouseInfoFromU9Mapper;
import com.fantechs.provider.restapi.imes.service.GetDataFromU9Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GetDataFromU9ServiceImpl implements GetDataFromU9Service {

    private Logger logger = LoggerFactory.getLogger(GetDataFromU9Service.class);

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ConstantBase constantBase;
    @Resource
    private CustGetItemInfoFromU9Mapper custGetItemInfoFromU9Mapper;
    @Resource
    private BasicFeignApi basicFeignApi;
    @Resource
    private CustGetWharehouseInfoFromU9Mapper custGetWharehouseInfoFromU9Mapper;

    @Override
    public int updateMaterial() throws Exception {
        // Redis锁，预防短时间内连续操作请求
        String updateMaterial = (String) redisUtil.get("updateMaterial");
        if (StringUtils.isNotEmpty(updateMaterial)) {
            throw new BizErrorException("正在同步数据，请稍后再试！");
        }
        redisUtil.set("updateMaterial", "updateMaterial", 60);
        //获取U9物料信息
        List<CustGetItemInfo> listItemInfos = getMaterialByU9();
        Date date = new Date();
        List<BaseMaterial> baseMaterialAddList = new ArrayList<>();//物料新增集合
        List<BaseMaterial> baseMaterialUpdateList = new ArrayList<>();//物料更新集合

        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();//物料信息查询对象
        searchBaseMaterial.setCodeQueryMark(1);
        for (CustGetItemInfo info : listItemInfos) {
            BaseMaterial baseMaterial = new BaseMaterial();
            baseMaterial.setMaterialCode(info.get料品编码());
            baseMaterial.setMaterialName(info.get品名());
            baseMaterial.setMaterialDesc(info.get描述());
            //smtMaterial.setMaterialType(StringUtils.isEmpty(info.get物料分类()) ? null : info.get物料分类());

            //保存物料信息
            searchBaseMaterial.setMaterialCode(info.get料品编码());
            ResponseEntity<List<BaseMaterial>> responseEntity = basicFeignApi.findList(searchBaseMaterial);
            if (StringUtils.isNotEmpty(responseEntity.getData())) {
                baseMaterialUpdateList.add(baseMaterial);
            } else {
                baseMaterialAddList.add(baseMaterial);
            }
            //集合满1000条数据时执行一次新增和更新操作
            if (listItemInfos.size() > 1000 && baseMaterialAddList.size() > 0 && baseMaterialAddList.size() % 1000 == 0) {
                //批量更新物料
                if (StringUtils.isNotEmpty(baseMaterialAddList)) {
                    basicFeignApi.batchUpdateByCode(baseMaterialUpdateList);
                    baseMaterialUpdateList.clear();
                }
                //批量新增物料
                if (StringUtils.isNotEmpty(baseMaterialAddList)) {
                    basicFeignApi.addList(baseMaterialAddList);
                    baseMaterialAddList.clear();
                }
            }
        }
        //批量更新物料
        if (StringUtils.isNotEmpty(baseMaterialAddList)) {
            basicFeignApi.batchUpdateByCode(baseMaterialUpdateList);
        }
        //批量新增物料
        if (StringUtils.isNotEmpty(baseMaterialAddList)) {
            basicFeignApi.addList(baseMaterialAddList);
        }
        // 释放Redis锁
        if (StringUtils.isNotEmpty(redisUtil.get("updateMaterial"))) {
            redisUtil.del("updateMaterial");
        }
        redisUtil.set(ConstantBase.API_LASTUPDATE_TIME_MATERIAL, DateUtils.getDateString(date, "yyyy-MM-dd HH:mm:ss"));
        return 1;
    }

    //从U9获取物料信息
    public List<CustGetItemInfo> getMaterialByU9() {
        //切换到从数据源
        DynamicDataSourceHolder.putDataSouce("secondary");
        String lastUpdateDate = (String) redisUtil.get(ConstantBase.API_LASTUPDATE_TIME_MATERIAL);
        Example example = new Example(CustGetItemInfo.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("组织id", constantBase.getDefaultOrgId())
                .orEqualTo("组织id",constantBase.getNewOrgId());
        if (!StringUtils.isEmpty(lastUpdateDate)) {
            criteria.andGreaterThanOrEqualTo("最后更新时间", lastUpdateDate);
        }
        List<CustGetItemInfo> listItemInfos = custGetItemInfoFromU9Mapper.selectByExample(example);
        if (StringUtils.isNotEmpty(listItemInfos)) {
            //恢复主数据源
            DynamicDataSourceHolder.removeDataSource();
            return listItemInfos;
        } else {
            //恢复主数据源
            DynamicDataSourceHolder.removeDataSource();
            return null;
        }
    }

    @Override
    public int updateWarehouse() throws Exception {

        // Redis锁，预防短时间内连续操作请求
        String updateWarehouse = (String) redisUtil.get("updateWarehouse");
        if (StringUtils.isNotEmpty(updateWarehouse)) {
            throw new BizErrorException("正在同步数据，请稍后再试！");
        }
        redisUtil.set("updateWarehouse", "updateWarehouse", 3);

        List<CustGetWhInfo> listWhInfos = getWarehouseInfoFromU9();

        Date date = new Date();
        List<BaseWarehouse> baseWarehousesAddList = new ArrayList<>();//批量新增集合
        List<BaseWarehouse> baseWarehousesUpdateList = new ArrayList<>();//批量更新集合

        SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();//仓库查询实体
        searchBaseWarehouse.setCodeQueryMark(1);
        if (StringUtils.isNotEmpty(listWhInfos)) {
            for (CustGetWhInfo info : listWhInfos) {
                BaseWarehouse baseWarehouse = new BaseWarehouse();
                baseWarehouse.setWarehouseCode(info.getCode());
                baseWarehouse.setWarehouseName(info.getName());
                searchBaseWarehouse.setWarehouseCode(baseWarehouse.getWarehouseCode());
                ResponseEntity<List<BaseWarehouse>> responseEntity = basicFeignApi.findList(searchBaseWarehouse);
                if (StringUtils.isNotEmpty(responseEntity.getData())) {
                    baseWarehousesUpdateList.add(baseWarehouse);
                } else {
                    baseWarehousesAddList.add(baseWarehouse);
                }
            }
        }

        logger.info("/material/updateMaterialByU9  同步更新仓库信息接口 " + " smtWarehousesAddList:" + JSON.toJSONString(baseWarehousesUpdateList));
        if (StringUtils.isNotEmpty(baseWarehousesUpdateList)) {
            basicFeignApi.batchUpdateWarehouseByCode(baseWarehousesUpdateList);
        }
        logger.info("/material/updateMaterialByU9  同步新增仓库信息接口 " + " smtWarehousesUpdateList:" + JSON.toJSONString(baseWarehousesAddList));
        if (StringUtils.isNotEmpty(baseWarehousesAddList)) {
            basicFeignApi.batchSave(baseWarehousesAddList);
        }

        // 释放Redis锁
        if (StringUtils.isNotEmpty(redisUtil.get("updateWarehouse"))) {
            redisUtil.del("updateWarehouse");
        }
        redisUtil.set(ConstantBase.API_LASTUPDATE_TIME_WAREHOUSE, DateUtils.getDateString(date, "yyyy-MM-dd HH:mm:ss"));
        return 1;
    }

    //从U9获取仓库数据
    public List<CustGetWhInfo> getWarehouseInfoFromU9() {
        String lastUpdateDate = (String) redisUtil.get(ConstantBase.API_LASTUPDATE_TIME_WAREHOUSE);
        Example example = new Example(CustGetWhInfo.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("orgid", constantBase.getDefaultOrgId());
        if (!StringUtils.isEmpty(lastUpdateDate)) {
//            criteria.andGreaterThanOrEqualTo("udpatetime", lastUpdateDate);
        }

        List<CustGetWhInfo> listWhInfos = custGetWharehouseInfoFromU9Mapper.selectByExample(example);
        return listWhInfos;
    }
}
