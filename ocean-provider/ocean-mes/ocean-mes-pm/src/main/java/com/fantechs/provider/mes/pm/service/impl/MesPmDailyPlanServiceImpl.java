package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmDailyPlanDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlan;
import com.fantechs.common.base.general.entity.mes.pm.MesPmDailyPlanDet;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmDailyPlan;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.MesPmDailyPlanDetMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmDailyPlanMapper;
import com.fantechs.provider.mes.pm.mapper.MesPmWorkOrderMapper;
import com.fantechs.provider.mes.pm.service.MesPmDailyPlanService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
    @Resource
    private MesPmDailyPlanDetMapper mesPmDailyPlanDetMapper;
    @Resource
    private MesPmWorkOrderMapper mesPmWorkOrderMapper;

    @Override
    public List<MesPmDailyPlanDto> findList(SearchMesPmDailyPlan searchMesPmDailyPlan) {
        if(StringUtils.isEmpty(searchMesPmDailyPlan.getOrgId())) {
            SysUser sysUser = currentUser();
            searchMesPmDailyPlan.setOrgId(sysUser.getOrganizationId());
        }

        if(StringUtils.isNotEmpty(searchMesPmDailyPlan.getPlanStartTime())) {
            try {
                searchMesPmDailyPlan.setStartTime(DateUtils.getStrToDateTime(searchMesPmDailyPlan.getPlanStartTime()));
                searchMesPmDailyPlan.setEndTime(DateUtils.getStrToDateTime(searchMesPmDailyPlan.getPlanStartTime()));
            } catch (ParseException e) {
                throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"时间格式转化错误");
            }
        }
        return mesPmDailyPlanMapper.findList(searchMesPmDailyPlan);
    }

    @Override
    public List<MesPmDailyPlanDto> findDaysList(SearchMesPmDailyPlan searchMesPmDailyPlan) throws ParseException {

        SysUser user = currentUser();

        searchMesPmDailyPlan.setOrgId(user.getOrganizationId());
        //如未填写计划日期则取当天时间
        Calendar calendar = Calendar.getInstance();
        if(StringUtils.isEmpty(searchMesPmDailyPlan.getPlanStartTime())) {
            searchMesPmDailyPlan.setStartTime(new Date());
        }else{
            searchMesPmDailyPlan.setStartTime(DateUtils.getStrToDateTime(searchMesPmDailyPlan.getPlanStartTime()));
        }

        calendar.setTime(searchMesPmDailyPlan.getStartTime());
        calendar.add(Calendar.DATE, +2);
        searchMesPmDailyPlan.setEndTime(calendar.getTime());
        searchMesPmDailyPlan.setStatus((byte)1);
        return mesPmDailyPlanMapper.findList(searchMesPmDailyPlan);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int save(MesPmDailyPlan mesPmDailyPlan) {
        SysUser user = currentUser();
        int num = 1;
        String sysOrderTypeCode=mesPmDailyPlan.getSysOrderTypeCode();
        Byte sourceBigType=mesPmDailyPlan.getSourceBigType();

        if(StringUtils.isEmpty(sysOrderTypeCode)){
            sysOrderTypeCode="MES-DPO";//单据类型 生产日计划
            mesPmDailyPlan.setSysOrderTypeCode(sysOrderTypeCode);
        }
        if(StringUtils.isEmpty(sourceBigType)){
            sourceBigType=(byte)2;//来源类型 自建
            mesPmDailyPlan.setSourceBigType(sourceBigType);
        }
        if(StringUtils.isEmpty(mesPmDailyPlan.getDailyPlanCode())){
            //计划单号
            mesPmDailyPlan.setDailyPlanCode(CodeUtils.getId("PLAN-"));
        }
        mesPmDailyPlan.setCreateTime(new Date());
        mesPmDailyPlan.setCreateUserId(user.getUserId());
        mesPmDailyPlan.setOrgId(user.getOrganizationId());
        mesPmDailyPlan.setIsDelete((byte) 1);
        mesPmDailyPlan.setStatus((byte) 1);
        num=mesPmDailyPlanMapper.insertUseGeneratedKeys(mesPmDailyPlan);

        List<MesPmDailyPlanDet> mesPmDailyPlanDets=mesPmDailyPlan.getMesPmDailyPlanDets();
        if(mesPmDailyPlanDets.size()>0){
            for (MesPmDailyPlanDet mesPmDailyPlanDet : mesPmDailyPlanDets) {
                MesPmWorkOrder mesPmWorkOrder=mesPmWorkOrderMapper.selectByPrimaryKey(mesPmDailyPlanDet.getWorkOrderId());
                if(StringUtils.isEmpty(mesPmWorkOrder)){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"找不到相应的工单信息");
                }
                BigDecimal nowQty=new BigDecimal(0);
                BigDecimal scheduleQty=new BigDecimal(0);
                BigDecimal workOrderQty=new BigDecimal(0);
                if(StringUtils.isNotEmpty(mesPmWorkOrder.getScheduledQty()))
                    scheduleQty=mesPmWorkOrder.getScheduledQty();//工单已排产数量

                if(StringUtils.isNotEmpty(mesPmWorkOrder.getWorkOrderQty()))
                    workOrderQty=mesPmWorkOrder.getWorkOrderQty();//工单数量

                if(StringUtils.isNotEmpty(mesPmDailyPlanDet.getScheduleQty()))
                    nowQty=mesPmDailyPlanDet.getFinishedQty();//本次排产数量

                if(nowQty.compareTo(new BigDecimal(0))!=1){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"排产数量必须大于0");
                }
                if((nowQty.add(scheduleQty)).compareTo(workOrderQty)==1){
                    throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"排产数量不能大于工单数量");
                }

                //新增明细
                mesPmDailyPlanDet.setDailyPlanId(mesPmDailyPlan.getDailyPlanId());
                mesPmDailyPlanDet.setCreateUserId(user.getUserId());
                mesPmDailyPlanDet.setCreateTime(new Date());
                mesPmDailyPlanDet.setIsDelete((byte)1);
                mesPmDailyPlanDet.setOrgId(user.getOrganizationId());
                num=mesPmDailyPlanDetMapper.insertUseGeneratedKeys(mesPmDailyPlanDet);

                //更新工单排产数量
                MesPmWorkOrder upMesPmWorkOrder=new MesPmWorkOrder();
                upMesPmWorkOrder.setWorkOrderId(mesPmWorkOrder.getWorkOrderId());
                upMesPmWorkOrder.setScheduledQty(mesPmWorkOrder.getScheduledQty().add(nowQty));
                upMesPmWorkOrder.setMaterialId(user.getUserId());
                upMesPmWorkOrder.setModifiedTime(new Date());
                num=mesPmWorkOrderMapper.updateByPrimaryKeySelective(upMesPmWorkOrder);

            }
        }

        return num;
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

    /**
     * 获取当前登录用户
     *
     * @return
     */
    private SysUser currentUser() {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
