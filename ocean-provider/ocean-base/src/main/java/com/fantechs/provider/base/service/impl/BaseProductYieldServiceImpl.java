package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseProductYieldDto;
import com.fantechs.common.base.general.entity.basic.BaseProductYield;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductYield;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProductYieldMapper;
import com.fantechs.provider.base.mapper.BaseProductYieldMapper;
import com.fantechs.provider.base.service.BaseProductYieldService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/20.
 */
@Service
public class BaseProductYieldServiceImpl extends BaseService<BaseProductYield> implements BaseProductYieldService {

    @Resource
    private BaseProductYieldMapper baseProductYieldMapper;
    @Resource
    private BaseHtProductYieldMapper baseHtProductYieldMapper;

    @Override
    public List<BaseProductYieldDto> findList(Map<String, Object> map) {
        return baseProductYieldMapper.findList(map);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(BaseProductYieldDto baseProductYieldDto) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        int i=0;

        Example example = new Example(BaseProductYield.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.orEqualTo("yieldType", 1);
        List<BaseProductYield> baseProductYields = baseProductYieldMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseProductYields)){
            throw new BizErrorException("通用数据最多只可配置一条");
        }
        example.clear();
        criteria = example.createCriteria();
        criteria.andEqualTo("orgId", currentUser.getOrganizationId());
        criteria.andEqualTo("materialId",baseProductYieldDto.getMaterialId());
        criteria.orEqualTo("proLineId",baseProductYieldDto.getProLineId());
        baseProductYields = baseProductYieldMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseProductYields)){
            throw new BizErrorException("产线与物料重复");
        }

        baseProductYieldDto.setCreateUserId(currentUser.getUserId());
        baseProductYieldDto.setCreateTime(new Date());
        baseProductYieldDto.setModifiedUserId(currentUser.getUserId());
        baseProductYieldDto.setModifiedTime(new Date());
        baseProductYieldDto.setOrgId(currentUser.getOrganizationId());
        baseProductYieldDto.setIsDelete((byte)1);
        i = baseProductYieldMapper.insertSelective(baseProductYieldDto);

        //新增生产线历史信息
        BaseHtProductYield baseHtProductYield =new BaseHtProductYield();
        BeanUtils.copyProperties(baseProductYieldDto, baseHtProductYield);
        baseHtProductYieldMapper.insertSelective(baseHtProductYield);
        return i;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(BaseProductYieldDto baseProductYieldDto) {
        SysUser currentUser =CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseProductYield.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.orEqualTo("yieldType", 1);
        List<BaseProductYield> baseProductYields = baseProductYieldMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseProductYields)&&!baseProductYields.get(0).getProductYieldId().equals(baseProductYieldDto.getProLineId())){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
        example.clear();
        criteria = example.createCriteria();
        criteria.andEqualTo("organizationId", currentUser.getOrganizationId());
        criteria.andEqualTo("materialId",baseProductYieldDto.getMaterialId());
        criteria.orEqualTo("priLineId",baseProductYieldDto.getProLineId());
        baseProductYields = baseProductYieldMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(baseProductYields)&&!baseProductYields.get(0).getProductYieldId().equals(baseProductYieldDto.getProLineId())){
            throw new BizErrorException("产线与物料重复");
        }

        baseProductYieldDto.setModifiedUserId(currentUser.getUserId());
        baseProductYieldDto.setModifiedTime(new Date());
        baseProductYieldDto.setOrgId(currentUser.getOrganizationId());
        int i= baseProductYieldMapper.updateByPrimaryKeySelective(baseProductYieldDto);

        //新增生产线历史信息
        BaseHtProductYield baseHtProductYield =new BaseHtProductYield();
        BeanUtils.copyProperties(baseProductYieldDto, baseHtProductYield);
        baseHtProductYieldMapper.insertSelective(baseHtProductYield);

        return i;
    }


}
