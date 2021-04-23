package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSamplePlanAqlDto;
import com.fantechs.common.base.general.dto.basic.BaseSamplePlanDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplePlan;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSamplePlan;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtSamplePlanMapper;
import com.fantechs.provider.base.mapper.BaseSamplePlanMapper;
import com.fantechs.provider.base.service.BaseSamplePlanAqlService;
import com.fantechs.provider.base.service.BaseSamplePlanService;
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
public class BaseSamplePlanServiceImpl extends BaseService<BaseSamplePlan> implements BaseSamplePlanService {

    @Resource
    private BaseSamplePlanMapper baseSamplePlanMapper;

    @Resource
    private BaseHtSamplePlanMapper baseHtSamplePlanMapper;

    @Resource
    private BaseSamplePlanAqlService baseSamplePlanAqlService;

    @Override
    public List<BaseSamplePlanDto> findList(Map<String, Object> map) {
        return baseSamplePlanMapper.findList(map);
    }

    @Override
    public int save(BaseSamplePlan record) {
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

        int i = baseSamplePlanMapper.insertUseGeneratedKeys(record);

        BaseHtSamplePlan baseHtSamplePlan = new BaseHtSamplePlan();
        BeanUtils.copyProperties(record, baseHtSamplePlan);
        baseHtSamplePlanMapper.insert(baseHtSamplePlan);

        return i;
    }

    @Override
    public int update(BaseSamplePlan entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        this.codeIfRepeat(entity);

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());
        BaseHtSamplePlan baseHtSamplePlan = new BaseHtSamplePlan();
        BeanUtils.copyProperties(entity, baseHtSamplePlan);
        baseHtSamplePlanMapper.insert(baseHtSamplePlan);

        return baseSamplePlanMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        List<BaseHtSamplePlan> list = new ArrayList<>();
        String[] idsArr  = ids.split(",");
        HashMap<String, Object> map = new HashMap<>();

        for (String id : idsArr) {
            BaseSamplePlan baseSamplePlan = baseSamplePlanMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseSamplePlan)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

            BaseHtSamplePlan baseHtSamplePlan = new BaseHtSamplePlan();
            BeanUtils.copyProperties(baseSamplePlan, baseHtSamplePlan);
            list.add(baseHtSamplePlan);
            map.put("samplePlanId",id);
            List<BaseSamplePlanAqlDto> samplingPlanAqlList = baseSamplePlanAqlService.findList(map);
            if (StringUtils.isNotEmpty(samplingPlanAqlList)){
                String aqlIds = "";
                for (BaseSamplePlanAqlDto baseSamplingPlanAqlDto : samplingPlanAqlList) {
                    aqlIds+=baseSamplingPlanAqlDto.getSamplePlanAqlId()+",";
                }
                baseSamplePlanAqlService.batchDelete(aqlIds.substring(0, aqlIds.length() - 1));
            }
        }

        baseHtSamplePlanMapper.insertList(list);
        return baseSamplePlanMapper.deleteByIds(ids);
    }

    private void codeIfRepeat(BaseSamplePlan entity){
        Example example = new Example(BaseSamplePlan.class);
        Example.Criteria criteria = example.createCriteria();
        //判断编码是否重复
        criteria.andEqualTo("samplePlanCode",entity.getSamplePlanCode());
        if (StringUtils.isNotEmpty(entity.getSamplePlanId())){
            criteria.andNotEqualTo("samplePlanId",entity.getSamplePlanId());
        }
        BaseSamplePlan baseSamplePlan = baseSamplePlanMapper.selectOneByExample(example);
        if (StringUtils.isNotEmpty(baseSamplePlan)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012001);
        }
    }

}
