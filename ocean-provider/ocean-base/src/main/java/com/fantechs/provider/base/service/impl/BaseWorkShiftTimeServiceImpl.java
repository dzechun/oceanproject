package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseWorkShiftTime;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseWorkShiftTimeMapper;
import com.fantechs.provider.base.service.BaseWorkShiftTimeService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/04.
 */
@Service
public class BaseWorkShiftTimeServiceImpl extends BaseService<BaseWorkShiftTime> implements BaseWorkShiftTimeService {

    @Resource
    private BaseWorkShiftTimeMapper baseWorkShiftTimeMapper;

    @Override
    public List<BaseWorkShiftTime> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        return baseWorkShiftTimeMapper.findList(map);
    }

    @Override
    public int save(BaseWorkShiftTime baseWorkShiftTime) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseWorkShiftTime.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workShiftId",baseWorkShiftTime.getWorkShiftId())
                .andLessThanOrEqualTo("startTime",baseWorkShiftTime.getStartTime())
                .andGreaterThanOrEqualTo("endTime",baseWorkShiftTime.getStartTime());
        List<BaseWorkShiftTime> baseWorkShiftTimes = baseWorkShiftTimeMapper.selectByExample(example);

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("workShiftId",baseWorkShiftTime.getWorkShiftId())
                .andGreaterThan("startTime",baseWorkShiftTime.getStartTime())
                .andLessThan("endTime",baseWorkShiftTime.getEndTime());
        List<BaseWorkShiftTime> baseWorkShiftTimes2 = baseWorkShiftTimeMapper.selectByExample(example);
        if (StringUtils.isNotEmpty(baseWorkShiftTimes2) || StringUtils.isNotEmpty(baseWorkShiftTimes)){
            throw new BizErrorException("添加的时间段冲突");
        }

        baseWorkShiftTime.setCreateTime(new Date());
        baseWorkShiftTime.setCreateUserId(user.getUserId());
        baseWorkShiftTime.setModifiedTime(new Date());
        baseWorkShiftTime.setModifiedUserId(user.getUserId());
        baseWorkShiftTime.setStatus(StringUtils.isEmpty(baseWorkShiftTime.getStatus())?1:baseWorkShiftTime.getStatus());
        baseWorkShiftTime.setOrganizationId(user.getOrganizationId());

        return super.save(baseWorkShiftTime);
    }

    @Override
    public int update(BaseWorkShiftTime baseWorkShiftTime) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        Example example = new Example(BaseWorkShiftTime.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andNotEqualTo("workShiftTimeId",baseWorkShiftTime.getWorkShiftTimeId())
                .andEqualTo("workShiftId",baseWorkShiftTime.getWorkShiftId())
                .andLessThanOrEqualTo("startTime",baseWorkShiftTime.getStartTime())
                .andGreaterThanOrEqualTo("endTime",baseWorkShiftTime.getStartTime());
        List<BaseWorkShiftTime> baseWorkShiftTimes = baseWorkShiftTimeMapper.selectByExample(example);

        example.clear();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("workShiftId",baseWorkShiftTime.getWorkShiftId())
                .andGreaterThan("startTime",baseWorkShiftTime.getStartTime())
                .andLessThan("endTime",baseWorkShiftTime.getEndTime());
        List<BaseWorkShiftTime> baseWorkShiftTimes2 = baseWorkShiftTimeMapper.selectByExample(example);
        if ( StringUtils.isNotEmpty(baseWorkShiftTimes) || StringUtils.isNotEmpty(baseWorkShiftTimes2)){
            throw new BizErrorException("添加的时间段冲突");
        }

        baseWorkShiftTime.setModifiedTime(new Date());
        baseWorkShiftTime.setModifiedUserId(user.getUserId());
        baseWorkShiftTime.setOrganizationId(user.getOrganizationId());

        return super.update(baseWorkShiftTime);
    }
}
