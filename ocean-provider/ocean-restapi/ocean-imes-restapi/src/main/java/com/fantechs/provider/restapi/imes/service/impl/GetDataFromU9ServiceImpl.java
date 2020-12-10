package com.fantechs.provider.restapi.imes.service.impl;

import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.common.base.entity.basic.U9.CustGetItemInfo;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.RedisUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.restapi.imes.config.ConstantBase;
import com.fantechs.provider.restapi.imes.config.DynamicDataSourceHolder;
import com.fantechs.provider.restapi.imes.mapper.GetDataFromU9Mapper;
import com.fantechs.provider.restapi.imes.service.GetDataFromU9Service;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GetDataFromU9ServiceImpl implements GetDataFromU9Service {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ConstantBase constantBase;
    @Resource
    private GetDataFromU9Mapper getDataFromU9Mapper;
    @Resource
    private BasicFeignApi basicFeignApi;

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
        List<CustGetItemInfo> listItemInfos = getDataFromU9Mapper.selectByExample(example);
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
}
