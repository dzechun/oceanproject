package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseBadnessCategoryDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCategory;
import com.fantechs.common.base.general.entity.basic.BaseShipmentEnterprise;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtShipmentEnterprise;
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
        return baseBadnessCategoryMapper.findList(map);
    }

    @Override
    public int save(BaseBadnessCategory record) {
//        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(user)){
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }

        this.codeIfRepeat(record);

        record.setCreateTime(new Date());
//        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
//        record.setModifiedUserId(user.getUserId());
//        record.setOrgId(user.getOrganizationId());

        int i = baseBadnessCategoryMapper.insertUseGeneratedKeys(record);

        BaseHtBadnessCategory baseHtBadnessCategory = new BaseHtBadnessCategory();
        BeanUtils.copyProperties(record,baseHtBadnessCategory);
        baseHtBadnessCategoryMapper.insert(baseHtBadnessCategory);

        return i;
    }

    @Override
    public int update(BaseBadnessCategory entity) {
//        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(user)){
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }

        this.codeIfRepeat(entity);

//        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());

        BaseHtBadnessCategory baseHtBadnessCategory = new BaseHtBadnessCategory();
        BeanUtils.copyProperties(entity,baseHtBadnessCategory);
        baseHtBadnessCategoryMapper.insert(baseHtBadnessCategory);

        return baseBadnessCategoryMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int batchDelete(String ids) {
//        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
//        if(StringUtils.isEmpty(user)){
//            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
//        }

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
        Example example = new Example(BaseBadnessCategory.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("badnessCategoryCode",entity.getBadnessCategoryCode());
        BaseBadnessCategory baseBadnessCategory = baseBadnessCategoryMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseBadnessCategory)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }
}
