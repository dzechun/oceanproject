package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlan;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlan;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.MesPmDailyPlanMapper;
import com.fantechs.provider.mes.pm.service.MesPmDailyPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/06/02.
 */
@Service
public class MesPmDailyPlanServiceImpl extends BaseService<MesPmDailyPlan> implements MesPmDailyPlanService {

    @Resource
    private MesPmDailyPlanMapper mesPmDailyPlanMapper;

    @Override
    public List<MesPmDailyPlanDto> findList(SearchMesPmDailyPlan searchMesPmDailyPlan) {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        searchMesPmDailyPlan.setOrgId(user.getOrganizationId());
        if(StringUtils.isNotEmpty(searchMesPmDailyPlan.getPlanTime())) {
            searchMesPmDailyPlan.setStartTime(searchMesPmDailyPlan.getPlanTime());
            searchMesPmDailyPlan.setEndTime(searchMesPmDailyPlan.getPlanTime());
        }
        return mesPmDailyPlanMapper.findList(searchMesPmDailyPlan);
    }

    @Override
    public List<MesPmDailyPlanDto> findDaysList(SearchMesPmDailyPlan searchMesPmDailyPlan) throws ParseException {

        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        searchMesPmDailyPlan.setOrgId(user.getOrganizationId());
        //如未填写计划日期则取当天时间

        Calendar calendar = Calendar.getInstance();
        if(StringUtils.isEmpty(searchMesPmDailyPlan.getPlanTime())) {
            calendar.setTime(new Date());
         //   calendar.add(Calendar.DATE, +1);
            searchMesPmDailyPlan.setPlanTime(DateUtils.getDateString(calendar.getTime()));
        }
        searchMesPmDailyPlan.setStartTime(searchMesPmDailyPlan.getPlanTime());
        Date strToDate = DateUtils.getStrToDate(searchMesPmDailyPlan.getPlanTime());
        calendar.setTime(strToDate);
        calendar.add(Calendar.DATE, +2);
        String endTime = DateUtils.getDateString(calendar.getTime());
        searchMesPmDailyPlan.setEndTime(endTime);
        searchMesPmDailyPlan.setStatus((byte)1);
        return mesPmDailyPlanMapper.findList(searchMesPmDailyPlan);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchSave(List<MesPmDailyPlan> list) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        int i = 1;
        if (StringUtils.isNotEmpty(list)){
            Example example = new Example(MesPmDailyPlan.class);
            List<MesPmDailyPlan> addList = new ArrayList<>();
            for (MesPmDailyPlan mesPmDailyPlan : list) {
                example.createCriteria().andEqualTo("workOrderId",mesPmDailyPlan.getWorkOrderId() == null ? -1 : mesPmDailyPlan.getWorkOrderId());
                MesPmDailyPlan mesPmDailyPlan1 = mesPmDailyPlanMapper.selectOneByExample(example);
                Date strToDate = null;
                try {
                    strToDate = DateUtils.getStrToDate(mesPmDailyPlan.getPlanDate());
                    mesPmDailyPlan.setPlanTime(strToDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //判断当日是否有工单，有则作为插单
                Example example1 = new Example(MesPmDailyPlan.class);
                example1.createCriteria().andEqualTo("planTime",mesPmDailyPlan.getPlanDate());
                List<MesPmDailyPlan> mesPmDailyPlan2 = mesPmDailyPlanMapper.selectByExample(example1);
                if(StringUtils.isNotEmpty(mesPmDailyPlan1)) {
                    mesPmDailyPlan.setDailyPlanId(mesPmDailyPlan1.getDailyPlanId());
                    if(StringUtils.isNotEmpty(mesPmDailyPlan2))
                        mesPmDailyPlan.setIfOrderInserting((byte)1);
                    else
                        mesPmDailyPlan.setIfOrderInserting((byte)0);
                    mesPmDailyPlan.setStatus((byte)1);
                    mesPmDailyPlanMapper.updateByPrimaryKeySelective(mesPmDailyPlan);
                    //TODO添加到履历表
                }else{
                    if(StringUtils.isNotEmpty(mesPmDailyPlan2))
                        mesPmDailyPlan.setIfOrderInserting((byte)1);
                    else
                        mesPmDailyPlan.setIfOrderInserting((byte)0);
                    mesPmDailyPlan.setStatus((byte)1);
                    mesPmDailyPlan.setCreateUserId(user.getUserId());
                    mesPmDailyPlan.setModifiedUserId(user.getUserId());
                    addList.add(mesPmDailyPlan);
                }
                example.clear();
                }
            if(StringUtils.isNotEmpty(addList)) {
                i = mesPmDailyPlanMapper.insertList(addList);
            }
        }
        return i;
    }

    @Override
    public int batchRemove(List<MesPmDailyPlan> list) {
        if(StringUtils.isEmpty(list))  throw new BizErrorException("请求参数为空");
        for(MesPmDailyPlan mesPmDailyPlan : list){
            if(StringUtils.isEmpty(mesPmDailyPlan.getDailyPlanId()))  throw new BizErrorException("请求id为空");
            mesPmDailyPlan.setStatus((byte)0);
            this.update(mesPmDailyPlan);
        }
        return 1;
    }
}
