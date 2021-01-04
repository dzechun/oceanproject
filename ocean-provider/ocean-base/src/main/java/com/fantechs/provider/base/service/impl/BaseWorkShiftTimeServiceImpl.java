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
        return baseWorkShiftTimeMapper.findList(map);
    }

    @Override
    public int save(BaseWorkShiftTime baseWorkShiftTime) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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

        return super.save(baseWorkShiftTime);
    }

    @Override
    public int update(BaseWorkShiftTime baseWorkShiftTime) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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

        return super.update(baseWorkShiftTime);
    }
}
