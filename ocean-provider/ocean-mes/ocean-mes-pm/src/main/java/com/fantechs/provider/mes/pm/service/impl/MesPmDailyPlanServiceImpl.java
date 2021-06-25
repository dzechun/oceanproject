package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanDto;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessReM;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlan;
import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlan;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
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
import java.util.*;

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
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        searchMesPmDailyPlan.setOrgId(user.getOrganizationId());
        //如未填写计划日期则取当天时间

        Calendar calendar = Calendar.getInstance();
        if(StringUtils.isEmpty(searchMesPmDailyPlan.getPlanTime())) {
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, +1);
            searchMesPmDailyPlan.setPlanTime(DateUtils.getDateString(calendar.getTime()));
        }
        searchMesPmDailyPlan.setStartTime(searchMesPmDailyPlan.getPlanTime());
        Date strToDate = DateUtils.getStrToDate(searchMesPmDailyPlan.getPlanTime());
        calendar.setTime(strToDate);
        calendar.add(Calendar.DATE, +2);
        String endTime = DateUtils.getDateString(calendar.getTime());
        searchMesPmDailyPlan.setEndTime(endTime);

        return mesPmDailyPlanMapper.findList(searchMesPmDailyPlan);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int batchSave(List<MesPmDailyPlan> list) {
       /* SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }*/
        if (StringUtils.isNotEmpty(list)){
            Example example = new Example(MesPmDailyPlan.class);
            for (MesPmDailyPlan mesPmDailyPlan : list) {
                example.createCriteria().andEqualTo("workOrderId",mesPmDailyPlan.getWorkOrderId() == null ? -1 : mesPmDailyPlan.getWorkOrderId());
                MesPmDailyPlan mesPmDailyPlan1 = mesPmDailyPlanMapper.selectOneByExample(example);
                if(StringUtils.isNotEmpty(mesPmDailyPlan1)) {
                    //TODO添加到履历表
                    mesPmDailyPlanMapper.deleteByExample(example);
                }
                if(StringUtils.isNotEmpty(mesPmDailyPlan.getPlanDate())) {
                    try {
                        mesPmDailyPlan.setPlanTime(DateUtils.getStrToDate(mesPmDailyPlan.getPlanDate()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                this.save(mesPmDailyPlan);
                example.clear();
                }
            }
        return 1;
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
