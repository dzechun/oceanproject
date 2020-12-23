package com.fantechs.provider.restapi.imes.service.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.SmtWarehouse;
import com.fantechs.common.base.entity.basic.U9.CustGetItemInfo;
import com.fantechs.common.base.entity.basic.U9.CustGetWhInfo;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtWarehouse;
import com.fantechs.common.base.response.ControllerUtil;
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
import org.springframework.util.CollectionUtils;
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
        if (StringUtils.isEmpty(updateMaterial)) {
            redisUtil.set("updateMaterial", "updateMaterial", 60);
        } else {
            return 500;
        }

        //获取U9物料信息
        List<CustGetItemInfo> listItemInfos = getMaterialByU9();

        Date date = new Date();
        List<SmtMaterial> smtMaterialAddList = new ArrayList<>();//物料新增集合
        List<SmtMaterial> smtMaterialUpdateList = new ArrayList<>();//物料更新集合

        SearchSmtMaterial searchSmtMaterial = new SearchSmtMaterial();//物料信息查询对象
        searchSmtMaterial.setCodeQueryMark(1);
        for (CustGetItemInfo info : listItemInfos) {
            SmtMaterial smtMaterial = new SmtMaterial();
            smtMaterial.setMaterialCode(info.get料品编码());
            smtMaterial.setMaterialName(info.get品名());
            smtMaterial.setMaterialDesc(info.get描述());
            smtMaterial.setMaterialType(StringUtils.isEmpty(info.get物料分类()) ? null : info.get物料分类());

            //保存物料信息
            searchSmtMaterial.setMaterialCode(info.get料品编码());
            ResponseEntity<List<SmtMaterial>> responseEntity = basicFeignApi.findList(searchSmtMaterial);
            if (StringUtils.isNotEmpty(responseEntity.getData())) {
                smtMaterialUpdateList.add(smtMaterial);
            } else {
                smtMaterialAddList.add(smtMaterial);
            }

            //集合满1000条数据时执行一次新增和更新操作
            if (listItemInfos.size() > 1000 && smtMaterialAddList.size() > 0 && smtMaterialAddList.size() % 1000 == 0) {

                //批量更新物料
                if (StringUtils.isNotEmpty(smtMaterialAddList)) {
                    basicFeignApi.batchUpdateByCode(smtMaterialUpdateList);
                    smtMaterialUpdateList.clear();
                }

                //批量新增物料
                if (StringUtils.isNotEmpty(smtMaterialAddList)) {
                    basicFeignApi.addList(smtMaterialAddList);
                    smtMaterialAddList.clear();
                }
            }
        }

        //批量更新物料
        if (StringUtils.isNotEmpty(smtMaterialAddList)) {
            basicFeignApi.batchUpdateByCode(smtMaterialUpdateList);
        }


        //批量新增物料
        if (StringUtils.isNotEmpty(smtMaterialAddList)) {
            basicFeignApi.addList(smtMaterialAddList);
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
        Example.Criteria criteria = example.createCriteria().andEqualTo("组织id", constantBase.getDefaultOrgId());
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
        if (StringUtils.isEmpty(updateWarehouse)) {
            redisUtil.set("updateWarehouse", "updateWarehouse", 60);
        } else {
            return 500;
        }

        List<CustGetWhInfo> listWhInfos = getWarehouseInfoFromU9();

        Date date = new Date();
        List<SmtWarehouse> smtWarehousesAddList = new ArrayList<>();//批量新增集合
        List<SmtWarehouse> smtWarehousesUpdateList = new ArrayList<>();//批量更新集合

        SearchSmtWarehouse searchSmtWarehouse = new SearchSmtWarehouse();//仓库查询实体
        searchSmtWarehouse.setCodeQueryMark(1);
        for (CustGetWhInfo info : listWhInfos) {
            SmtWarehouse smtWarehouse = new SmtWarehouse();
            smtWarehouse.setWarehouseCode(info.getCode());
            smtWarehouse.setWarehouseName(info.getName());
            searchSmtWarehouse.setWarehouseCode(smtWarehouse.getWarehouseCode());
            ResponseEntity<List<SmtWarehouse>> responseEntity = basicFeignApi.findList(searchSmtWarehouse);
            if (StringUtils.isNotEmpty(responseEntity.getData())){
                smtWarehousesUpdateList.add(smtWarehouse);
            }else {
                smtWarehousesAddList.add(smtWarehouse);
            }
        }


        logger.info("/material/updateMaterialByU9  同步更新仓库信息接口 " + " smtWarehousesAddList:" + JSON.toJSONString(smtWarehousesUpdateList));
        basicFeignApi.batchUpdateWarehouseByCode(smtWarehousesUpdateList);
        logger.info("/material/updateMaterialByU9  同步新增仓库信息接口 " + " smtWarehousesUpdateList:" + JSON.toJSONString(smtWarehousesAddList));
        basicFeignApi.batchSave(smtWarehousesAddList);

        // 释放Redis锁
        if (StringUtils.isNotEmpty(redisUtil.get("updateWarehouse"))) {
            redisUtil.del("updateWarehouse");
        }

        redisUtil.set(ConstantBase.API_LASTUPDATE_TIME_WAREHOUSE, DateUtils.getDateString(date, "yyyy-MM-dd HH:mm:ss"));

        return 1;
    }

    //从U9获取仓库数据
    public List<CustGetWhInfo> getWarehouseInfoFromU9(){

        //切换到从数据源
        DynamicDataSourceHolder.putDataSouce("secondary");
        String lastUpdateDate = (String) redisUtil.get(ConstantBase.API_LASTUPDATE_TIME_WAREHOUSE);
        Example example = new Example(CustGetWhInfo.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("orgid", constantBase.getDefaultOrgId());
        if (!StringUtils.isEmpty(lastUpdateDate)) {
//            criteria.andGreaterThanOrEqualTo("udpatetime", lastUpdateDate);
        }

        List<CustGetWhInfo> listWhInfos = custGetWharehouseInfoFromU9Mapper.selectByExample(example);
        if (CollectionUtils.isEmpty(listWhInfos)) {
            //恢复主数据源
            DynamicDataSourceHolder.removeDataSource();
            return null;
        }else {
            //恢复主数据源
            DynamicDataSourceHolder.removeDataSource();
            return listWhInfos;
        }
    }
}
