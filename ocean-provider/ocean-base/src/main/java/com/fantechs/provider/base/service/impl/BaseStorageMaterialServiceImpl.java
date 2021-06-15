package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.entity.basic.BaseStorageMaterial;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorageMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageMaterial;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtStorageMaterialMapper;
import com.fantechs.provider.base.mapper.BaseStorageMaterialMapper;
import com.fantechs.provider.base.service.BaseStorageMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wcz on 2020/09/24.
 */
@Service
public class BaseStorageMaterialServiceImpl extends BaseService<BaseStorageMaterial> implements BaseStorageMaterialService {

    @Resource
    private BaseStorageMaterialMapper baseStorageMaterialMapper;

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
}
