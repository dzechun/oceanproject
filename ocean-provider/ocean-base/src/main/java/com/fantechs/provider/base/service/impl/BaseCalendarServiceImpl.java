package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseCalendarDto;
import com.fantechs.common.base.general.dto.basic.BaseWorkShiftTimeDto;
import com.fantechs.common.base.general.entity.basic.BaseCalendar;
import com.fantechs.common.base.general.entity.basic.BaseCalendarWorkShift;
import com.fantechs.common.base.general.entity.basic.BaseWorkShift;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseCalendarMapper;
import com.fantechs.provider.base.service.BaseCalendarService;
import com.fantechs.provider.base.service.BaseCalendarWorkShiftService;
import com.fantechs.provider.base.service.BaseWorkShiftService;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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

    @Override
    public int save(BaseCalendar baseCalendar) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        //新增日历
        baseCalendar.setCreateTime(new Date());
        baseCalendar.setCreateUserId(user.getUserId());
        baseCalendar.setModifiedTime(new Date());
        baseCalendar.setModifiedUserId(user.getUserId());
        baseCalendar.setStatus(StringUtils.isEmpty(baseCalendar.getStatus()) ? 1 : baseCalendar.getStatus());
        return baseCalendarMapper.insertUseGeneratedKeys(baseCalendar);
    }

    @Override
    public int update(BaseCalendar baseCalendar) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

        baseCalendar.setModifiedUserId(user.getUserId());
        baseCalendar.setModifiedTime(new Date());

        return baseCalendarMapper.updateByPrimaryKeySelective(baseCalendar);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }

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
        return baseCalendarMapper.findList(map);
    }

    @Override
    @Test
    public void calendarPostpone() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);//当前年
        int month = calendar.get(Calendar.MONTH);//当前月
        int day = calendar.get(Calendar.DAY_OF_MONTH);//本月的天
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);//获取本月的最大天数
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        String date1 = simpleDateFormat.format(date);//获取年-月

        //判断是否为本月的最后一天
        if (lastDay == day) {
            //判断是否为今年的最后一个月
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

    //复制日历信息
    public void copyCalendar(String date, int year, int month) {
        //获取本月的日历信息
        HashMap<String, Object> map = new HashMap<>();
        map.put("date", date);
        List<BaseCalendarDto> baseCalendarDtos = findList(map);

        for (BaseCalendarDto baseCalendarDto : baseCalendarDtos) {
            //为每一条产线新增一份日历
            BaseCalendar baseCalendar = new BaseCalendar();
            baseCalendar.setProLineId(baseCalendarDto.getProLineId());
            baseCalendar.setDate("" + year + "-" + month);
            save(baseCalendar);

            //复制上一月的每日计划
            BaseCalendarWorkShift baseCalendarWorkShift = new BaseCalendarWorkShift();
            baseCalendarWorkShift.setWorkShiftId(baseCalendarDto.getWorkShiftId());
            baseCalendarWorkShift.setCalendarId(baseCalendar.getCalendarId());
            baseCalendarWorkShiftService.save(baseCalendarWorkShift);
        }
    }

    @Override
    public BaseCalendarDto findAllWorkShiftTime(Long proLineId, String date) {

        List<BaseWorkShiftTimeDto> workShiftTimeDtos = new ArrayList<>();//保存天以及当天对应的班次

        //根据产线id和日期获取日历
        Example example = new Example(BaseCalendar.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("proLineId",proLineId)
                .andEqualTo("date",date);
        BaseCalendar baseCalendar = baseCalendarMapper.selectOneByExample(example);
        BaseCalendarDto baseCalendarDto = new BaseCalendarDto();
        if (StringUtils.isNotEmpty(baseCalendar)){
            BeanUtils.copyProperties(baseCalendar,baseCalendarDto);
            //通过日历id获取日历班次关系表的信息
            Long calendarId = baseCalendar.getCalendarId();
            Example example1 = new Example(BaseCalendarWorkShift.class);
            //排序日历信息，让它按1-31的顺序输出，方便前端解析
            for (int i = 1; i <= 31; i++) {
                Example.Criteria criteria1 = example1.createCriteria();
                criteria1.andEqualTo("calendarId",calendarId)
                        .andEqualTo("day",i);
                List<BaseCalendarWorkShift> baseCalendarWorkShifts = baseCalendarWorkShiftService.selectByExample(example1);
                //将同一天的班次都放到一个对象中，对于同一天的日历，除了班次其他信息都是一样的，只需要一个对象去保存
                example1.clear();
                if (StringUtils.isNotEmpty(baseCalendarWorkShifts)){
                    BaseWorkShiftTimeDto baseWorkShiftTimeDto = new BaseWorkShiftTimeDto();

                    ArrayList<String> allWorkShift = new ArrayList<>();
                    //保存班次时间
                    for (BaseCalendarWorkShift baseCalendarWorkShift : baseCalendarWorkShifts) {
                        Example example2 = new Example(BaseWorkShift.class);
                        Example.Criteria criteria2 = example2.createCriteria();
                        criteria2.andEqualTo("workShiftId",baseCalendarWorkShift.getWorkShiftId());
                        BaseWorkShift baseWorkShift = baseWorkShiftServicel.selectByExample(example2).get(0);
                        //格式化时间再保存
                        String strDateFormat = "HH:mm:ss";
                        SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                        String startTime = sdf.format(baseWorkShift.getStartTime());
                        String endTime = sdf.format(baseWorkShift.getEndTime());
                        String workTime = startTime + "-" + endTime;
                        allWorkShift.add(workTime);
                    }

                    baseWorkShiftTimeDto.setDay((long) i);
                    baseWorkShiftTimeDto.setAllWorkShift(allWorkShift);
                    workShiftTimeDtos.add(baseWorkShiftTimeDto);
                }
            }
        }
        baseCalendarDto.setBaseWorkShiftTimeDtos(workShiftTimeDtos);

        return baseCalendarDto;
    }
}