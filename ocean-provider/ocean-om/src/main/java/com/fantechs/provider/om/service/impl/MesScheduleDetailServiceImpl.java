package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.general.dto.om.MesScheduleDetailDTO;
import com.fantechs.common.base.general.entity.om.MesScheduleDetail;
import com.fantechs.provider.om.service.MesScheduleDetailService;
import com.fantechs.provider.om.mapper.MesScheduleDetailMapper;
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
 * @Date: 2021年1月9日 17:10
 * @Description: 排产详情接口实现
 * @Version: 1.0
 */
@Service
public class MesScheduleDetailServiceImpl extends BaseService<MesScheduleDetail>  implements MesScheduleDetailService{

     @Resource
     private MesScheduleDetailMapper mesScheduleDetailMapper;

    @Override
    public List<MesScheduleDetail> selectAll(Map<String,Object> map) {
        Example example = new Example(MesScheduleDetail.class);
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
        return mesScheduleDetailMapper.selectByExample(example);
    }

    @Override
    public MesScheduleDetail selectByKey(Object id) {
        MesScheduleDetail mesScheduleDetail = mesScheduleDetailMapper.selectByPrimaryKey(id);
        if(mesScheduleDetail != null && (mesScheduleDetail.getIsDelete() != null && mesScheduleDetail.getIsDelete() == 0)){
        mesScheduleDetail = null;
        }
        return mesScheduleDetail;
    }

    @Override
    public int save(MesScheduleDetail mesScheduleDetail) {
        mesScheduleDetail.setCreateUserId(null);
        mesScheduleDetail.setIsDelete((byte)1);
        return mesScheduleDetailMapper.insertSelective(mesScheduleDetail);
    }

    @Override
    public int deleteByKey(Object id) {
        MesScheduleDetail mesScheduleDetail = new MesScheduleDetail();
        mesScheduleDetail.setScheduleDetailId((long)id);
        mesScheduleDetail.setIsDelete((byte)0);
        return update(mesScheduleDetail);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<MesScheduleDetail> mesScheduleDetails = selectAll(map);
        if (StringUtils.isNotEmpty(mesScheduleDetails)) {
            for (MesScheduleDetail mesScheduleDetail : mesScheduleDetails) {
                if(deleteByKey(mesScheduleDetail.getScheduleDetailId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(MesScheduleDetail mesScheduleDetail) {
        mesScheduleDetail.setModifiedUserId(null);
        return mesScheduleDetailMapper.updateByPrimaryKeySelective(mesScheduleDetail);
    }

   @Override
   public List<MesScheduleDetailDTO> selectFilterAll(Map<String, Object> map) {
       return mesScheduleDetailMapper.selectFilterAll(map);
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
