package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseCalendarDto;
import com.fantechs.common.base.general.dto.basic.BaseCalendarWorkShiftDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkShiftDto;
import com.fantechs.common.base.general.entity.basic.BaseCalendar;
import com.fantechs.common.base.general.entity.basic.BaseCalendarWorkShift;
import com.fantechs.common.base.general.entity.basic.BaseWorkShiftTime;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseCalendarWorkShift;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkShift;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkShiftTime;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseCalendarMapper;
import com.fantechs.provider.base.service.BaseCalendarService;
import com.fantechs.provider.base.service.BaseCalendarWorkShiftService;
import com.fantechs.provider.base.service.BaseWorkShiftService;
import com.fantechs.provider.base.service.BaseWorkShiftTimeService;
import org.junit.Test;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by leifengzhi on 2020/12/21.
 */
@Service
public class BaseCalendarServiceImpl extends BaseService<BaseCalendar> implements BaseCalendarService {

    @Resource
    private BaseCalendarMapper baseCalendarMapper;
    @Resource
    private BaseCalendarWorkShiftService baseCalendarWorkShiftService;
    @Resource
    private BaseWorkShiftService baseWorkShiftServicel;
    @Resource
    private BaseWorkShiftTimeService baseWorkShiftTimeService;

    @Override
    public int save(BaseCalendar baseCalendar) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        //????????????
        baseCalendar.setCreateTime(new Date());
        baseCalendar.setCreateUserId(user.getUserId());
        baseCalendar.setModifiedTime(new Date());
        baseCalendar.setModifiedUserId(user.getUserId());
        baseCalendar.setStatus(StringUtils.isEmpty(baseCalendar.getStatus()) ? 1 : baseCalendar.getStatus());
        baseCalendar.setOrganizationId(user.getOrganizationId());
        return baseCalendarMapper.insertUseGeneratedKeys(baseCalendar);
    }

    @Override
    public int update(BaseCalendar baseCalendar) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        baseCalendar.setModifiedUserId(user.getUserId());
        baseCalendar.setModifiedTime(new Date());
        baseCalendar.setOrganizationId(user.getOrganizationId());

        return baseCalendarMapper.updateByPrimaryKeySelective(baseCalendar);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        String[] idsArr = ids.split(",");
        for (String id : idsArr) {
            BaseCalendar baseCalendar = baseCalendarMapper.selectByPrimaryKey(id);
            if (StringUtils.isEmpty(baseCalendar)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }

        }
        return baseCalendarMapper.deleteByIds(ids);
    }

    @Override
    public List<BaseCalendarDto> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());

        //????????????
        List<BaseCalendarDto> baseCalendarDtos = baseCalendarMapper.findList(map);
        for (BaseCalendarDto baseCalendarDto : baseCalendarDtos) {
            if (StringUtils.isNotEmpty(baseCalendarDto)) {

                //??????????????????????????????
                SearchBaseCalendarWorkShift searchBaseCalendarWorkShift = new SearchBaseCalendarWorkShift();
                searchBaseCalendarWorkShift.setCalendarId(baseCalendarDto.getCalendarId());
                List<BaseCalendarWorkShiftDto> baseCalendarWorkShiftDtos = baseCalendarWorkShiftService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseCalendarWorkShift));


                if (StringUtils.isNotEmpty(baseCalendarWorkShiftDtos)) {
                    baseCalendarDto.setBaseCalendarWorkShiftDtos(baseCalendarWorkShiftDtos);
                    for (BaseCalendarWorkShiftDto baseCalendarWorkShiftDto : baseCalendarWorkShiftDtos) {

                        //??????????????????????????????????????????????????????
                        SearchBaseWorkShiftTime searchBaseWorkShiftTime = new SearchBaseWorkShiftTime();
                        searchBaseWorkShiftTime.setWorkShiftId(baseCalendarWorkShiftDto.getWorkShiftId());
                        List<BaseWorkShiftTime> baseWorkShiftTimes = baseWorkShiftTimeService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkShiftTime));
                        baseCalendarWorkShiftDto.setBaseWorkShiftTimes(baseWorkShiftTimes);

                        //??????????????????
                        SearchBaseWorkShift searchBaseWorkShift = new SearchBaseWorkShift();
                        searchBaseWorkShift.setWorkShiftId(baseCalendarWorkShiftDto.getWorkShiftId());
                        BaseWorkShiftDto baseWorkShiftDto = baseWorkShiftServicel.findList(ControllerUtil.dynamicConditionByEntity(searchBaseWorkShift)).get(0);
                        baseCalendarWorkShiftDto.setWorkShiftName(baseWorkShiftDto.getWorkShiftName());
                    }
                }
            }
        }
        return baseCalendarDtos;
    }

    @Override
    @Test
    public void calendarPostpone() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);//?????????
        int month = calendar.get(Calendar.MONTH);//?????????
        int day = calendar.get(Calendar.DAY_OF_MONTH);//????????????
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//???????????????????????????
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String date1 = simpleDateFormat.format(date);//?????????-???

        //????????????????????????????????????
        if (lastDay == day) {
            //???????????????????????????????????????
            if (month == 12) {
                year++;
                month = 1;
                copyCalendar(date1, year, month);
            } else {
                month++;
                copyCalendar(date1, year, month);
            }
        }
    }

    //??????????????????
    public void copyCalendar(String date, int year, int month) {
        //???????????????????????????
        HashMap<String, Object> map = new HashMap<>();
        map.put("date", date);
        List<BaseCalendarDto> baseCalendarDtos = findList(map);

        for (BaseCalendarDto baseCalendarDto : baseCalendarDtos) {
            //????????????????????????????????????
            BaseCalendar baseCalendar = new BaseCalendar();
            baseCalendar.setProLineId(baseCalendarDto.getProLineId());
            baseCalendar.setDate("" + year + "-" + month);
            save(baseCalendar);

            //??????????????????????????????
            BaseCalendarWorkShift baseCalendarWorkShift = new BaseCalendarWorkShift();
            baseCalendarWorkShift.setWorkShiftId(baseCalendarDto.getWorkShiftId());
            baseCalendarWorkShift.setCalendarId(baseCalendar.getCalendarId());
            baseCalendarWorkShiftService.save(baseCalendarWorkShift);
        }
    }
/*
    @Override
    public BaseCalendarDto findAllWorkShiftTime(Long proLineId, String date) {

        List<BaseWorkShiftTimeDto> workShiftTimeDtos = new ArrayList<>();//????????????????????????????????????

        //????????????id?????????????????????
        Example example = new Example(BaseCalendar.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("proLineId",proLineId)
                .andEqualTo("date",date);
        BaseCalendar baseCalendar = baseCalendarMapper.selectOneByExample(example);
        BaseCalendarDto baseCalendarDto = new BaseCalendarDto();
        if (StringUtils.isNotEmpty(baseCalendar)){
            BeanUtils.copyProperties(baseCalendar,baseCalendarDto);
            //????????????id????????????????????????????????????
            Long calendarId = baseCalendar.getCalendarId();
            Example example1 = new Example(BaseCalendarWorkShift.class);
            //??????????????????????????????1-31????????????????????????????????????
            for (int i = 1; i <= 31; i++) {
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("calendarId",calendarId)
                        .andEqualTo("day",i);
                List<BaseCalendarWorkShift> baseCalendarWorkShifts = baseCalendarWorkShiftService.selectByExample(example1);
                //???????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
                example1.clear();
                if (StringUtils.isNotEmpty(baseCalendarWorkShifts)){
                    BaseWorkShiftTimeDto baseWorkShiftTimeDto = new BaseWorkShiftTimeDto();

                    ArrayList<String> allWorkShift = new ArrayList<>();
                    //??????????????????
                    for (BaseCalendarWorkShift baseCalendarWorkShift : baseCalendarWorkShifts) {
                        Example example2 = new Example(BaseWorkShiftTime.class);
                        Example.Criteria criteria2 = example2.createCriteria();
                        criteria2.andEqualTo("workShiftId",baseCalendarWorkShift.getWorkShiftId());
                        //????????????????????????????????????
                        List<BaseWorkShiftTime> baseWorkShiftTimes = baseWorkShiftTimeService.selectByExample(example2);
                        for (BaseWorkShiftTime baseWorkShiftTime : baseWorkShiftTimes) {
                            //????????????????????????
                            String strDateFormat = "HH:mm:ss";
                            SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                            String startTime = sdf.format(baseWorkShiftTime.getStartTime());
                            String endTime = sdf.format(baseWorkShiftTime.getEndTime());
                            String workTime = startTime + "-" + endTime;
                            allWorkShift.add(workTime);
                        }
                    }

                    baseWorkShiftTimeDto.setDay((long) i);
                    baseWorkShiftTimeDto.setAllWorkShift(allWorkShift);
                    workShiftTimeDtos.add(baseWorkShiftTimeDto);
                }
            }
        }
        baseCalendarDto.setBaseWorkShiftTimeDtos(workShiftTimeDtos);

        return baseCalendarDto;
    }*/
}
