package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtMaterialCategoryDto;
import com.fantechs.common.base.entity.basic.SmtMaterialCategory;
import com.fantechs.common.base.entity.basic.history.SmtHtMaterialCategory;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtMaterialCategoryMapper;
import com.fantechs.provider.imes.basic.mapper.SmtMaterialCategoryMapper;
import com.fantechs.provider.imes.basic.service.SmtMaterialCategoryService;
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
 * Created by leifengzhi on 2020/12/31.
 */
@Service
public class SmtMaterialCategoryServiceImpl extends BaseService<SmtMaterialCategory> implements SmtMaterialCategoryService {

    @Resource
    private SmtMaterialCategoryMapper smtMaterialCategoryMapper;
    @Resource
    private SmtHtMaterialCategoryMapper smtHtMaterialCategoryMapper;

    @Override
    public List<SmtMaterialCategoryDto> findList(Map<String, Object> map) {
        return smtMaterialCategoryMapper.findList(map);
    }

    @Override
    public int save(SmtMaterialCategory smtMaterialCategory) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        smtMaterialCategory.setCreateTime(new Date());
        smtMaterialCategory.setCreateUserId(user.getUserId());
        smtMaterialCategory.setModifiedTime(new Date());
        smtMaterialCategory.setModifiedUserId(user.getUserId());
        smtMaterialCategory.setStatus(StringUtils.isEmpty(smtMaterialCategory.getStatus())?1:smtMaterialCategory.getStatus());

        int i = smtMaterialCategoryMapper.insertUseGeneratedKeys(smtMaterialCategory);

        SmtHtMaterialCategory smtHtMaterialCategory = new SmtHtMaterialCategory();
        BeanUtils.copyProperties(smtMaterialCategory,smtHtMaterialCategory);
        smtHtMaterialCategoryMapper.insert(smtHtMaterialCategory);

        return i;
    }

    @Override
    public int update(SmtMaterialCategory smtMaterialCategory) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        smtMaterialCategory.setModifiedTime(new Date());
        smtMaterialCategory.setModifiedUserId(user.getUserId());

        SmtHtMaterialCategory smtHtMaterialCategory = new SmtHtMaterialCategory();
        BeanUtils.copyProperties(smtMaterialCategory,smtHtMaterialCategory);
        smtHtMaterialCategoryMapper.insert(smtHtMaterialCategory);

        return smtMaterialCategoryMapper.updateByPrimaryKeySelective(smtMaterialCategory);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        List<SmtHtMaterialCategory> srmHtDeliveryNotes= new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            SmtMaterialCategory smtMaterialCategory = smtMaterialCategoryMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(smtMaterialCategory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            Example example = new Example(SmtMaterialCategory.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("parentId",id);
            List<SmtMaterialCategory> smtMaterialCategories = smtMaterialCategoryMapper.selectByExample(example);
            if (StringUtils.isNotEmpty(smtMaterialCategories)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012004);
            }

            SmtHtMaterialCategory smtHtMaterialCategory = new SmtHtMaterialCategory();
            BeanUtils.copyProperties(smtMaterialCategory,smtHtMaterialCategory);
            srmHtDeliveryNotes.add(smtHtMaterialCategory);
        }

        smtHtMaterialCategoryMapper.insertList(srmHtDeliveryNotes);

        return smtMaterialCategoryMapper.deleteByIds(ids);
    }
}
