package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseProductMaterialReP;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessReM;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductMaterialReP;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductProcessReM;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProductMaterialRePMapper;
import com.fantechs.provider.base.mapper.BaseProductMaterialRePMapper;
import com.fantechs.provider.base.service.BaseProductMaterialRePService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/28.
 */
@Service
public class BaseProductMaterialRePServiceImpl extends BaseService<BaseProductMaterialReP> implements BaseProductMaterialRePService {

    @Resource
    private BaseProductMaterialRePMapper baseProductMaterialRePMapper;

    @Resource
    private BaseHtProductMaterialRePMapper baseHtProductMaterialRePMapper;

    @Override
    public List<BaseProductMaterialReP> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseProductMaterialRePMapper.findList(map);
    }

    @Override
    public List<BaseHtProductMaterialReP> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseHtProductMaterialRePMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseProductMaterialReP baseProductMaterialReP) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        baseProductMaterialReP.setCreateUserId(user.getUserId());
        baseProductMaterialReP.setCreateTime(new Date());
        baseProductMaterialReP.setModifiedUserId(user.getUserId());
        baseProductMaterialReP.setModifiedTime(new Date());
        baseProductMaterialReP.setStatus(StringUtils.isEmpty(baseProductMaterialReP.getStatus())?1:baseProductMaterialReP.getStatus());
        baseProductMaterialReP.setOrgId(user.getOrganizationId());
        int i = baseProductMaterialRePMapper.insertUseGeneratedKeys(baseProductMaterialReP);

        BaseHtProductMaterialReP baseHtProductMaterialReP = new BaseHtProductMaterialReP();
        BeanUtils.copyProperties(baseProductMaterialReP, baseHtProductMaterialReP);
        baseHtProductMaterialRePMapper.insertSelective(baseHtProductMaterialReP);

        return i;

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseProductMaterialReP baseProductMaterialReP) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        baseProductMaterialReP.setModifiedUserId(user.getUserId());
        baseProductMaterialReP.setModifiedTime(new Date());
        baseProductMaterialReP.setOrgId(user.getOrganizationId());
        int i=baseProductMaterialRePMapper.updateByPrimaryKeySelective(baseProductMaterialReP);

        BaseHtProductMaterialReP baseHtProductMaterialReP = new BaseHtProductMaterialReP();
        BeanUtils.copyProperties(baseProductMaterialReP, baseHtProductMaterialReP);
        baseHtProductMaterialRePMapper.insertSelective(baseHtProductMaterialReP);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtProductMaterialReP> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseProductMaterialReP baseProductMaterialReP = baseProductMaterialRePMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseProductMaterialReP)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtProductMaterialReP baseHtProductMaterialReP = new BaseHtProductMaterialReP();
            BeanUtils.copyProperties(baseProductMaterialReP, baseHtProductMaterialReP);
            list.add(baseHtProductMaterialReP);
        }

        baseHtProductMaterialRePMapper.insertList(list);

        return baseProductMaterialRePMapper.deleteByIds(ids);
    }
}
