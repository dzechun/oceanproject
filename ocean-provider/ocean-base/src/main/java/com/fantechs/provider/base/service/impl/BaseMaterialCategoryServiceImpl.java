package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseMaterialCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterialCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialCategory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtMaterialCategoryMapper;
import com.fantechs.provider.base.mapper.BaseMaterialCategoryMapper;
import com.fantechs.provider.base.service.BaseMaterialCategoryService;
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
 * Created by leifengzhi on 2020/12/31.
 */
@Service
public class BaseMaterialCategoryServiceImpl extends BaseService<BaseMaterialCategory> implements BaseMaterialCategoryService {

    @Resource
    private BaseMaterialCategoryMapper baseMaterialCategoryMapper;
    @Resource
    private BaseHtMaterialCategoryMapper baseHtMaterialCategoryMapper;

    @Override
    public List<BaseMaterialCategoryDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        map.put("orgId", user.getOrganizationId());
        return baseMaterialCategoryMapper.findList(map);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(BaseMaterialCategory baseMaterialCategory) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        baseMaterialCategory.setCreateTime(new Date());
        baseMaterialCategory.setCreateUserId(user.getUserId());
        baseMaterialCategory.setModifiedTime(new Date());
        baseMaterialCategory.setModifiedUserId(user.getUserId());
        baseMaterialCategory.setOrganizationId(user.getOrganizationId());
        baseMaterialCategory.setStatus(StringUtils.isEmpty(baseMaterialCategory.getStatus())?1: baseMaterialCategory.getStatus());

        baseMaterialCategoryMapper.insertUseGeneratedKeys(baseMaterialCategory);

        BaseHtMaterialCategory baseHtMaterialCategory = new BaseHtMaterialCategory();
        BeanUtils.copyProperties(baseMaterialCategory, baseHtMaterialCategory);
        int i = baseHtMaterialCategoryMapper.insert(baseHtMaterialCategory);

        return i;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int update(BaseMaterialCategory baseMaterialCategory) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        baseMaterialCategory.setModifiedTime(new Date());
        baseMaterialCategory.setModifiedUserId(user.getUserId());

        BaseHtMaterialCategory baseHtMaterialCategory = new BaseHtMaterialCategory();
        BeanUtils.copyProperties(baseMaterialCategory, baseHtMaterialCategory);
        baseHtMaterialCategoryMapper.insert(baseHtMaterialCategory);

        return baseMaterialCategoryMapper.updateByPrimaryKeySelective(baseMaterialCategory);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<BaseHtMaterialCategory> srmHtDeliveryNotes= new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseMaterialCategory baseMaterialCategory = baseMaterialCategoryMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseMaterialCategory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            Example example = new Example(BaseMaterialCategory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("parentId",id);
            List<BaseMaterialCategory> smtMaterialCategories = baseMaterialCategoryMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(smtMaterialCategories)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            BaseHtMaterialCategory baseHtMaterialCategory = new BaseHtMaterialCategory();
            BeanUtils.copyProperties(baseMaterialCategory, baseHtMaterialCategory);
            srmHtDeliveryNotes.add(baseHtMaterialCategory);
        }

        baseHtMaterialCategoryMapper.insertList(srmHtDeliveryNotes);

        return baseMaterialCategoryMapper.deleteByIds(ids);
    }
}
