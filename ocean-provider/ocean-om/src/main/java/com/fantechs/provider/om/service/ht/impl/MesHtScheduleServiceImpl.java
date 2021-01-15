package com.fantechs.provider.om.service.ht.impl;

import com.fantechs.common.base.general.entity.mes.pm.history.MesHtSchedule;
import com.fantechs.common.base.general.dto.mes.pm.history.MesHtScheduleDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.om.mapper.MesHtScheduleMapper;
import com.fantechs.provider.om.service.ht.MesHtScheduleService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import com.fantechs.common.base.utils.StringUtils;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月9日 14:18
 * @Description: 排产单履历表接口实现
 * @Version: 1.0
 */
@Service
public class MesHtScheduleServiceImpl extends BaseService<MesHtSchedule>  implements MesHtScheduleService {

     @Resource
     private MesHtScheduleMapper mesHtScheduleMapper;

    @Override
    public List<MesHtSchedule> selectAll(Map<String,Object> map) {
        Example example = new Example(MesHtSchedule.class);
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
        return mesHtScheduleMapper.selectByExample(example);
    }

    @Override
    public MesHtSchedule selectByKey(Object id) {
        MesHtSchedule mesHtSchedule = mesHtScheduleMapper.selectByPrimaryKey(id);
        if(mesHtSchedule != null && (mesHtSchedule.getIsDelete() != null && mesHtSchedule.getIsDelete() == 0)){
        mesHtSchedule = null;
        }
        return mesHtSchedule;
    }

    @Override
    public int save(MesHtSchedule mesHtSchedule) {
        mesHtSchedule.setCreateUserId(null);
        mesHtSchedule.setIsDelete((byte)1);
        return mesHtScheduleMapper.insertSelective(mesHtSchedule);
    }

    @Override
    public int deleteByKey(Object id) {
        MesHtSchedule mesHtSchedule = new MesHtSchedule();
        mesHtSchedule.setHtScheduleId((long)id);
        mesHtSchedule.setIsDelete((byte)0);
        return update(mesHtSchedule);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<MesHtSchedule> mesHtSchedules = selectAll(map);
        if (StringUtils.isNotEmpty(mesHtSchedules)) {
            for (MesHtSchedule mesHtSchedule : mesHtSchedules) {
                if(deleteByKey(mesHtSchedule.getHtScheduleId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(MesHtSchedule mesHtSchedule) {
        mesHtSchedule.setModifiedUserId(null);
        return mesHtScheduleMapper.updateByPrimaryKeySelective(mesHtSchedule);
    }

   @Override
   public List<MesHtScheduleDTO> selectFilterAll(Map<String, Object> map) {
       return mesHtScheduleMapper.selectFilterAll(map);
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
