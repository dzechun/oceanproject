package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.dto.mes.pm.MesPmExplainProcessPlanDTO;
import com.fantechs.common.base.general.entity.mes.pm.MesPmExplainProcessPlan;
import com.fantechs.common.base.general.entity.mes.pm.MesPmMasterPlan;
import com.fantechs.provider.mes.pm.service.MesPmExplainProcessPlanService;
import com.fantechs.provider.mes.pm.mapper.MesPmExplainProcessPlanMapper;
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
 * @Date: 2021年1月18日 10:46
 * @Description: 执行工序计划表接口实现
 * @Version: 1.0
 */
@Service
public class MesPmExplainProcessPlanServiceImpl extends BaseService<MesPmExplainProcessPlan>  implements MesPmExplainProcessPlanService{

     @Resource
     private MesPmExplainProcessPlanMapper mesPmExplainProcessPlanMapper;

    @Override
    public List<MesPmExplainProcessPlan> selectAll(Map<String,Object> map) {
        Example example = new Example(MesPmExplainProcessPlan.class);
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
        return mesPmExplainProcessPlanMapper.selectByExample(example);
    }

    @Override
    public MesPmExplainProcessPlan selectByKey(Object id) {
        MesPmExplainProcessPlan mesPmExplainProcessPlan = mesPmExplainProcessPlanMapper.selectByPrimaryKey(id);
        if(mesPmExplainProcessPlan != null && (mesPmExplainProcessPlan.getIsDelete() != null && mesPmExplainProcessPlan.getIsDelete() == 0)){
        mesPmExplainProcessPlan = null;
        }
        return mesPmExplainProcessPlan;
    }

    @Override
    public int save(MesPmExplainProcessPlan mesPmExplainProcessPlan) {
        SysUser sysUser = this.currentUser();
        mesPmExplainProcessPlan.setCreateUserId(sysUser.getUserId());
        mesPmExplainProcessPlan.setIsDelete((byte)1);
        return mesPmExplainProcessPlanMapper.insertSelective(mesPmExplainProcessPlan);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser sysUser = this.currentUser();
        String[] idGroup = ids.split(",");
        for (String id : idGroup) {
            MesPmExplainProcessPlan mesPmExplainProcessPlan = this.selectByKey(id);
            if(StringUtils.isEmpty(mesPmExplainProcessPlan)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return super.batchDelete(ids);
    }

    @Override
    public int deleteByKey(Object id) {
        MesPmExplainProcessPlan mesPmExplainProcessPlan = new MesPmExplainProcessPlan();
        mesPmExplainProcessPlan.setExplainProcessPlanId((long)id);
        mesPmExplainProcessPlan.setIsDelete((byte)0);
        return update(mesPmExplainProcessPlan);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<MesPmExplainProcessPlan> mesPmExplainProcessPlans = selectAll(map);
        if (StringUtils.isNotEmpty(mesPmExplainProcessPlans)) {
            for (MesPmExplainProcessPlan mesPmExplainProcessPlan : mesPmExplainProcessPlans) {
                if(deleteByKey(mesPmExplainProcessPlan.getExplainProcessPlanId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(MesPmExplainProcessPlan mesPmExplainProcessPlan) {
        SysUser sysUser = this.currentUser();
        mesPmExplainProcessPlan.setModifiedUserId(sysUser.getUserId());
        return mesPmExplainProcessPlanMapper.updateByPrimaryKeySelective(mesPmExplainProcessPlan);
    }

   @Override
   public List<MesPmExplainProcessPlanDTO> selectFilterAll(Map<String, Object> map) {
       return mesPmExplainProcessPlanMapper.selectFilterAll(map);
   }

    @Override
    public int batchAdd(List<MesPmExplainProcessPlan> mesPmExplainProcessPlanList) {
        return mesPmExplainProcessPlanMapper.batchAdd(mesPmExplainProcessPlanList);
    }

    @Override
    public int batchUpdate(List<MesPmExplainProcessPlan> mesPmExplainProcessPlanList) {
        if(StringUtils.isEmpty(mesPmExplainProcessPlanList)){
            return 0;
        }
        for (MesPmExplainProcessPlan mesPmExplainProcessPlan : mesPmExplainProcessPlanList) {
            this.update(mesPmExplainProcessPlan);
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
