package com.fantechs.provider.restapi.imes.service.impl;

import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.basic.SmtWarehouse;
import com.fantechs.common.base.entity.basic.qis.QisResultBean;
import com.fantechs.common.base.entity.basic.qis.QisWareHouseCW;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorage;
import com.fantechs.common.base.entity.basic.search.SearchSmtWarehouse;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.restapi.imes.config.ConstantBase;
import com.fantechs.provider.restapi.imes.config.DataSource;
import com.fantechs.provider.restapi.imes.config.RestURL;
import com.fantechs.provider.restapi.imes.service.GetDataFromQisService;
import com.fantechs.provider.restapi.imes.service.GetDataFromU9Service;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class GetDataFromQisServiceImpl implements GetDataFromQisService {

    private Logger logger = LoggerFactory.getLogger(GetDataFromQisService.class);

    @Resource
    private RestURL restURL;
    @Resource
    private ConstantBase constantBase;
    @Resource
    private GetDataFromU9Service getDataFromQisService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private BasicFeignApi basicFeignApi;


    @Override
    @Transactional(rollbackFor = Exception.class)
    @DataSource
    public int updateStorageFromQis() throws Exception {

        Date date = new Date();
        String lastUpdateDate = (String) redisUtil.get(ConstantBase.API_LASTUPDATE_TIME_CW);
        //lastUpdateDate = "2017-01-01T07:50:46.963Z";
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotEmpty(lastUpdateDate)) {
            map.put("updated", lastUpdateDate);
        }

        getDataFromQisService.updateWarehouse();

        //同步U9仓库完成，获取本地的仓库数据
        SearchSmtWarehouse searchSmtWarehouse1 = new SearchSmtWarehouse();
        searchSmtWarehouse1.setPageSize(999999);
        ResponseEntity<List<SmtWarehouse>> responseEntity = basicFeignApi.findList(searchSmtWarehouse1);
        List<SmtWarehouse> warehouseList = responseEntity.getData();

        //同步QIS储位数据
        String url = restURL.getQisGetNewUpdateCW();
        String result = RestTemplateUtil.postForString(url, map);
        QisResultBean<List<QisWareHouseCW>> responseEntity1 = BeanUtils.convertJson(result, new TypeToken<QisResultBean<List<QisWareHouseCW>>>() {
        }.getType());
        if (responseEntity1.getCode() != 200) {
            throw new Exception("获取QIS储位信息失败：" + responseEntity.getMessage());
        }

        List<SmtStorage> storageUpdateList = new ArrayList<>();//批量更新储位集合
        List<SmtStorage> storageAddList = new ArrayList<>();//批量新增储位集合

        SearchSmtStorage searchSmtStorage = new SearchSmtStorage();//储位查询实体
        searchSmtStorage.setCodeQueryMark((byte) 1);//对编码做等值查询

        List<QisWareHouseCW> qisWareHouseCWList = responseEntity1.getResult();

        for (QisWareHouseCW qisWareHouseCW : qisWareHouseCWList) {
            if (constantBase.getDefaultOrgName().equals(qisWareHouseCW.getOrgname())) {

                SmtStorage smtStorage = new SmtStorage();//储位对象
                smtStorage.setStorageCode(qisWareHouseCW.getCode());
                smtStorage.setStorageName(qisWareHouseCW.getName());
                smtStorage.setCreateTime(new Date());
                smtStorage.setModifiedTime(new Date());
                if (StringUtils.isNotEmpty(warehouseList)){
                    for (SmtWarehouse smtWarehouse : warehouseList) {
                        if (smtWarehouse.getWarehouseCode().equals(qisWareHouseCW.getCkNo())){
                            smtStorage.setWarehouseId(smtWarehouse.getWarehouseId());
                        }
                    }
                }

                //判断对储位执行新增还是更新
                searchSmtStorage.setStorageCode(smtStorage.getStorageCode());
                ResponseEntity<List<SmtStorage>> storageResponseEntity = basicFeignApi.findList(searchSmtStorage);
                if (StringUtils.isNotEmpty(storageResponseEntity.getData())) {
                    storageUpdateList.add(smtStorage);
                } else {
                    storageAddList.add(smtStorage);
                }
            }

            //每1000条数据批量操作储位
            if (qisWareHouseCWList.size() > 1000 && storageAddList.size() > 0 && storageAddList.size() % 1000 == 0) {
                //批量更新储位
                if (StringUtils.isNotEmpty(storageUpdateList)) {
                    basicFeignApi.batchUpdate(storageUpdateList);
                    storageAddList.clear();
                }
                //批量新增储位
                if (StringUtils.isNotEmpty(storageAddList)) {
                    basicFeignApi.batchAdd(storageAddList);
                    storageAddList.clear();
                }
            }

        }

        //数量小于1000时的批量操作
        //批量更新储位
        if (StringUtils.isNotEmpty(storageUpdateList)) {
            basicFeignApi.batchUpdate(storageUpdateList);
            storageAddList.clear();
        }
        //批量新增储位
        if (StringUtils.isNotEmpty(storageAddList)) {
            basicFeignApi.batchAdd(storageAddList);
            storageAddList.clear();
        }

        redisUtil.set(ConstantBase.API_LASTUPDATE_TIME_CW, DateUtils.getDateString(date, "yyyy-MM-dd HH:mm:ss"));
        return 1;
    }

}
