package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseInspectionExemptedList;
import com.fantechs.common.base.general.entity.basic.BaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionExemptedList;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInventoryStatus;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtInspectionExemptedListMapper;
import com.fantechs.provider.base.mapper.BaseInspectionExemptedListMapper;
import com.fantechs.provider.base.service.BaseInspectionExemptedListService;
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
 * Created by leifengzhi on 2021/05/21.
 */
@Service
public class BaseInspectionExemptedListServiceImpl extends BaseService<BaseInspectionExemptedList> implements BaseInspectionExemptedListService {

    @Resource
    private BaseInspectionExemptedListMapper baseInspectionExemptedListMapper;
    @Resource
    private BaseHtInspectionExemptedListMapper baseHtInspectionExemptedListMapper;

    @Override
    public List<BaseInspectionExemptedList> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return baseInspectionExemptedListMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseInspectionExemptedList baseInspectionExemptedList) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        baseInspectionExemptedList.setCreateUserId(user.getUserId());
        baseInspectionExemptedList.setCreateTime(new Date());
        baseInspectionExemptedList.setModifiedUserId(user.getUserId());
        baseInspectionExemptedList.setModifiedTime(new Date());
        baseInspectionExemptedList.setStatus(StringUtils.isEmpty(baseInspectionExemptedList.getStatus())?1: baseInspectionExemptedList.getStatus());
        baseInspectionExemptedList.setOrgId(user.getOrganizationId());
        int i = baseInspectionExemptedListMapper.insertUseGeneratedKeys(baseInspectionExemptedList);

        BaseHtInspectionExemptedList baseHtInspectionExemptedList = new BaseHtInspectionExemptedList();
        BeanUtils.copyProperties(baseInspectionExemptedList, baseHtInspectionExemptedList);
        baseHtInspectionExemptedListMapper.insert(baseHtInspectionExemptedList);

        return i;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseInspectionExemptedList baseInspectionExemptedList) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        baseInspectionExemptedList.setModifiedTime(new Date());
        baseInspectionExemptedList.setModifiedUserId(user.getUserId());
        baseInspectionExemptedList.setOrgId(user.getOrganizationId());

        BaseHtInspectionExemptedList baseHtInspectionExemptedList = new BaseHtInspectionExemptedList();
        BeanUtils.copyProperties(baseInspectionExemptedList, baseHtInspectionExemptedList);
        baseHtInspectionExemptedListMapper.insert(baseHtInspectionExemptedList);

        return baseInspectionExemptedListMapper.updateByPrimaryKeySelective(baseInspectionExemptedList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtInspectionExemptedList> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BaseInspectionExemptedList baseInspectionExemptedList = baseInspectionExemptedListMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(baseInspectionExemptedList)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BaseHtInspectionExemptedList baseHtInspectionExemptedList = new BaseHtInspectionExemptedList();
            BeanUtils.copyProperties(baseInspectionExemptedList, baseHtInspectionExemptedList);
            list.add(baseHtInspectionExemptedList);
        }

        baseHtInspectionExemptedListMapper.insertList(list);

        return baseInspectionExemptedListMapper.deleteByIds(ids);
    }
}
