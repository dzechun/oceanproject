package com.fantechs.provider.eam.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.EamSparePart;
import com.fantechs.common.base.general.entity.eam.EamSparePartCategory;
import com.fantechs.common.base.general.entity.eam.history.EamHtSparePart;
import com.fantechs.common.base.general.entity.eam.history.EamHtSparePartCategory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamHtSparePartCategoryMapper;
import com.fantechs.provider.eam.mapper.EamSparePartCategoryMapper;
import com.fantechs.provider.eam.service.EamSparePartCategoryService;
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
 * Created by leifengzhi on 2021/09/17.
 */
@Service
public class EamSparePartCategoryServiceImpl extends BaseService<EamSparePartCategory> implements EamSparePartCategoryService {

    @Resource
    private EamSparePartCategoryMapper eamSparePartCategoryMapper;

    @Resource
    private EamHtSparePartCategoryMapper eamHtSparePartCategoryMapper;

    @Override
    public List<EamSparePartCategory> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return eamSparePartCategoryMapper.findList(map);
    }

    @Override
    public List<EamHtSparePartCategory> findHtList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return eamHtSparePartCategoryMapper.findHtList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(EamSparePartCategory record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(EamSparePartCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sparePartCategoryCode", record.getSparePartCategoryCode());
        EamSparePartCategory eamSparePartCategory = eamSparePartCategoryMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamSparePartCategory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateUserId(user.getUserId());
        record.setCreateTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setStatus(StringUtils.isEmpty(record.getStatus())?1: record.getStatus());
        record.setOrgId(user.getOrganizationId());
        eamSparePartCategoryMapper.insertUseGeneratedKeys(record);

        //履历
        EamHtSparePartCategory eamHtSparePartCategory = new EamHtSparePartCategory();
        BeanUtils.copyProperties(record, eamHtSparePartCategory);
        int i = eamHtSparePartCategoryMapper.insertSelective(eamHtSparePartCategory);
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(EamSparePartCategory entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(EamSparePartCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sparePartCategoryCode", entity.getSparePartCategoryCode())
                .andNotEqualTo("sparePartCategoryId",entity.getSparePartCategoryId());
        EamSparePartCategory eamSparePartCategory = eamSparePartCategoryMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(eamSparePartCategory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(user.getUserId());
        eamSparePartCategoryMapper.updateByPrimaryKeySelective(entity);

        //履历
        EamHtSparePartCategory eamHtSparePartCategory = new EamHtSparePartCategory();
        BeanUtils.copyProperties(entity, eamHtSparePartCategory);
        int i = eamHtSparePartCategoryMapper.insertSelective(eamHtSparePartCategory);
        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        List<EamHtSparePartCategory> list = new ArrayList<>();
        String[] idArry = ids.split(",");
        for (String id : idArry) {
            EamSparePartCategory eamSparePartCategory = eamSparePartCategoryMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(eamSparePartCategory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            EamHtSparePartCategory eamHtSparePartCategory = new EamHtSparePartCategory();
            BeanUtils.copyProperties(eamSparePartCategory, eamHtSparePartCategory);
            list.add(eamHtSparePartCategory);
        }
        //履历
        eamHtSparePartCategoryMapper.insertList(list);

        return eamSparePartCategoryMapper.deleteByIds(ids);
    }
}
