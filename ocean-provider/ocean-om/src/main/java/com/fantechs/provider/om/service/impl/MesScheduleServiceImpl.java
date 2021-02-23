package com.fantechs.provider.om.service.impl;

import com.fantechs.common.base.entity.basic.history.WmsInHtStorageBillsDet;
import com.fantechs.common.base.entity.storage.WmsInStorageBillsDet;
import com.fantechs.common.base.general.dto.mes.pm.SaveWorkOrderAndBom;
import com.fantechs.common.base.general.dto.om.MesOrderMaterialDTO;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesOrderMaterialListDTO;
import com.fantechs.common.base.general.entity.om.MesSchedule;
import com.fantechs.common.base.general.dto.om.MesScheduleDTO;
import com.fantechs.common.base.general.entity.om.MesScheduleDetail;
import com.fantechs.common.base.general.entity.om.SmtOrder;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.history.MesHtSchedule;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.om.mapper.MesScheduleMapper;
import com.fantechs.provider.om.service.MesScheduleService;
import com.fantechs.provider.om.service.SmtOrderService;
import com.fantechs.provider.om.service.ht.MesHtScheduleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import com.fantechs.common.base.utils.StringUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Auther: bingo.ren
 * @Date: 2021年1月6日 18:01
 * @Description: 工单排产表接口实现
 * @Version: 1.0
 */
@Service
public class MesScheduleServiceImpl extends BaseService<MesSchedule>  implements MesScheduleService {

     @Resource
     private MesScheduleMapper mesScheduleMapper;
     @Resource
     private MesHtScheduleService mesHtScheduleService;
     @Resource
     private SmtOrderService smtOrderService;
     @Resource
     private PMFeignApi pmFeignApi;

    @Override
    public List<MesSchedule> selectAll(Map<String,Object> map) {
        Example example = new Example(MesSchedule.class);
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
        return mesScheduleMapper.selectByExample(example);
    }

    @Override
    public List<MesSchedule> selectLikeAll(Map<String,Object> map) {
        Example example = new Example(MesSchedule.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("isDelete",1).orIsNull("isDelete");
        example.and(criteria1);
        if(StringUtils.isNotEmpty(map)){
            map.forEach((k,v)->{
                if(StringUtils.isNotEmpty(v)){
                    criteria.orLike(k,"%"+v+"%");
                }
            });
        }
        return mesScheduleMapper.selectByExample(example);
    }

    @Override
    public MesSchedule selectByKey(Object id) {
        MesSchedule mesSchedule = mesScheduleMapper.selectByPrimaryKey(id);
        if(mesSchedule != null && (mesSchedule.getIsDelete() != null && mesSchedule.getIsDelete() == 0)){
        mesSchedule = null;
        }
        return mesSchedule;
    }

    @Override
    public MesSchedule selectByMap(Map<String,Object> map) {
        List<MesSchedule> mesSchedule = selectAll(map);
        if(StringUtils.isEmpty(mesSchedule)){
            return null;
        }
        if(mesSchedule.size()>1){
            return null;
        }
        return mesSchedule.get(0);
    }

    @Override
    public int save(MesSchedule mesSchedule) {
        mesSchedule.setScheduleCode(CodeUtils.getId("SCHED"));
        mesSchedule.setCreateUserId(null);
        mesSchedule.setIsDelete((byte)1);
        if(mesScheduleMapper.insertSelective(mesSchedule)<=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012006);
        }
        recordHistory(mesSchedule,"新增");
        return 1;
    }

    @Override
    public int batchDelete(String ids) {
        SysUser sysUser = this.currentUser();
        List<MesHtSchedule> mesHtScheduleList=new LinkedList<>();
        String[] idGroup = ids.split(",");
        for (String id : idGroup) {
            MesSchedule mesSchedule = this.selectByKey(id);
            if(StringUtils.isEmpty(mesSchedule)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            MesHtSchedule mesHtSchedule = new MesHtSchedule();
            BeanUtils.autoFillEqFields(mesSchedule,mesHtSchedule);
            mesHtSchedule.setModifiedUserId(sysUser.getUserId());
            mesHtScheduleList.add(mesHtSchedule);
        }
        mesHtScheduleService.batchSave(mesHtScheduleList);
        return super.batchDelete(ids);
    }

    @Override
    public int deleteByKey(Object id) {
        MesSchedule mesSchedule = new MesSchedule();
        mesSchedule.setScheduleId((long)id);
        mesSchedule.setIsDelete((byte)0);
        return update(mesSchedule);
    }

    @Override
    public int deleteByMap(Map<String,Object> map){
        List<MesSchedule> mesSchedules = selectAll(map);
        if (StringUtils.isNotEmpty(mesSchedules)) {
            for (MesSchedule mesSchedule : mesSchedules) {
                if(deleteByKey(mesSchedule.getScheduleId())<=0){
                    return 0;
                }
            }
        }
        return 1;
    }

    @Override
    public int update(MesSchedule mesSchedule) {
        mesSchedule.setModifiedUserId(null);
        if(mesScheduleMapper.updateByPrimaryKeySelective(mesSchedule)<=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012006);
        }
        recordHistory(mesSchedule,"更新");
        return 1;
    }

    @Override
    public String selectUserName(Object id) {
        return mesScheduleMapper.selectUserName(id);
    }

   @Override
   public List<MesScheduleDTO> selectFilterAll(Map<String, Object> map) {
       return mesScheduleMapper.selectFilterAll(map);
   }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveByOrderMaterialIdList(Long proLineId,List<Long> orderMaterialIdList) {
        double total=0.0;
        List<MesScheduleDetail> mesScheduleDetailList=new LinkedList<>();
        for (Long orderMaterialId : orderMaterialIdList) {
            SearchMesOrderMaterialListDTO searchMesOrderMaterialListDTO = new SearchMesOrderMaterialListDTO();
            searchMesOrderMaterialListDTO.setOrderMaterialId(orderMaterialId);
            List<MesOrderMaterialDTO> mesOrderMaterialDTOList = smtOrderService.findOrderMaterial(searchMesOrderMaterialListDTO);
            if(StringUtils.isEmpty(mesOrderMaterialDTOList)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"未找到订单物料数据");
            }
            MesOrderMaterialDTO mesOrderMaterialDTO = mesOrderMaterialDTOList.get(0);
            SmtOrder smtOrder = smtOrderService.selectByKey(mesOrderMaterialDTO.getOrderId());
            if(StringUtils.isEmpty(smtOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012005.getCode(),"未找到订单数据");
            }
            if(smtOrder.getScheduleStatus()==2){
                throw new BizErrorException("订单已完成排产");
            }
            smtOrder.setScheduleStatus((byte)1);
            if(smtOrderService.update(smtOrder)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
            //根据产品信息生成工单
            SmtWorkOrder smtWorkOrder = new SmtWorkOrder();
            smtWorkOrder.setOrderId(smtOrder.getOrderId());
            smtWorkOrder.setProLineId(proLineId);
            smtWorkOrder.setMaterialId(mesOrderMaterialDTO.getMaterialId());
            smtWorkOrder.setProductionQuantity(mesOrderMaterialDTO.getTotal());
            smtWorkOrder.setContractNo(smtOrder.getContractCode());
            smtWorkOrder.setRemark("无BOM");
            SaveWorkOrderAndBom saveWorkOrderAndBom = new SaveWorkOrderAndBom();
            saveWorkOrderAndBom.setSmtWorkOrder(smtWorkOrder);
            saveWorkOrderAndBom.setGenerate(false);
            ResponseEntity responseEntity = pmFeignApi.saveWorkOrder(saveWorkOrderAndBom);
            if(responseEntity.getCode()!=0){
                throw new BizErrorException(responseEntity.getMessage());
            }
            //生成排产详情
            MesScheduleDetail mesScheduleDetail = new MesScheduleDetail();
            mesScheduleDetail.setWorkOrderId((long)responseEntity.getData());
            mesScheduleDetail.setOrderId(smtOrder.getOrderId());
            mesScheduleDetailList.add(mesScheduleDetail);
            total+=mesOrderMaterialDTO.getTotal().doubleValue();
        }
        //生成排产单
        MesSchedule mesSchedule = new MesSchedule();
        mesSchedule.setProLineId(proLineId);
        mesSchedule.setTotal(new BigDecimal(total));
        if(this.save(mesSchedule)<=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012006);
        }
        if(mesScheduleDetailList.size()>0){
            for (MesScheduleDetail mesScheduleDetail : mesScheduleDetailList) {
                mesScheduleDetail.setScheduleId(mesSchedule.getScheduleId());
            }
            if(mesScheduleMapper.batchScheduleDetail(mesScheduleDetailList)<=0){
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
     * @param mesSchedule
     * @param operation
     */
    private void recordHistory(MesSchedule mesSchedule,String operation){
        MesHtSchedule mesHtSchedule = new MesHtSchedule();
        mesHtSchedule.setOperation(operation);
        if (StringUtils.isEmpty(mesSchedule)){
            return;
        }
        BeanUtils.autoFillEqFields(mesSchedule,mesHtSchedule);
        mesHtScheduleService.save(mesHtSchedule);
    }
}
