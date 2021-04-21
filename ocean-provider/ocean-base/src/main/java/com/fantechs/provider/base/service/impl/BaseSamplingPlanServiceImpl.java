package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSamplingPlanAqlDto;
import com.fantechs.common.base.general.dto.basic.BaseSamplingPlanDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessCategory;
import com.fantechs.common.base.general.entity.basic.BaseSamplingPlan;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCategory;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSamplingPlan;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtSamplingPlanMapper;
import com.fantechs.provider.base.mapper.BaseSamplingPlanAqlMapper;
import com.fantechs.provider.base.mapper.BaseSamplingPlanMapper;
import com.fantechs.provider.base.service.BaseHtSamplingPlanService;
import com.fantechs.provider.base.service.BaseSamplingPlanAqlService;
import com.fantechs.provider.base.service.BaseSamplingPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/04/21.
 */
@Service
public class BaseSamplingPlanServiceImpl extends BaseService<BaseSamplingPlan> implements BaseSamplingPlanService {

    @Resource
    private BaseSamplingPlanMapper baseSamplingPlanMapper;

    @Resource
    private BaseHtSamplingPlanMapper baseHtSamplingPlanMapper;

    @Resource
    private BaseSamplingPlanAqlService baseSamplingPlanAqlService;

    @Override
    public List<BaseSamplingPlanDto> findList(Map<String, Object> map) {
        return baseSamplingPlanMapper.findList(map);
    }

    @Override
    public int save(BaseSamplingPlan record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(record);

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());

        int i = baseSamplingPlanMapper.insertUseGeneratedKeys(record);

        BaseHtSamplingPlan baseHtSamplingPlan = new BaseHtSamplingPlan();
        BeanUtils.copyProperties(record,baseHtSamplingPlan);
        baseHtSamplingPlanMapper.insert(baseHtSamplingPlan);

        return i;
    }

    @Override
    public int update(BaseSamplingPlan entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());
        BaseHtSamplingPlan baseHtSamplingPlan = new BaseHtSamplingPlan();
        BeanUtils.copyProperties(entity,baseHtSamplingPlan);
        baseHtSamplingPlanMapper.insert(baseHtSamplingPlan);

        return baseSamplingPlanMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtSamplingPlan> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        HashMap<String, Object> map = new HashMap<>();

        for (String id : idsArr) {
            BaseSamplingPlan baseSamplingPlan = baseSamplingPlanMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseSamplingPlan)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtSamplingPlan baseHtSamplingPlan = new BaseHtSamplingPlan();
            BeanUtils.copyProperties(baseSamplingPlan,baseHtSamplingPlan);
            list.add(baseHtSamplingPlan);
            map.put("samplingPlanId",id);
            List<BaseSamplingPlanAqlDto> samplingPlanAqlList = baseSamplingPlanAqlService.findList(map);
            if (StringUtils.isNotEmpty(samplingPlanAqlList)){
                String aqlIds = "";
                for (BaseSamplingPlanAqlDto baseSamplingPlanAqlDto : samplingPlanAqlList) {
                    aqlIds+=baseSamplingPlanAqlDto.getSamplingPlanAqlId()+",";
                }
                baseSamplingPlanAqlService.batchDelete(aqlIds);
            }
        }

        baseHtSamplingPlanMapper.insertList(list);
        return baseSamplingPlanMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(BaseSamplingPlan entity){
        Example example = new Example(BaseSamplingPlan.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("samplingPlanCode",entity.getSamplingPlanCode());
        if (StringUtils.isNotEmpty(entity.getSamplingPlanId())){
            criteria.andNotEqualTo("samplingPlanId",entity.getSamplingPlanId());
        }
        BaseSamplingPlan baseSamplingPlan = baseSamplingPlanMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseSamplingPlan)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

}
