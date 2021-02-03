package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.dto.mes.pm.MaterialAndPartsDTO;
import com.fantechs.common.base.general.dto.mes.pm.SaveProcessListProcessReDTO;
import com.fantechs.common.base.general.dto.mes.pm.SmtProcessListProcessDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtProcessListProcess;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessListProcessRe;
import com.fantechs.common.base.general.dto.mes.pm.MesPmProcessListProcessReDTO;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProcessPlan;
import com.fantechs.common.base.general.entity.mes.pm.SmtProcessListProcess;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.provider.mes.pm.service.MesPmProcessListProcessReService;
import com.fantechs.provider.mes.pm.mapper.MesPmProcessListProcessReMapper;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.mes.pm.service.SmtProcessListProcessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import com.fantechs.common.base.utils.StringUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月19日 20:18
 * @Description: 流程单工序退回表接口实现
 * @Version: 1.0
 */
@Service
public class MesPmProcessListProcessReServiceImpl extends BaseService<MesPmProcessListProcessRe>  implements MesPmProcessListProcessReService{

     @Resource
     private MesPmProcessListProcessReMapper mesPmProcessListProcessReMapper;
     @Resource
     private SmtProcessListProcessService smtProcessListProcessService;

    @Override
    public List<MesPmProcessListProcessRe> selectAll(Map<String,Object> map) {
        Example example = new Example(MesPmProcessListProcessRe.class);
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
        return mesPmProcessListProcessReMapper.selectByExample(example);
    }

    @Override
    public MesPmProcessListProcessRe selectByKey(Object id) {
        MesPmProcessListProcessRe mesPmProcessListProcessRe = mesPmProcessListProcessReMapper.selectByPrimaryKey(id);
        if(mesPmProcessListProcessRe != null && (mesPmProcessListProcessRe.getIsDelete() != null && mesPmProcessListProcessRe.getIsDelete() == 0)){
        mesPmProcessListProcessRe = null;
        }
        return mesPmProcessListProcessRe;
    }

    @Override
    public int save(MesPmProcessListProcessRe mesPmProcessListProcessRe) {
        SysUser sysUser = this.currentUser();
        mesPmProcessListProcessRe.setCreateUserId(sysUser.getUserId());
        mesPmProcessListProcessRe.setIsDelete((byte)1);
        mesPmProcessListProcessRe.setProcessListProcessReCode(CodeUtils.getId("MPPLPR"));
        return mesPmProcessListProcessReMapper.insertSelective(mesPmProcessListProcessRe);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser sysUser = this.currentUser();
        String[] idGroup = ids.split(",");
        for (String id : idGroup) {
            MesPmProcessListProcessRe mesPmProcessListProcessRe = this.selectByKey(id);
            if(StringUtils.isEmpty(mesPmProcessListProcessRe)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return super.batchDelete(ids);
    }

    @Override
    public int deleteByKey(Object id) {
        MesPmProcessListProcessRe mesPmProcessListProcessRe = new MesPmProcessListProcessRe();
        mesPmProcessListProcessRe.setProcessListProcessReId((long)id);
        mesPmProcessListProcessRe.setIsDelete((byte)0);
        return update(mesPmProcessListProcessRe);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<MesPmProcessListProcessRe> mesPmProcessListProcessRes = selectAll(map);
        if (StringUtils.isNotEmpty(mesPmProcessListProcessRes)) {
            for (MesPmProcessListProcessRe mesPmProcessListProcessRe : mesPmProcessListProcessRes) {
                if(deleteByKey(mesPmProcessListProcessRe.getProcessListProcessReId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(MesPmProcessListProcessRe mesPmProcessListProcessRe) {
        SysUser sysUser = this.currentUser();
        mesPmProcessListProcessRe.setModifiedUserId(sysUser.getUserId());
        return mesPmProcessListProcessReMapper.updateByPrimaryKeySelective(mesPmProcessListProcessRe);
    }

   @Override
   public List<MesPmProcessListProcessReDTO> selectFilterAll(Map<String, Object> map) {
       List<MesPmProcessListProcessReDTO> mesPmProcessListProcessReDTOS = mesPmProcessListProcessReMapper.selectFilterAll(map);
       if(StringUtils.isNotEmpty(mesPmProcessListProcessReDTOS)){
           for (MesPmProcessListProcessReDTO mesPmProcessListProcessReDTO : mesPmProcessListProcessReDTOS) {
               if(StringUtils.isEmpty(mesPmProcessListProcessReDTO.getMaterialCode())){
                   MaterialAndPartsDTO materialAndPartsDTO = smtProcessListProcessService.findPartsInformation(mesPmProcessListProcessReDTO.getWorkOrderCardPoolId());
                   BeanUtils.autoFillEqFields(materialAndPartsDTO,mesPmProcessListProcessReDTO);
               }
           }
       }
       return mesPmProcessListProcessReDTOS;
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SaveProcessListProcessReDTO saveProcessListProcessReDTO) {
        if(saveProcessListProcessReDTO.getReProcessId()==saveProcessListProcessReDTO.getProcessId()){
            throw new BizErrorException("退回工序与被退工工序不能相同");
        }
        if(saveProcessListProcessReDTO.getReQty().doubleValue()<=0){
            throw new BizErrorException("退回数量必须大于0");
        }

        List<MesPmProcessListProcessRe> mesPmProcessListProcessReList = this.selectAll(ControllerUtil.dynamicCondition(
                "workOrderCardPoolId", saveProcessListProcessReDTO.getWorkOrderCardPoolId(),
                "processId", saveProcessListProcessReDTO.getProcessId(),
                "reProcessId", saveProcessListProcessReDTO.getReProcessId(),
                "status",1
        ));
        //查询退回工序已经报工数量，退回数量不允许大于报工数量，及上工序不能退下工序
        List<SmtProcessListProcess> reProcessListProcesseList = smtProcessListProcessService.selectAll(ControllerUtil.dynamicCondition(
                "workOrderCardPoolId", saveProcessListProcessReDTO.getWorkOrderCardPoolId(),
                "processId", saveProcessListProcessReDTO.getReProcessId(),
                "processType",2,
                "status",2));
        if(StringUtils.isEmpty(reProcessListProcesseList)){
            throw new BizErrorException("指定退回工序不存在已报工信息:"+saveProcessListProcessReDTO.getReProcessId());
        }
        SmtProcessListProcess preProcessListProcess = reProcessListProcesseList.get(0);
        MesPmProcessListProcessRe mesPmProcessListProcessRe = new MesPmProcessListProcessRe();
        if(StringUtils.isNotEmpty(mesPmProcessListProcessReList)){
            mesPmProcessListProcessRe=mesPmProcessListProcessReList.get(0);
            BigDecimal reQtyTotal = mesPmProcessListProcessRe.getReQty().add(saveProcessListProcessReDTO.getReQty());
            if(reQtyTotal.compareTo(preProcessListProcess.getOutputQuantity())>0){
                throw new BizErrorException("累计退回数量不允许大于指定退回工序报工数");
            }
            mesPmProcessListProcessRe.setReQty(new BigDecimal(saveProcessListProcessReDTO.getReQty().doubleValue()));
            mesPmProcessListProcessRe.setPreQty(saveProcessListProcessReDTO.getPreQty());
            mesPmProcessListProcessRe.setStatus(saveProcessListProcessReDTO.getOperation());
            mesPmProcessListProcessRe.setStaffId(saveProcessListProcessReDTO.getStaffId());
            if(this.update(mesPmProcessListProcessRe)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }else{
            if(saveProcessListProcessReDTO.getReQty().compareTo(preProcessListProcess.getOutputQuantity())>0){
                throw new BizErrorException("退回数量不允许大于指定退回工序报工数");
            }
            mesPmProcessListProcessRe.setWorkOrderCardPoolId(saveProcessListProcessReDTO.getWorkOrderCardPoolId());
            mesPmProcessListProcessRe.setProcessId(saveProcessListProcessReDTO.getProcessId());
            mesPmProcessListProcessRe.setReProcessId(saveProcessListProcessReDTO.getReProcessId());
            mesPmProcessListProcessRe.setReQty(new BigDecimal(saveProcessListProcessReDTO.getReQty().doubleValue()));
            mesPmProcessListProcessRe.setPreQty(saveProcessListProcessReDTO.getPreQty());
            mesPmProcessListProcessRe.setStatus(saveProcessListProcessReDTO.getOperation());
            mesPmProcessListProcessRe.setStaffId(saveProcessListProcessReDTO.getStaffId());
            if(this.save(mesPmProcessListProcessRe)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }

        //修改退回工序的报工情况
        /*if(saveProcessListProcessReDTO.getOperation()==2){
            preProcessListProcess.setOutputQuantity(preProcessListProcess.getOutputQuantity().subtract(saveProcessListProcessReDTO.getReQty()));
            preProcessListProcess.setStatus((byte)1);
            if(smtProcessListProcessService.update(preProcessListProcess)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }*/
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
