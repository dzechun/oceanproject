package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseCalendar;
import com.fantechs.common.base.general.entity.basic.BaseCalendarWorkShift;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseCalendarWorkShiftMapper;
import com.fantechs.provider.base.service.BaseCalendarService;
import com.fantechs.provider.base.service.BaseCalendarWorkShiftService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by leifengzhi on 2020/12/21.
 */
@Service
public class BaseCalendarWorkShiftServiceImpl extends BaseService<BaseCalendarWorkShift> implements BaseCalendarWorkShiftService {

    @Resource
    private BaseCalendarWorkShiftMapper baseCalendarWorkShiftMapper;
    @Resource
    private BaseCalendarService baseCalendarService;

    @Override
    public int save(BaseCalendarWorkShift baseCalendarWorkShift) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //日历为空，先添加一个日历
        if (StringUtils.isEmpty(baseCalendarWorkShift.getCalendarId())){
            BaseCalendar baseCalendar = new BaseCalendar();
            baseCalendar.setDate(baseCalendarWorkShift.getDate());
            baseCalendar.setProLineId(baseCalendarWorkShift.getProLineId());
            baseCalendarService.save(baseCalendar);
            baseCalendarWorkShift.setCalendarId(baseCalendar.getCalendarId());
        }

        //新增日历和班次关系
        baseCalendarWorkShift.setCreateTime(new Date());
        baseCalendarWorkShift.setCreateUserId(user.getUserId());
        baseCalendarWorkShift.setModifiedTime(new Date());
        baseCalendarWorkShift.setModifiedUserId(user.getUserId());
        baseCalendarWorkShift.setStatus(StringUtils.isEmpty(baseCalendarWorkShift.getStatus())?1:baseCalendarWorkShift.getStatus());
        return baseCalendarWorkShiftMapper.insertUseGeneratedKeys(baseCalendarWorkShift);
    }

    @Override
    public int update(BaseCalendarWorkShift baseCalendarWorkShift) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        baseCalendarWorkShift.setModifiedUserId(user.getUserId());
        baseCalendarWorkShift.setModifiedTime(new Date());

        return baseCalendarWorkShiftMapper.updateByPrimaryKeySelective(baseCalendarWorkShift);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        String[] idsArr  = ids.split(",");
        for (String id : idsArr) {
            BaseCalendarWorkShift baseCalendarWorkShift = baseCalendarWorkShiftMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseCalendarWorkShift)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

        }
        return baseCalendarWorkShiftMapper.deleteByIds(ids);
    }

    @Override
    public int batchSave(List<BaseCalendarWorkShift> baseCalendarWorkShifts) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //日历为空，先添加一个日历
        BaseCalendarWorkShift baseCalendarWorkShift = baseCalendarWorkShifts.get(0);
        if (StringUtils.isEmpty(baseCalendarWorkShift.getCalendarId())){
            BaseCalendar baseCalendar = new BaseCalendar();
            baseCalendar.setDate(baseCalendarWorkShift.getDate());
            baseCalendar.setProLineId(baseCalendarWorkShift.getProLineId());
            baseCalendarService.save(baseCalendar);
            baseCalendarWorkShift.setCalendarId(baseCalendar.getCalendarId());
        }


        for (BaseCalendarWorkShift calendarWorkShift : baseCalendarWorkShifts) {
            //判断该产线当日班次是否重复绑定
            Example example = new Example(BaseCalendarWorkShift.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("workShiftId",calendarWorkShift.getWorkShiftId())
                    .andEqualTo("calendarId",calendarWorkShift.getCalendarId())
                    .andEqualTo("day",calendarWorkShift.getDay());
            BaseCalendarWorkShift baseCalendarWorkShift1 = baseCalendarWorkShiftMapper.selectOneByExample(example);
            if (StringUtils.isNotEmpty(baseCalendarWorkShift1)){
                throw new BizErrorException("当日已存在该班次");
            }
            //新增日历和班次关系
            calendarWorkShift.setCreateTime(new Date());
            calendarWorkShift.setCreateUserId(user.getUserId());
            calendarWorkShift.setModifiedTime(new Date());
            calendarWorkShift.setModifiedUserId(user.getUserId());
            calendarWorkShift.setStatus(StringUtils.isEmpty(baseCalendarWorkShift.getStatus())?1:baseCalendarWorkShift.getStatus());
        }
        int i = baseCalendarWorkShiftMapper.insertList(baseCalendarWorkShifts);
        return i;
    }

    @Override
    public int deleteByCalendarIdAndDay(Integer calendarId, Byte day) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        Example example = new Example(BaseCalendarWorkShift.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("calendarId",calendarId)
                .andEqualTo("day",day);
        List<BaseCalendarWorkShift> baseCalendarWorkShifts = baseCalendarWorkShiftMapper.selectByExample(example);
        if (StringUtils.isEmpty(baseCalendarWorkShifts)){
            throw new BizErrorException("当日无可删除班次");
        }
        return baseCalendarWorkShiftMapper.deleteByExample(example);
    }
}
