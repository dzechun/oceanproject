package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSampleStandardDto;
import com.fantechs.common.base.general.entity.basic.BaseSampleStandard;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSampleStandard;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtSampleStandardMapper;
import com.fantechs.provider.base.mapper.BaseSampleStandardMapper;
import com.fantechs.provider.base.service.BaseSampleStandardService;
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
 * Created by leifengzhi on 2021/04/06.
 */
@Service
public class BaseSampleStandardServiceImpl extends BaseService<BaseSampleStandard> implements BaseSampleStandardService {

    @Resource
    private BaseSampleStandardMapper baseSampleStandardMapper;
    @Resource
    private BaseHtSampleStandardMapper baseHtSampleStandardMapper;

    @Override
    public List<BaseSampleStandardDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseSampleStandardMapper.findList(map);
    }

    @Override
    public int save(BaseSampleStandard baseSampleStandard) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseSampleStandard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("sampleStandardName",baseSampleStandard.getSampleStandardName());
        BaseSampleStandard baseSampleStandard1 = baseSampleStandardMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseSampleStandard1)){
            throw new BizErrorException("抽样标准名称已存在");
        }

        baseSampleStandard.setCreateTime(new Date());
        baseSampleStandard.setCreateUserId(user.getUserId());
        baseSampleStandard.setModifiedTime(new Date());
        baseSampleStandard.setModifiedUserId(user.getUserId());
        baseSampleStandard.setStatus((byte) 1);
        baseSampleStandard.setOrgId(user.getOrganizationId());
        baseSampleStandardMapper.insertUseGeneratedKeys(baseSampleStandard);

        BaseHtSampleStandard baseHtSampleStandard = new BaseHtSampleStandard();
        BeanUtils.copyProperties(baseSampleStandard,baseHtSampleStandard);
        return baseHtSampleStandardMapper.insertSelective(baseHtSampleStandard);
    }

    @Override
    public int update(BaseSampleStandard baseSampleStandard) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseSampleStandard.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("sampleStandardName",baseSampleStandard.getSampleStandardName())
                .andNotEqualTo("sampleStandardId",baseSampleStandard.getSampleStandardId());
        BaseSampleStandard baseSampleStandard1 = baseSampleStandardMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseSampleStandard1)){
            throw new BizErrorException("抽样标准名称已存在");
        }

        baseSampleStandard.setModifiedTime(new Date());
        baseSampleStandard.setModifiedUserId(user.getUserId());
        baseSampleStandard.setOrgId(user.getOrganizationId());
        baseSampleStandardMapper.updateByPrimaryKeySelective(baseSampleStandard);

        BaseHtSampleStandard baseHtSampleStandard = new BaseHtSampleStandard();
        BeanUtils.copyProperties(baseSampleStandard,baseHtSampleStandard);
        return baseHtSampleStandardMapper.insertSelective(baseHtSampleStandard);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        ArrayList<BaseHtSampleStandard> baseHtSampleStandards = new ArrayList<>();
        String[] idsArray = ids.split(",");
        for (String id : idsArray) {
            BaseSampleStandard baseSampleStandard = baseSampleStandardMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseSampleStandard)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtSampleStandard baseHtSampleStandard = new BaseHtSampleStandard();
            BeanUtils.copyProperties(baseSampleStandard,baseHtSampleStandard);
            baseHtSampleStandard.setModifiedTime(new Date());
            baseHtSampleStandard.setModifiedUserId(user.getUserId());
            baseHtSampleStandards.add(baseHtSampleStandard);
        }
        baseHtSampleStandardMapper.insertList(baseHtSampleStandards);
        return baseSampleStandardMapper.deleteByIds(ids);
    }
}
