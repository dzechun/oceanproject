package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.basic.SmtStorageMaterial;
import com.fantechs.common.base.entity.basic.history.SmtHtStorage;
import com.fantechs.common.base.entity.basic.qis.QisResultBean;
import com.fantechs.common.base.entity.basic.qis.QisWareHouseCW;
import com.fantechs.common.base.entity.basic.search.SearchSmtStorage;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.imes.basic.config.ConstantBase;
import com.fantechs.provider.imes.basic.config.RestURL;
import com.fantechs.provider.imes.basic.mapper.SmtHtStorageMapper;
import com.fantechs.provider.imes.basic.mapper.SmtStorageMapper;
import com.fantechs.provider.imes.basic.mapper.SmtStorageMaterialMapper;
import com.fantechs.provider.imes.basic.service.SmtStorageService;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by wcz on 2020/09/23.
 */
@Service
public class SmtStorageServiceImpl extends BaseService<SmtStorage> implements SmtStorageService {

    @Resource
    private SmtStorageMapper smtStorageMapper;
    @Resource
    private SmtHtStorageMapper smtHtStorageMapper;
    @Resource
    private SmtStorageMaterialMapper smtStorageMaterialMapper;
    @Resource
    RestURL restURL;
    @Resource
    ConstantBase constantBase;
    @Resource
    RedisUtil redisUtil;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int getNewUpdateCWByUpdateDate() throws Exception {

        Date date = new Date();
        int i = 0, j = 0;
        String lastUpdateDate = (String) redisUtil.get(ConstantBase.API_LASTUPDATE_TIME_CW);
        lastUpdateDate = "2017-01-01T09:50:25.095Z";
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotEmpty(lastUpdateDate)) {
            map.put("updated", lastUpdateDate);
        } else {
            map = null;
        }
        String url = restURL.getQisGetNewUpdateCW();
        String result = RestTemplateUtil.postForString(url, map);
        QisResultBean<List<QisWareHouseCW>> responseEntity = BeanUtils.convertJson(result, new TypeToken<QisResultBean<List<QisWareHouseCW>>>() {
        }.getType());
        if (responseEntity.getCode() != 200) {
            throw new Exception("获取QIS储位信息失败：" + responseEntity.getMessage());
        }

        List<SmtStorage> storageUpdateList = new ArrayList<>();//批量更新储位集合
        List<SmtStorage> storageAddList = new ArrayList<>();//批量新增储位集合


        List<QisWareHouseCW> qisWareHouseCWList = responseEntity.getResult();

        Example example = new Example(SmtStorage.class);
        Example.Criteria criteria = example.createCriteria();
        for (QisWareHouseCW qisWareHouseCW : qisWareHouseCWList) {
            if (constantBase.getDefaultOrgName().equals(qisWareHouseCW.getOrgname())) {
                SmtStorage smtStorage = new SmtStorage();
                smtStorage.setWarehouseName(qisWareHouseCW.getCkName());
                smtStorage.setWarehouseCode(qisWareHouseCW.getCkNo());
                smtStorage.setStorageCode(qisWareHouseCW.getCode());
                smtStorage.setStorageName(qisWareHouseCW.getName());

                criteria.andEqualTo("storageCode", smtStorage.getStorageCode());
                SmtStorage storage = smtStorageMapper.selectOneByExample(example);
                if (StringUtils.isNotEmpty(storage)) {
                    smtStorage.setCreateTime(new Date());
                    smtStorage.setModifiedTime(new Date());
                    storageUpdateList.add(smtStorage);
                } else {
                    smtStorage.setCreateTime(new Date());
                    storageAddList.add(smtStorage);
                }
            }
            if (qisWareHouseCWList.size() > 1000 && storageAddList.size()>0 && storageAddList.size() % 1000 == 0) {
                //批量更新储位
                if (StringUtils.isNotEmpty(storageUpdateList)) {
                    i = batchUpdate(storageUpdateList);
                    storageAddList.clear();
                }
                //批量新增储位
                if (StringUtils.isNotEmpty(storageAddList)) {
                    j = batchAdd(storageAddList);
                    storageAddList.clear();
                }
            }
        }

        //批量更新储位
        if (StringUtils.isNotEmpty(storageUpdateList)) {
            i = batchUpdate(storageUpdateList);
        }
        //批量新增储位
        if (StringUtils.isNotEmpty(storageAddList)) {
            j = batchAdd(storageAddList);
        }


        redisUtil.set(ConstantBase.API_LASTUPDATE_TIME_CW, DateUtils.getDateString(date, "yyyy-MM-dd HH:mm:ss"));
        return i + j;
    }

    @Override
    public int batchUpdate(List<SmtStorage> smtStorages) {
        return smtStorageMapper.batchUpdate(smtStorages);
    }

    @Override
    public int batchAdd(List<SmtStorage> smtStorages) {
        return smtStorageMapper.batchAdd(smtStorages);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtStorage smtStorage) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtStorage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("storageCode", smtStorage.getStorageCode());
        List<SmtStorage> smtStorages = smtStorageMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtStorages)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtStorage.setCreateUserId(currentUser.getUserId());
        smtStorage.setCreateTime(new Date());
        smtStorage.setModifiedUserId(currentUser.getUserId());
        smtStorage.setModifiedTime(new Date());
        smtStorageMapper.insertUseGeneratedKeys(smtStorage);

        //新增储位历史信息
        SmtHtStorage smtHtStorage = new SmtHtStorage();
        org.springframework.beans.BeanUtils.copyProperties(smtStorage, smtHtStorage);
        int i = smtHtStorageMapper.insertSelective(smtHtStorage);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i = 0;
        List<SmtHtStorage> list = new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] storageIds = ids.split(",");
        for (String storageId : storageIds) {
            SmtStorage smtStorage = smtStorageMapper.selectByPrimaryKey(Long.parseLong(storageId));
            if (StringUtils.isEmpty(smtStorage)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被储位物料引用
            Example example = new Example(SmtStorageMaterial.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("storageId", storageId);
            List<SmtStorageMaterial> smtStorageMaterials = smtStorageMaterialMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(smtStorageMaterials)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            //该储位和电子标签控制器绑定
//            SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
//            searchSmtElectronicTagStorage.setStorageId(storageId);
//            ResponseEntity<List<SmtElectronicTagStorageDto>> list1 = electronicTagFeignApi.findList(searchSmtElectronicTagStorage);
//            if (StringUtils.isNotEmpty(list1.getData().get(0))){
//                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
//            }

            //新增储位历史信息
            SmtHtStorage smtHtStorage = new SmtHtStorage();
            org.springframework.beans.BeanUtils.copyProperties(smtStorage, smtHtStorage);
            smtHtStorage.setModifiedUserId(currentUser.getUserId());
            smtHtStorage.setModifiedTime(new Date());
            list.add(smtHtStorage);
        }
        smtHtStorageMapper.insertList(list);

        return smtStorageMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtStorage storage) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtStorage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("storageCode", storage.getStorageCode());

        SmtStorage smtStorage = smtStorageMapper.selectOneByExample(example);

        if (StringUtils.isNotEmpty(smtStorage) && !smtStorage.getStorageId().equals(storage.getStorageId())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        storage.setModifiedUserId(currentUser.getUserId());
        storage.setModifiedTime(new Date());
        int i = smtStorageMapper.updateByPrimaryKeySelective(storage);

        //新增储位历史信息
        SmtHtStorage smtHtStorage = new SmtHtStorage();
        org.springframework.beans.BeanUtils.copyProperties(storage, smtHtStorage);
        smtHtStorageMapper.insertSelective(smtHtStorage);
        return i;
    }

    @Override
    public List<SmtStorage> findList(SearchSmtStorage searchSmtStorage) {
        return smtStorageMapper.findList(searchSmtStorage);
    }
}
