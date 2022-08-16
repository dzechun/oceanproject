package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBadnessCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCategory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseBadnessCategoryMapper;
import com.fantechs.provider.base.mapper.BaseHtBadnessCategoryMapper;
import com.fantechs.provider.base.service.BaseBadnessCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/02.
 */
@Service
public class BaseBadnessCategoryServiceImpl extends BaseService<BaseBadnessCategory> implements BaseBadnessCategoryService {

    @Resource
    private BaseBadnessCategoryMapper baseBadnessCategoryMapper;

    @Resource
    private BaseHtBadnessCategoryMapper baseHtBadnessCategoryMapper;

    @Override
    public List<BaseBadnessCategoryDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId",user.getOrganizationId());
        return baseBadnessCategoryMapper.findList(map);
    }

    @Override
    public int save(BaseBadnessCategory record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(record);

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());

        int i = baseBadnessCategoryMapper.insertUseGeneratedKeys(record);

        BaseHtBadnessCategory baseHtBadnessCategory = new BaseHtBadnessCategory();
        BeanUtils.copyProperties(record,baseHtBadnessCategory);
        baseHtBadnessCategoryMapper.insertSelective(baseHtBadnessCategory);

        return i;
    }

    @Override
    public int update(BaseBadnessCategory entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());
        BaseHtBadnessCategory baseHtBadnessCategory = new BaseHtBadnessCategory();
        BeanUtils.copyProperties(entity,baseHtBadnessCategory);
        baseHtBadnessCategoryMapper.insertSelective(baseHtBadnessCategory);

        return baseBadnessCategoryMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        List<BaseHtBadnessCategory> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseBadnessCategory baseBadnessCategory = baseBadnessCategoryMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseBadnessCategory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtBadnessCategory baseHtBadnessCategory = new BaseHtBadnessCategory();
            BeanUtils.copyProperties(baseBadnessCategory,baseHtBadnessCategory);
            list.add(baseHtBadnessCategory);
        }

        baseHtBadnessCategoryMapper.insertList(list);
        return baseBadnessCategoryMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(BaseBadnessCategory entity){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseBadnessCategory.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("badnessCategoryCode",entity.getBadnessCategoryCode());
        if (StringUtils.isNotEmpty(entity.getBadnessCategoryId())){
            criteria.andNotEqualTo("badnessCategoryId",entity.getBadnessCategoryId());
        }
        BaseBadnessCategory baseBadnessCategory = baseBadnessCategoryMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseBadnessCategory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }


    @Override
    public BaseBadnessCategory saveByApi(BaseBadnessCategory baseBadnessCategory) {
        Example example = new Example(BaseBadnessCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("badnessCategoryCode", baseBadnessCategory.getBadnessCategoryCode());
        criteria.andEqualTo("orgId", baseBadnessCategory.getOrgId());
        BaseBadnessCategory badnessCategory = baseBadnessCategoryMapper.selectOneByExample(example);
        example.clear();
        if(StringUtils.isEmpty(badnessCategory)){
            baseBadnessCategory.setCreateTime(new Date());
            baseBadnessCategoryMapper.insertUseGeneratedKeys(baseBadnessCategory);
        }else{
            baseBadnessCategory.setBadnessCategoryId(badnessCategory.getBadnessCategoryId());
            baseBadnessCategoryMapper.updateByPrimaryKeySelective(baseBadnessCategory);
        }
        return baseBadnessCategory;
    }

}
