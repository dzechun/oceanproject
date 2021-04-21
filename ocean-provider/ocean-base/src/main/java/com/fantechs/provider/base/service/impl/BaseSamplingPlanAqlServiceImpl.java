package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseSamplingPlanAqlDto;
import com.fantechs.common.base.general.entity.basic.BaseSamplingPlan;
import com.fantechs.common.base.general.entity.basic.BaseSamplingPlanAcRe;
import com.fantechs.common.base.general.entity.basic.BaseSamplingPlanAql;
import com.fantechs.common.base.general.entity.basic.history.BaseHtSamplingPlan;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseSamplingPlanAcReMapper;
import com.fantechs.provider.base.mapper.BaseSamplingPlanAqlMapper;
import com.fantechs.provider.base.service.BaseSamplingPlanAqlService;
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
 * Created by leifengzhi on 2021/04/21.
 */
@Service
public class BaseSamplingPlanAqlServiceImpl extends BaseService<BaseSamplingPlanAql> implements BaseSamplingPlanAqlService {

    @Resource
    private BaseSamplingPlanAqlMapper baseSamplingPlanAqlMapper;

    @Resource
    private BaseSamplingPlanAcReMapper baseSamplingPlanAcReMapper;

    @Override
    public List<BaseSamplingPlanAqlDto> findList(Map<String, Object> map) {
        return baseSamplingPlanAqlMapper.findList(map);
    }

    @Override
    public int save(BaseSamplingPlanAql record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        record.setOrgId(user.getOrganizationId());

        int i = baseSamplingPlanAqlMapper.insertUseGeneratedKeys(record);

        if (StringUtils.isNotEmpty(record.getList())){
            List<BaseSamplingPlanAcRe> list = record.getList();
            for (BaseSamplingPlanAcRe baseSamplingPlanAcRe : list) {
                baseSamplingPlanAcRe.setSamplingPlanAqlId(record.getSamplingPlanAqlId());
            }
            baseSamplingPlanAcReMapper.insertList(list);
        }

        return i;
    }

    @Override
    public int update(BaseSamplingPlanAql entity) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        entity.setModifiedUserId(user.getUserId());
        entity.setModifiedTime(new Date());
        entity.setOrgId(user.getOrganizationId());

        Example example = new Example(BaseSamplingPlanAcRe.class);
        example.createCriteria().andEqualTo("samplingPlanAqlId",entity.getSamplingPlanAqlId());
        baseSamplingPlanAcReMapper.deleteByExample(example);

        if (StringUtils.isNotEmpty(entity.getList())){
            baseSamplingPlanAcReMapper.insertList(entity.getList());
        }


        return baseSamplingPlanAqlMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        Example example = new Example(BaseSamplingPlanAcRe.class);

        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseSamplingPlanAql baseSamplingPlanAql = baseSamplingPlanAqlMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseSamplingPlanAql)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            example.createCriteria().andEqualTo("samplingPlanAqlId",id);
            baseSamplingPlanAcReMapper.deleteByExample(example);
        }

        return baseSamplingPlanAqlMapper.deleteByIds(ids);
    }
}
