package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessPlan;
import com.fantechs.common.base.general.dto.mes.pm.MesPmProcessPlanDTO;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.provider.mes.pm.service.MesPmProcessPlanService;
import com.fantechs.provider.mes.pm.mapper.MesPmProcessPlanMapper;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.support.BaseService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import com.fantechs.common.base.utils.StringUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月15日 16:26
 * @Description: 工序计划表接口实现
 * @Version: 1.0
 */
@Service
public class MesPmProcessPlanServiceImpl extends BaseService<MesPmProcessPlan>  implements MesPmProcessPlanService{

     @Resource
     private MesPmProcessPlanMapper mesPmProcessPlanMapper;

    @Override
    public List<MesPmProcessPlan> selectAll(Map<String,Object> map) {
        Example example = new Example(MesPmProcessPlan.class);
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
        return mesPmProcessPlanMapper.selectByExample(example);
    }

    @Override
    public MesPmProcessPlan selectByKey(Object id) {
        MesPmProcessPlan mesPmProcessPlan = mesPmProcessPlanMapper.selectByPrimaryKey(id);
        if(mesPmProcessPlan != null && (mesPmProcessPlan.getIsDelete() != null && mesPmProcessPlan.getIsDelete() == 0)){
        mesPmProcessPlan = null;
        }
        return mesPmProcessPlan;
    }

    @Override
    public int save(MesPmProcessPlan mesPmProcessPlan) {
        mesPmProcessPlan.setCreateUserId(null);
        mesPmProcessPlan.setIsDelete((byte)1);
        mesPmProcessPlan.setProcessPlanCode(CodeUtils.getId("MPPLAN"));
        return mesPmProcessPlanMapper.insertSelective(mesPmProcessPlan);
    }

    @Override
    public int deleteByKey(Object id) {
        MesPmProcessPlan mesPmProcessPlan = new MesPmProcessPlan();
        mesPmProcessPlan.setProcessPlanId((long)id);
        mesPmProcessPlan.setIsDelete((byte)0);
        return update(mesPmProcessPlan);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<MesPmProcessPlan> mesPmProcessPlans = selectAll(map);
        if (StringUtils.isNotEmpty(mesPmProcessPlans)) {
            for (MesPmProcessPlan mesPmProcessPlan : mesPmProcessPlans) {
                if(deleteByKey(mesPmProcessPlan.getProcessPlanId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(MesPmProcessPlan mesPmProcessPlan) {
        mesPmProcessPlan.setModifiedUserId(null);
        return mesPmProcessPlanMapper.updateByPrimaryKeySelective(mesPmProcessPlan);
    }

   @Override
   public List<MesPmProcessPlanDTO> selectFilterAll(Map<String, Object> map) {
       return mesPmProcessPlanMapper.selectFilterAll(map);
   }

    @Override
    public int batchAdd(List<MesPmProcessPlan> mesPmProcessPlanList) {
        return mesPmProcessPlanMapper.batchAdd(mesPmProcessPlanList);
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
