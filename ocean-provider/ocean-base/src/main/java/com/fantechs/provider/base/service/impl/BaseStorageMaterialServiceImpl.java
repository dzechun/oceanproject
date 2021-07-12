package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.imports.BaseStorageImport;
import com.fantechs.common.base.general.dto.basic.imports.BaseStorageMaterialImport;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorage;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorageMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.*;
import com.fantechs.provider.base.service.BaseStorageMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by wcz on 2020/09/24.
 */
@Service
public class BaseStorageMaterialServiceImpl extends BaseService<BaseStorageMaterial> implements BaseStorageMaterialService {

    @Resource
    private BaseStorageMaterialMapper baseStorageMaterialMapper;
    @Resource
    private BaseStorageMapper baseStorageMapper;
    @Resource
    private BaseMaterialMapper baseMaterialMapper;
    @Resource
    private BaseMaterialOwnerMapper baseMaterialOwnerMapper;
    @Resource
    private BaseHtStorageMaterialMapper baseHtStorageMaterialMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseStorageMaterial baseStorageMaterial) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        baseStorageMaterial.setCreateUserId(currentUser.getUserId());
        baseStorageMaterial.setCreateTime(new Date());
        baseStorageMaterial.setModifiedUserId(currentUser.getUserId());
        baseStorageMaterial.setModifiedTime(new Date());
        baseStorageMaterial.setOrganizationId(currentUser.getOrganizationId());
        baseStorageMaterialMapper.insertUseGeneratedKeys(baseStorageMaterial);

        //新增储位物料历史信息
        BaseHtStorageMaterial baseHtStorageMaterial = new BaseHtStorageMaterial();
        BeanUtils.copyProperties(baseStorageMaterial, baseHtStorageMaterial);
        int i = baseHtStorageMaterialMapper.insertSelective(baseHtStorageMaterial);
        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        int i = 0;
        List<BaseHtStorageMaterial> list = new ArrayList<>();

        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] storageMaterialIds = ids.split(",");
        for (String storageMaterialId : storageMaterialIds) {
            BaseStorageMaterial baseStorageMaterial = baseStorageMaterialMapper.selectByPrimaryKey(Long.parseLong(storageMaterialId));
            if (StringUtils.isEmpty(baseStorageMaterial)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增储位物料历史信息
            BaseHtStorageMaterial baseHtStorageMaterial = new BaseHtStorageMaterial();
            BeanUtils.copyProperties(baseStorageMaterial, baseHtStorageMaterial);
            baseHtStorageMaterial.setModifiedUserId(currentUser.getUserId());
            baseHtStorageMaterial.setModifiedTime(new Date());
            list.add(baseHtStorageMaterial);
        }
        baseHtStorageMaterialMapper.insertList(list);

        return baseStorageMaterialMapper.deleteByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseStorageMaterial baseStorageMaterial) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUser)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        baseStorageMaterial.setModifiedUserId(currentUser.getUserId());
        baseStorageMaterial.setModifiedTime(new Date());
        baseStorageMaterial.setOrganizationId(currentUser.getOrganizationId());
        int i = baseStorageMaterialMapper.updateByPrimaryKeySelective(baseStorageMaterial);

        //新增储位物料历史信息
        BaseHtStorageMaterial baseHtStorageMaterial = new BaseHtStorageMaterial();
        BeanUtils.copyProperties(baseStorageMaterial, baseHtStorageMaterial);
        baseHtStorageMaterialMapper.insertSelective(baseHtStorageMaterial);
        return i;
    }


    @Override
    public List<BaseStorageMaterial> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return baseStorageMaterialMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importExcel(List<BaseStorageMaterialImport> baseStorageMaterialImports) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Map<String, Object> resutlMap = new HashMap<>();  //封装操作结果
        int success = 0;  //记录操作成功数
        List<Integer> fail = new ArrayList<>();  //记录操作失败行数
        LinkedList<BaseStorageMaterial> list = new LinkedList<>();
        LinkedList<BaseHtStorageMaterial> htList = new LinkedList<>();
        ArrayList<BaseStorageMaterialImport> storageMaterialImports = new ArrayList<>();

        for (int i = 0; i < baseStorageMaterialImports.size(); i++) {
            BaseStorageMaterialImport baseStorageMaterialImport = baseStorageMaterialImports.get(i);
            String storageCode = baseStorageMaterialImport.getStorageCode();
            String materialCode = baseStorageMaterialImport.getMaterialCode();
            String materialOwnerCode = baseStorageMaterialImport.getMaterialOwnerCode();

            if (StringUtils.isEmpty(
                    storageCode,materialCode,materialOwnerCode
            )){
                fail.add(i+4);
                continue;
            }

            //判断库位是否存在
            Example example1 = new Example(BaseStorage.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria1.andEqualTo("storageCode",storageCode);
            BaseStorage baseStorage = baseStorageMapper.selectOneByExample(example1);
            if (StringUtils.isEmpty(baseStorage)){
                fail.add(i+4);
                continue;
            }
            baseStorageMaterialImport.setStorageId(baseStorage.getStorageId());

            //判断物料信息是否存在
            Example example2 = new Example(BaseMaterial.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("organizationId", currentUser.getOrganizationId());
            criteria2.andEqualTo("materialCode",materialCode);
            BaseMaterial baseMaterial = baseMaterialMapper.selectOneByExample(example2);
            if (StringUtils.isEmpty(baseMaterial)){
                fail.add(i+4);
                continue;
            }
            baseStorageMaterialImport.setMaterialId(baseMaterial.getMaterialId());

            //判断货主信息是否存在
            Example example3 = new Example(BaseMaterialOwner.class);
            Example.Criteria criteria3 = example3.createCriteria();
            criteria3.andEqualTo("orgId", currentUser.getOrganizationId());
            criteria3.andEqualTo("materialOwnerCode",materialOwnerCode);
            BaseMaterialOwner baseMaterialOwner = baseMaterialOwnerMapper.selectOneByExample(example3);
            if (StringUtils.isEmpty(baseMaterialOwner)){
                fail.add(i+4);
                continue;
            }
            baseStorageMaterialImport.setMaterialOwnerId(baseMaterialOwner.getMaterialOwnerId());

            storageMaterialImports.add(baseStorageMaterialImport);
        }

        if(StringUtils.isNotEmpty(storageMaterialImports)) {
            for (BaseStorageMaterialImport baseStorageMaterialImport : storageMaterialImports) {
                BaseStorageMaterial baseStorageMaterial = new BaseStorageMaterial();
                BeanUtils.copyProperties(baseStorageMaterialImport, baseStorageMaterial);
                baseStorageMaterial.setStatus(1);
                baseStorageMaterial.setCreateTime(new Date());
                baseStorageMaterial.setCreateUserId(currentUser.getUserId());
                baseStorageMaterial.setModifiedTime(new Date());
                baseStorageMaterial.setModifiedUserId(currentUser.getUserId());
                baseStorageMaterial.setOrganizationId(currentUser.getOrganizationId());
                baseStorageMaterial.setPutawayTactics(baseStorageMaterialImport.getPutawayTactics().byteValue());
                baseStorageMaterial.setReplenishTactics(baseStorageMaterialImport.getReplenishTactics().byteValue());
                list.add(baseStorageMaterial);
            }
        }

        if (StringUtils.isNotEmpty(list)){
            success = baseStorageMaterialMapper.insertList(list);
        }

        for (BaseStorageMaterial baseStorageMaterial : list) {
            BaseHtStorageMaterial baseHtStorageMaterial = new BaseHtStorageMaterial();
            BeanUtils.copyProperties(baseStorageMaterial, baseHtStorageMaterial);
            htList.add(baseHtStorageMaterial);
        }

        if (StringUtils.isNotEmpty(htList)){
            baseHtStorageMaterialMapper.insertList(htList);
        }

        resutlMap.put("操作成功总数",success);
        resutlMap.put("操作失败行数",fail);
        return resutlMap;
    }
}
