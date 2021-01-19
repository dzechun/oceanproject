package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.dto.mes.pm.SaveMesPmMasterPlanDTO;
import com.fantechs.common.base.general.dto.mes.pm.TurnExplainPlanDTO;
import com.fantechs.common.base.general.entity.mes.pm.MesPmExplainPlan;
import com.fantechs.common.base.general.entity.mes.pm.MesPmExplainProcessPlan;
import com.fantechs.common.base.general.entity.mes.pm.MesPmMasterPlan;
import com.fantechs.common.base.general.dto.mes.pm.MesPmMasterPlanDTO;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessPlan;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.provider.mes.pm.service.MesPmExplainPlanService;
import com.fantechs.provider.mes.pm.service.MesPmExplainProcessPlanService;
import com.fantechs.provider.mes.pm.service.MesPmMasterPlanService;
import com.fantechs.provider.mes.pm.mapper.MesPmMasterPlanMapper;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.service.MesPmProcessPlanService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ControllerAdvice;
import tk.mybatis.mapper.entity.Example;
import com.fantechs.common.base.utils.StringUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月15日 15:52
 * @Description: 总计划表（月计划表）接口实现
 * @Version: 1.0
 */
@Service
public class MesPmMasterPlanServiceImpl extends BaseService<MesPmMasterPlan>  implements MesPmMasterPlanService{

     @Resource
     private MesPmMasterPlanMapper mesPmMasterPlanMapper;
     @Resource
     private MesPmExplainPlanService mesPmExplainPlanService;
     @Resource
     private MesPmProcessPlanService mesPmProcessPlanService;
     @Resource
     private MesPmExplainProcessPlanService mesPmExplainProcessPlanService;

    @Override
    public List<MesPmMasterPlan> selectAll(Map<String,Object> map) {
        Example example = new Example(MesPmMasterPlan.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("isDelete",1).orIsNull("isDelete");
        example.and(criteria1);
        if(StringUtils.isNotEmpty(map)){
            map.forEach((k,v)->{
                if(StringUtils.isNotEmpty(v)){
                    switch (k){
                        case "Name":
                            criteria.andLike(k,"%"+v+"%");
                            break;
                        default :
                            criteria.andEqualTo(k,v);
                            break;
                    }
                }
            });
        }
        return mesPmMasterPlanMapper.selectByExample(example);
    }

    @Override
    public MesPmMasterPlan selectByKey(Object id) {
        MesPmMasterPlan mesPmMasterPlan = mesPmMasterPlanMapper.selectByPrimaryKey(id);
        if(mesPmMasterPlan != null && (mesPmMasterPlan.getIsDelete() != null && mesPmMasterPlan.getIsDelete() == 0)){
        mesPmMasterPlan = null;
        }
        return mesPmMasterPlan;
    }

    @Override
    public int save(MesPmMasterPlan mesPmMasterPlan) {
        mesPmMasterPlan.setCreateUserId(null);
        mesPmMasterPlan.setIsDelete((byte)1);
        mesPmMasterPlan.setNoScheduleQty(mesPmMasterPlan.getProductQty());
        mesPmMasterPlan.setMasterPlanCode(CodeUtils.getId("MPLAIN"));
        return mesPmMasterPlanMapper.insertSelective(mesPmMasterPlan);
    }

    @Override
    public int deleteByKey(Object id) {
        MesPmMasterPlan mesPmMasterPlan = new MesPmMasterPlan();
        mesPmMasterPlan.setMasterPlanId((long)id);
        mesPmMasterPlan.setIsDelete((byte)0);
        return update(mesPmMasterPlan);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<MesPmMasterPlan> mesPmMasterPlans = selectAll(map);
        if (StringUtils.isNotEmpty(mesPmMasterPlans)) {
            for (MesPmMasterPlan mesPmMasterPlan : mesPmMasterPlans) {
                if(deleteByKey(mesPmMasterPlan.getMasterPlanId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(MesPmMasterPlan mesPmMasterPlan) {
        mesPmMasterPlan.setModifiedUserId(null);
        return mesPmMasterPlanMapper.updateByPrimaryKeySelective(mesPmMasterPlan);
    }

   @Override
   public List<MesPmMasterPlanDTO> selectFilterAll(Map<String, Object> map) {
       return mesPmMasterPlanMapper.selectFilterAll(map);
   }

    @Override
    public int save(SaveMesPmMasterPlanDTO saveMesPmMasterPlanDTO) {
        MesPmMasterPlan mesPmMasterPlan = saveMesPmMasterPlanDTO.getMesPmMasterPlan();
        if(StringUtils.isEmpty(mesPmMasterPlan.getMasterPlanId())){
            //新增
            if(this.save(mesPmMasterPlan)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }else{
            //更新
            if(this.update(mesPmMasterPlan)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }
        List<MesPmProcessPlan> mesPmProcessPlanList = saveMesPmMasterPlanDTO.getMesPmProcessPlanList();
        if(StringUtils.isNotEmpty(mesPmProcessPlanList)){
            for (MesPmProcessPlan mesPmProcessPlan : mesPmProcessPlanList) {
                if(StringUtils.isEmpty(mesPmProcessPlan.getProcessPlanId())){
                    mesPmProcessPlan.setMasterPlanId(mesPmMasterPlan.getMasterPlanId());
                }
            }
            if(mesPmProcessPlanService.batchAdd(mesPmProcessPlanList)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int turnExplainPlan(TurnExplainPlanDTO turnExplainPlanDTO) {
        MesPmMasterPlan mesPmMasterPlan = this.selectByKey(turnExplainPlanDTO.getMasterPlanId());
        if(StringUtils.isEmpty(mesPmMasterPlan)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005);
        }
        //判断总计划已排产数是否足够
        double theScheduleQty = turnExplainPlanDTO.getTheScheduleQty().doubleValue();
        double scheduledQty = mesPmMasterPlan.getScheduledQty().doubleValue();
        if(theScheduleQty>(mesPmMasterPlan.getProductQty().doubleValue()-scheduledQty)){
            throw new BizErrorException("本次排产数大于剩余排产数");
        }
        MesPmExplainPlan mesPmExplainPlan = new MesPmExplainPlan();
        mesPmExplainPlan.setMasterPlanId(mesPmMasterPlan.getMasterPlanId());
        mesPmExplainPlan.setWorkOrderId(mesPmMasterPlan.getWorkOrderId());
        mesPmExplainPlan.setProductQty(turnExplainPlanDTO.getTheScheduleQty());
        mesPmExplainPlan.setPlanedStartDate(turnExplainPlanDTO.getPlanedStartDate());
        mesPmExplainPlan.setPlanedEndDate(turnExplainPlanDTO.getPlanedEndDate());
        if(mesPmExplainPlanService.save(mesPmExplainPlan)<=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012006);
        }
        //更新总计划数据的已排产数
        mesPmMasterPlan.setScheduledQty(new BigDecimal(scheduledQty+theScheduleQty));
        if(this.update(mesPmMasterPlan)<=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012006);
        }
        //默认将总计划的工序计划带入到执行计划
        List<MesPmProcessPlan> mesPmProcessPlanList = mesPmProcessPlanService.selectAll(ControllerUtil.dynamicCondition("masterPlanId", mesPmMasterPlan.getMasterPlanId()));
        List<MesPmExplainProcessPlan> mesPmExplainProcessPlanList=new LinkedList<>();
        if(StringUtils.isNotEmpty(mesPmProcessPlanList)){
            for (MesPmProcessPlan mesPmProcessPlan : mesPmProcessPlanList) {
                MesPmExplainProcessPlan mesPmExplainProcessPlan = new MesPmExplainProcessPlan();
                BeanUtils.copyProperties(mesPmProcessPlan,mesPmExplainProcessPlan);
                mesPmExplainProcessPlan.setExplainPlanId(mesPmExplainPlan.getExplainPlanId());
                mesPmExplainProcessPlanList.add(mesPmExplainProcessPlan);
            }
            if(mesPmExplainProcessPlanService.batchAdd(mesPmExplainProcessPlanList)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }
        return 1;
    }


    /**
     * 获取当前登录用户
     * @return
     */
    private SysUser currentUser(){
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }

    /**
     * 记录操作历史
     * @param id
     * @param operation
     */
    private void recordHistory(Long id,String operation){
        /*HT ht= new HT();
        ht.setOperation(operation);
        MesSchedule mesSchedule = selectByKey(id);
        if (StringUtils.isEmpty(mesSchedule)){
            return;
        }
        BeanUtils.autoFillEqFields(mesSchedule,ht);
        htService.save(ht);*/
    }
}
