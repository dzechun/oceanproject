package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseStorageMaterial;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtStorageMapper;
import com.fantechs.provider.base.mapper.BaseStorageMapper;
import com.fantechs.provider.base.mapper.BaseStorageMaterialMapper;
import com.fantechs.provider.base.service.BaseStorageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wcz on 2020/09/23.
 */
@Service
public class BaseStorageServiceImpl extends BaseService<BaseStorage> implements BaseStorageService {

    @Resource
    private BaseStorageMapper baseStorageMapper;
    @Resource
    private BaseHtStorageMapper baseHtStorageMapper;
    @Resource
    private BaseStorageMaterialMapper baseStorageMaterialMapper;

    @Override
    public int batchUpdate(List<BaseStorage> baseStorages) {
        return baseStorageMapper.batchUpdate(baseStorages);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseStorage baseStorage) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseStorage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("storageCode", baseStorage.getStorageCode());
        List<BaseStorage> baseStorages = baseStorageMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseStorages)) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        baseStorage.setCreateUserId(currentUser.getUserId());
        baseStorage.setCreateTime(new Date());
        baseStorage.setModifiedUserId(currentUser.getUserId());
        baseStorage.setModifiedTime(new Date());
        baseStorage.setOrganizationId(currentUser.getOrganizationId());
        baseStorageMapper.insertUseGeneratedKeys(baseStorage);

        //新增储位和物料关系
        List<BaseStorageMaterial> baseStorageMaterials = baseStorage.getBaseStorageMaterials();
        if(StringUtils.isNotEmpty(baseStorageMaterials)){
            for (BaseStorageMaterial baseStorageMaterial : baseStorageMaterials) {
                baseStorageMaterial.setStorageId(baseStorage.getStorageId());
            }
        }
        if(StringUtils.isNotEmpty(baseStorageMaterials)){
            baseStorageMaterialMapper.insertList(baseStorageMaterials);
        }

        //新增储位历史信息
        BaseHtStorage baseHtStorage = new BaseHtStorage();
        org.springframework.beans.BeanUtils.copyProperties(baseStorage, baseHtStorage);
        int i = baseHtStorageMapper.insertSelective(baseHtStorage);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i = 0;
        List<BaseHtStorage> list = new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] storageIds = ids.split(",");
        for (String storageId : storageIds) {
            BaseStorage baseStorage = baseStorageMapper.selectByPrimaryKey(Long.parseLong(storageId));
            if (StringUtils.isEmpty(baseStorage)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            //被储位物料引用
            Example example = new Example(BaseStorageMaterial.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("storageId", storageId);
            List<BaseStorageMaterial> baseStorageMaterials = baseStorageMaterialMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(baseStorageMaterials)) {
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
            BaseHtStorage baseHtStorage = new BaseHtStorage();
            org.springframework.beans.BeanUtils.copyProperties(baseStorage, baseHtStorage);
            baseHtStorage.setModifiedUserId(currentUser.getUserId());
            baseHtStorage.setModifiedTime(new Date());
            list.add(baseHtStorage);
        }
        baseHtStorageMapper.insertList(list);

        return baseStorageMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseStorage storage) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseStorage.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("storageCode", storage.getStorageCode());

        BaseStorage baseStorage = baseStorageMapper.selectOneByExample(example);

        if (StringUtils.isNotEmpty(baseStorage) && !baseStorage.getStorageId().equals(storage.getStorageId())) {
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        storage.setModifiedUserId(currentUser.getUserId());
        storage.setModifiedTime(new Date());
        storage.setOrganizationId(currentUser.getOrganizationId());
        int i = baseStorageMapper.updateByPrimaryKeySelective(storage);

        //新增储位历史信息
        BaseHtStorage baseHtStorage = new BaseHtStorage();
        org.springframework.beans.BeanUtils.copyProperties(storage, baseHtStorage);
        baseHtStorageMapper.insertSelective(baseHtStorage);

        //移除旧的绑定关系
        Example example1 = new Example(BaseStorageMaterial.class);
        Example.Criteria criteria1 = example1.createCriteria();
        criteria1.andEqualTo("storageId",baseStorage.getStorageId());
        baseStorageMaterialMapper.deleteByExample(example1);

        //新增绑定关系
        List<BaseStorageMaterial> baseStorageMaterials = storage.getBaseStorageMaterials();
        if(StringUtils.isNotEmpty(baseStorageMaterials)){
            for (BaseStorageMaterial baseStorageMaterial : baseStorageMaterials) {
                baseStorageMaterial.setStorageId(baseStorage.getStorageId());
            }
        }
        if(StringUtils.isNotEmpty(baseStorageMaterials)){
            baseStorageMaterialMapper.insertList(baseStorageMaterials);
        }

        return i;
    }

    @Override
    public List<BaseStorage> findList(SearchBaseStorage searchBaseStorage) {
        List<BaseStorage> baseStorages = baseStorageMapper.findList(searchBaseStorage);
        SearchBaseStorageMaterial searchBaseStorageMaterial = new SearchBaseStorageMaterial();

        for (BaseStorage baseStorage: baseStorages) {
            searchBaseStorageMaterial.setStorageId(baseStorage.getStorageId());
            List<BaseStorageMaterial> baseStorageMaterials = baseStorageMaterialMapper.findList(searchBaseStorageMaterial);
            if(StringUtils.isNotEmpty(baseStorageMaterials)){
                baseStorage.setBaseStorageMaterials(baseStorageMaterials);
            }
        }
        return baseStorages;
    }


}
