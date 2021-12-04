package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigCategoryDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentCategory;
import com.fantechs.common.base.general.entity.eam.EamJigCategory;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentCategory;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigCategory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtJigCategoryMapper;
import com.fantechs.provider.eam.mapper.EamJigCategoryMapper;
import com.fantechs.provider.eam.service.EamJigCategoryService;
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
 *
 * Created by leifengzhi on 2021/07/28.
 */
@Service
public class EamJigCategoryServiceImpl extends BaseService<EamJigCategory> implements EamJigCategoryService {

    @Resource
    private EamJigCategoryMapper eamJigCategoryMapper;
    @Resource
    private EamHtJigCategoryMapper eamHtJigCategoryMapper;

    @Override
    public List<EamJigCategoryDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());

        return eamJigCategoryMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamJigCategory record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        ifRepeat(record);

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamJigCategoryMapper.insertUseGeneratedKeys(record);

        EamHtJigCategory eamHtJigCategory = new EamHtJigCategory();
        BeanUtils.copyProperties(record, eamHtJigCategory);
        int i = eamHtJigCategoryMapper.insertSelective(eamHtJigCategory);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamJigCategory entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        ifRepeat(entity);

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());

        EamHtJigCategory eamHtJigCategory = new EamHtJigCategory();
        BeanUtils.copyProperties(entity, eamHtJigCategory);
        eamHtJigCategoryMapper.insertSelective(eamHtJigCategory);

        return eamJigCategoryMapper.updateByPrimaryKeySelective(entity);
    }

    public void ifRepeat(EamJigCategory eamJigCategory){
        Example example = new Example(EamJigCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("jigCategoryCode", eamJigCategory.getJigCategoryCode());
        if(StringUtils.isNotEmpty(eamJigCategory.getJigCategoryId())){
            criteria.andNotEqualTo("jigCategoryId",eamJigCategory.getJigCategoryId());
        }
        EamJigCategory jigCategory = eamJigCategoryMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(jigCategory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("jigCategoryName", eamJigCategory.getJigCategoryName());
        if(StringUtils.isNotEmpty(eamJigCategory.getJigCategoryId())){
            criteria1.andNotEqualTo("jigCategoryId",eamJigCategory.getJigCategoryId());
        }
        EamJigCategory eamJigCategory1 = eamJigCategoryMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamJigCategory1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001.getCode(),"名称重复");
        }

    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<EamHtJigCategory> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamJigCategory eamJigCategory = eamJigCategoryMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamJigCategory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            EamHtJigCategory eamHtJigCategory = new EamHtJigCategory();
            BeanUtils.copyProperties(eamJigCategory, eamHtJigCategory);
            list.add(eamHtJigCategory);
        }

        eamHtJigCategoryMapper.insertList(list);

        return eamJigCategoryMapper.deleteByIds(ids);
    }
}
