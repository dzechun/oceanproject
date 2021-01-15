package com.fantechs.provider.bcm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.bcm.BcmLabelCategoryDto;
import com.fantechs.common.base.general.entity.bcm.BcmLabelCategory;
import com.fantechs.common.base.general.entity.bcm.history.BcmHtLabelCategory;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabelCategory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.bcm.mapper.BcmHtLabelCategoryMapper;
import com.fantechs.provider.bcm.mapper.BcmLabelCategoryMapper;
import com.fantechs.provider.bcm.service.BcmLabelCategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@Service
public class BcmLabelCategoryServiceImpl  extends BaseService<BcmLabelCategory> implements BcmLabelCategoryService {

         @Resource
         private BcmLabelCategoryMapper bcmLabelCategoryMapper;
         @Resource
         private BcmHtLabelCategoryMapper bcmHtLabelCategoryMapper;

    @Override
    public List<BcmLabelCategoryDto> findList(SearchBcmLabelCategory searchBcmLabelCategory) {
        return bcmLabelCategoryMapper.findList(searchBcmLabelCategory);
    }

    @Override
    public int save(BcmLabelCategory record) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(BcmLabelCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("labelCategoryCode",record.getLabelCategoryCode());
//        example.or();
//        criteria.andEqualTo("labelCategoryName",record.getLabelCategoryName());
        BcmLabelCategory bcmLabelCategory = bcmLabelCategoryMapper.selectOneByExample(example);
        if(!StringUtils.isEmpty(bcmLabelCategory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        record.setCreateTime(new Date());
        record.setCreateUserId(currentUserInfo.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(currentUserInfo.getUserId());

        return bcmLabelCategoryMapper.insertUseGeneratedKeys(record);
    }

    @Override
    public int update(BcmLabelCategory entity) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BcmLabelCategory.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("labelCategoryCode",entity.getLabelCategoryCode());
        criteria.andNotEqualTo("labelCategoryId",entity.getLabelCategoryId());
        BcmLabelCategory bcmLabelCategory = bcmLabelCategoryMapper.selectOneByExample(example);

        entity.setModifiedUserId(currentUserInfo.getUserId());
        entity.setModifiedTime(new Date());
        int num = bcmLabelCategoryMapper.updateByPrimaryKeySelective(entity);

        //新增历史记录
        BcmHtLabelCategory bcmHtLabelCategory = new BcmHtLabelCategory();
        BeanUtils.copyProperties(entity,bcmHtLabelCategory);
        bcmHtLabelCategoryMapper.updateByPrimaryKeySelective(bcmHtLabelCategory);

        return num;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser currentUserInfo = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUserInfo)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BcmHtLabelCategory> list = new ArrayList<>();

        String[] idArry = ids.split(",");
        for (String id : idArry) {
            BcmLabelCategory bcmLabelCategory = bcmLabelCategoryMapper.selectByPrimaryKey(id);
            if(StringUtils.isEmpty(bcmLabelCategory)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            BcmHtLabelCategory bcmHtLabelCategory = new BcmHtLabelCategory();
            BeanUtils.copyProperties(bcmLabelCategory,bcmHtLabelCategory);
            list.add(bcmHtLabelCategory);
        }
        bcmHtLabelCategoryMapper.insertList(list);

        return bcmLabelCategoryMapper.deleteByIds(ids);
    }
}
