package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.dto.mes.pm.SaveMesPmExplainPlanDTO;
import com.fantechs.common.base.general.dto.mes.pm.SaveMesPmMasterPlanDTO;
import com.fantechs.common.base.general.entity.mes.pm.MesPmExplainPlan;
import com.fantechs.common.base.general.dto.mes.pm.MesPmExplainPlanDTO;
import com.fantechs.common.base.general.entity.mes.pm.MesPmExplainProcessPlan;
import com.fantechs.common.base.general.entity.mes.pm.MesPmMasterPlan;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessPlan;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.provider.mes.pm.service.MesPmExplainPlanService;
import com.fantechs.provider.mes.pm.mapper.MesPmExplainPlanMapper;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.service.MesPmExplainProcessPlanService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import com.fantechs.common.base.utils.StringUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月15日 15:58
 * @Description: 执行计划（周/日计划表）接口实现
 * @Version: 1.0
 */
@Service
public class MesPmExplainPlanServiceImpl extends BaseService<MesPmExplainPlan>  implements MesPmExplainPlanService{

     @Resource
     private MesPmExplainPlanMapper mesPmExplainPlanMapper;
     @Resource
     private MesPmExplainProcessPlanService mesPmExplainProcessPlanService;

    @Override
    public List<MesPmExplainPlan> selectAll(Map<String,Object> map) {
        Example example = new Example(MesPmExplainPlan.class);
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
        return mesPmExplainPlanMapper.selectByExample(example);
    }

    @Override
    public MesPmExplainPlan selectByKey(Object id) {
        MesPmExplainPlan mesPmExplainPlan = mesPmExplainPlanMapper.selectByPrimaryKey(id);
        if(mesPmExplainPlan != null && (mesPmExplainPlan.getIsDelete() != null && mesPmExplainPlan.getIsDelete() == 0)){
        mesPmExplainPlan = null;
        }
        return mesPmExplainPlan;
    }

    @Override
    public int save(MesPmExplainPlan mesPmExplainPlan) {
        SysUser sysUser = this.currentUser();
        mesPmExplainPlan.setCreateUserId(sysUser.getUserId());
        mesPmExplainPlan.setIsDelete((byte)1);
        mesPmExplainPlan.setNoScheduleQty(mesPmExplainPlan.getProductQty());
        mesPmExplainPlan.setExplainPlanCode(CodeUtils.getId("EPLAIN"));
        return mesPmExplainPlanMapper.insertSelective(mesPmExplainPlan);
    }

    @Override
    public int deleteByKey(Object id) {
        MesPmExplainPlan mesPmExplainPlan = new MesPmExplainPlan();
        mesPmExplainPlan.setExplainPlanId((long)id);
        mesPmExplainPlan.setIsDelete((byte)0);
        return update(mesPmExplainPlan);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<MesPmExplainPlan> mesPmExplainPlans = selectAll(map);
        if (StringUtils.isNotEmpty(mesPmExplainPlans)) {
            for (MesPmExplainPlan mesPmExplainPlan : mesPmExplainPlans) {
                if(deleteByKey(mesPmExplainPlan.getExplainPlanId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(MesPmExplainPlan mesPmExplainPlan) {
        SysUser sysUser = this.currentUser();
        mesPmExplainPlan.setModifiedUserId(sysUser.getUserId());
        return mesPmExplainPlanMapper.updateByPrimaryKeySelective(mesPmExplainPlan);
    }

   @Override
   public List<MesPmExplainPlanDTO> selectFilterAll(Map<String, Object> map) {
       return mesPmExplainPlanMapper.selectFilterAll(map);
   }

    @Override
    public int save(SaveMesPmExplainPlanDTO saveMesPmExplainPlanDTO) {
        MesPmExplainPlan mesPmExplainPlan = saveMesPmExplainPlanDTO.getMesPmExplainPlan();
        if(StringUtils.isEmpty(mesPmExplainPlan.getExplainPlanId())){
            //新增
            if(this.save(mesPmExplainPlan)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }else{
            //更新
            if(this.update(mesPmExplainPlan)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }
        List<MesPmExplainProcessPlan> mesPmExplainProcessPlanList = saveMesPmExplainPlanDTO.getMesPmExplainProcessPlanList();
        if(StringUtils.isNotEmpty(mesPmExplainProcessPlanList)){
            for (MesPmExplainProcessPlan mesPmExplainProcessPlan : mesPmExplainProcessPlanList) {
                if(StringUtils.isEmpty(mesPmExplainProcessPlan.getExplainProcessPlanId())){
                    mesPmExplainProcessPlan.setExplainPlanId(mesPmExplainPlan.getExplainPlanId());
                }
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
