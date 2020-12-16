package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.basic.BaseProductFamilyDto;
import com.fantechs.common.base.general.entity.basic.BaseProductFamily;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductFamily;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProductFamilyMapper;
import com.fantechs.provider.base.mapper.BaseProductFamilyMapper;
import com.fantechs.provider.base.service.BaseProductFamilyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/12/15.
 */
@Service
public class BaseProductFamilyServiceImpl extends BaseService<BaseProductFamily> implements BaseProductFamilyService {

    @Resource
    private BaseProductFamilyMapper baseProductFamilyMapper;
    @Resource
    private BaseHtProductFamilyMapper baseHtProductFamilyMapper;

    @Override
    public int save(BaseProductFamily baseProductFamily) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseProductFamily.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("productFamilyCode",baseProductFamily.getProductFamilyCode());
        BaseProductFamily baseProductFamily1 = baseProductFamilyMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseProductFamily1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        Example example1 = new Example(BaseProductFamily.class);
        Example.Criteria criteria2 = example1.createCriteria();
        //判断名称是否重复
        criteria2.andEqualTo("productFamilyName",baseProductFamily.getProductFamilyName());
        BaseProductFamily baseProductFamily2 = baseProductFamilyMapper.selectOneByExample(example1);
        if (StringUtils.isNotEmpty(baseProductFamily2)){
            throw new BizErrorException("产品族名称已存在");
        }

        baseProductFamily.setCreateTime(new Date());
        baseProductFamily.setCreateUserId(user.getUserId());
        baseProductFamily.setModifiedTime(new Date());
        baseProductFamily.setModifiedUserId(user.getUserId());
        baseProductFamily.setStatus(StringUtils.isEmpty(baseProductFamily.getStatus())?1:baseProductFamily.getStatus());
        int i = baseProductFamilyMapper.insertUseGeneratedKeys(baseProductFamily);

        BaseHtProductFamily baseHtProductFamily = new BaseHtProductFamily();
        BeanUtils.copyProperties(baseProductFamily,baseHtProductFamily);
        baseHtProductFamilyMapper.insert(baseHtProductFamily);

        return i;
    }

    @Override
    public int update(BaseProductFamily baseProductFamily) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseProductFamily.class);
        Example.Criteria criteria1 = example.createCriteria();
        //判断编码是否重复
        criteria1.andEqualTo("productFamilyCode",baseProductFamily.getProductFamilyCode())
                .andNotEqualTo("productFamilyId",baseProductFamily.getProductFamilyId());
        BaseProductFamily baseProductFamily1 = baseProductFamilyMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseProductFamily1)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }

        Example example1 = new Example(BaseProductFamily.class);
        Example.Criteria criteria2 = example1.createCriteria();
        //判断名称是否重复
        criteria2.andEqualTo("productFamilyName",baseProductFamily.getProductFamilyName())
                .andNotEqualTo("productFamilyId",baseProductFamily.getProductFamilyId());
        BaseProductFamily baseProductFamily2 = baseProductFamilyMapper.selectOneByExample(example1);
        if (StringUtils.isNotEmpty(baseProductFamily2)){
            throw new BizErrorException("产品族名称已经存在");
        }

        baseProductFamily.setModifiedUserId(user.getUserId());
        baseProductFamily.setModifiedTime(new Date());

        BaseHtProductFamily baseHtProductFamily = new BaseHtProductFamily();
        BeanUtils.copyProperties(baseProductFamily,baseHtProductFamily);
        baseHtProductFamilyMapper.insert(baseHtProductFamily);

        return baseProductFamilyMapper.updateByPrimaryKeySelective(baseProductFamily);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtProductFamily> baseHtProductFamilies = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseProductFamily baseProductFamily = baseProductFamilyMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseProductFamily)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtProductFamily baseHtProductFamily = new BaseHtProductFamily();
            BeanUtils.copyProperties(baseProductFamily,baseHtProductFamily);
            baseHtProductFamilies.add(baseHtProductFamily);
        }

        baseHtProductFamilyMapper.insertList(baseHtProductFamilies);
        return baseProductFamilyMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseProductFamilyDto> findList(Map<String, Object> map) {
        return baseProductFamilyMapper.findList(map);
    }
}
