package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtProcessCategoryDto;
import com.fantechs.common.base.entity.basic.SmtProcessCategory;
import com.fantechs.common.base.entity.basic.history.SmtHtProcessCategory;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.imes.basic.mapper.SmtHtProcessCategoryMapper;
import com.fantechs.provider.imes.basic.mapper.SmtProcessCategoryMapper;
import com.fantechs.provider.imes.basic.service.SmtProcessCategoryService;
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
 * Created by leifengzhi on 2020/10/15.
 */
@Service
public class SmtProcessCategoryServiceImpl extends BaseService<SmtProcessCategory> implements SmtProcessCategoryService {

    @Resource
    private SmtProcessCategoryMapper smtProcessCategoryMapper;

    @Resource
    private SmtHtProcessCategoryMapper smtHtProcessCategoryMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtProcessCategory smtProcessCategory) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProcessCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processCategoryCode",smtProcessCategory.getProcessCategoryCode());
        List<SmtProcessCategory> smtProcessCategories = smtProcessCategoryMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtProcessCategories)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProcessCategory.setCreateUserId(currentUserInfo.getUserId());
        smtProcessCategory.setCreateTime(new Date());
        smtProcessCategoryMapper.insertUseGeneratedKeys(smtProcessCategory);

        //新增工序类别历史信息
        SmtHtProcessCategory smtHtProcessCategory = new SmtHtProcessCategory();
        BeanUtils.copyProperties(smtProcessCategory,smtHtProcessCategory);
        return smtHtProcessCategoryMapper.insertSelective(smtHtProcessCategory);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtProcessCategory smtProcessCategory) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(SmtProcessCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("processCategoryCode",smtProcessCategory.getProcessCategoryCode());
        List<SmtProcessCategory> smtProcessCategories = smtProcessCategoryMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(smtProcessCategories)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        smtProcessCategory.setModifiedUserId(currentUserInfo.getUserId());
        smtProcessCategory.setModifiedTime(new Date());
        smtProcessCategoryMapper.updateByPrimaryKeySelective(smtProcessCategory);

        //新增工序列表历史信息
        SmtHtProcessCategory smtHtProcessCategory = new SmtHtProcessCategory();
        BeanUtils.copyProperties(smtProcessCategory,smtHtProcessCategory);
        smtHtProcessCategory.setModifiedTime(new Date());
        smtHtProcessCategory.setModifiedUserId(currentUserInfo.getUserId());
        return smtHtProcessCategoryMapper.insertSelective(smtHtProcessCategory);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        ArrayList<SmtHtProcessCategory> list = new ArrayList<>();

        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] processCategoryIds = ids.split(",");
        for (String processCategoryId : processCategoryIds) {
            SmtProcessCategory smtProcessCategory = smtProcessCategoryMapper.selectByPrimaryKey(processCategoryId);
            if (StringUtils.isEmpty(smtProcessCategory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //新增工序历史信息
            SmtHtProcessCategory smtHtProcessCategory = new SmtHtProcessCategory();
            BeanUtils.copyProperties(smtProcessCategory,smtHtProcessCategory);
            smtHtProcessCategory.setModifiedUserId(currentUserInfo.getUserId());
            smtHtProcessCategory.setModifiedTime(new Date());
            list.add(smtHtProcessCategory);
        }
        smtHtProcessCategoryMapper.insertList(list);

        return smtProcessCategoryMapper.deleteByIds(ids);
    }

    @Override
    public List<SmtProcessCategoryDto> findList(Map<String, Object> map) {
        return smtProcessCategoryMapper.findList(map);
    }
}
