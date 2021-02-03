package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.general.dto.basic.BasePlatePartsDetDto;
import com.fantechs.common.base.general.dto.basic.BasePlatePartsDto;
import com.fantechs.common.base.general.dto.mes.pm.*;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrder;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardPool;
import com.fantechs.common.base.general.entity.basic.search.SearchBasePlateParts;
import com.fantechs.common.base.general.entity.mes.pm.*;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.mes.pm.service.*;
import com.fantechs.provider.mes.pm.mapper.MesPmMasterPlanMapper;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.support.BaseService;
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
     @Resource
     private SmtWorkOrderService smtWorkOrderService;
     @Resource
     private SmtWorkOrderCardPoolService smtWorkOrderCardPoolService;
     @Resource
     private SmtWorkOrderCardCollocationService smtWorkOrderCardCollocationService;
     @Resource
     private BaseFeignApi baseFeignApi;

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
        SysUser sysUser = this.currentUser();
        mesPmMasterPlan.setCreateUserId(sysUser.getUserId());
        mesPmMasterPlan.setIsDelete((byte)1);
        mesPmMasterPlan.setNoScheduleQty(mesPmMasterPlan.getProductQty());
        mesPmMasterPlan.setMasterPlanCode(CodeUtils.getId("MPLAIN"));
        return mesPmMasterPlanMapper.insertSelective(mesPmMasterPlan);
    }

    @Override
    public int batchDelete(String ids) {
        SysUser sysUser = this.currentUser();
        String[] idGroup = ids.split(",");
        for (String id : idGroup) {
            MesPmMasterPlan mesPmMasterPlan = this.selectByKey(id);
            if(StringUtils.isEmpty(mesPmMasterPlan)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return super.batchDelete(ids);
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
        SysUser sysUser = this.currentUser();
        mesPmMasterPlan.setModifiedUserId(sysUser.getUserId());
        return mesPmMasterPlanMapper.updateByPrimaryKeySelective(mesPmMasterPlan);
    }

   @Override
   public List<MesPmMasterPlanDTO> selectFilterAll(Map<String, Object> map) {
       return mesPmMasterPlanMapper.selectFilterAll(map);
   }

    @Override
    @Transactional
    public int save(SaveMesPmMasterPlanDTO saveMesPmMasterPlanDTO) {
        MesPmMasterPlan mesPmMasterPlan = saveMesPmMasterPlanDTO.getMesPmMasterPlan();
        double scheduleQuantity = mesPmMasterPlan.getProductQty().doubleValue();
        if(scheduleQuantity<=0){
            throw new BizErrorException("排产数必须大于0");
        }
        if(StringUtils.isEmpty(mesPmMasterPlan.getMasterPlanId())){
            //新增
            //判断计划排产数是否大于工单数
            SmtWorkOrder smtWorkOrder = smtWorkOrderService.selectByKey(mesPmMasterPlan.getWorkOrderId());
            if(StringUtils.isEmpty(smtWorkOrder)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012005);
            }
            double workOrderQuantity = smtWorkOrder.getWorkOrderQuantity().doubleValue();
            double scheduledQuantity = smtWorkOrder.getScheduledQuantity().doubleValue();

            if(scheduleQuantity>(workOrderQuantity-scheduledQuantity)){
                throw new BizErrorException("计划排产数不能大于工单剩余生产数");
            }
            smtWorkOrder.setScheduledQuantity(new BigDecimal(scheduledQuantity+scheduleQuantity));
            if(smtWorkOrderService.update(smtWorkOrder)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
            mesPmMasterPlan.setScheduledQty(null);
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
        //找出原有工序计划
        List<MesPmProcessPlan> mesPmProcessPlans = mesPmProcessPlanService.selectAll(ControllerUtil.dynamicCondition(
                "masterPlanId", mesPmMasterPlan.getMasterPlanId()
        ));
        if(StringUtils.isNotEmpty(mesPmProcessPlanList)){
            for (MesPmProcessPlan mesPmProcessPlan : mesPmProcessPlanList) {
                if(StringUtils.isEmpty(mesPmProcessPlan.getProcessPlanId())){
                    mesPmProcessPlan.setMasterPlanId(mesPmMasterPlan.getMasterPlanId());
                    mesPmProcessPlan.setProcessPlanCode(CodeUtils.getId("MPPP"));
                    if(mesPmProcessPlanService.save(mesPmProcessPlan)<=0){
                        throw new BizErrorException(ErrorCodeEnum.OPT20012006);
                    }
                }else{
                    if(mesPmProcessPlanService.update(mesPmProcessPlan)<=0){
                        throw new BizErrorException(ErrorCodeEnum.OPT20012006);
                    }
                }
            }
            //=====找出删除的工序计划
            for (MesPmProcessPlan mesPmProcessPlan : mesPmProcessPlans) {
                boolean isHas=false;
                for (MesPmProcessPlan pmProcessPlan : mesPmProcessPlanList) {
                    if(pmProcessPlan.getProcessPlanId().compareTo(mesPmProcessPlan.getProcessPlanId())==0){
                        isHas=true;
                        break;
                    }
                }
                if(!isHas){
                    if(mesPmProcessPlanService.deleteByKey(mesPmProcessPlan.getProcessPlanId())<=0){
                        throw new BizErrorException(ErrorCodeEnum.OPT20012006);
                    }
                }
            }
            //=====
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int turnExplainPlan(TurnExplainPlanDTO turnExplainPlanDTO) {
        if(StringUtils.isEmpty(turnExplainPlanDTO.getTheScheduleQty()) || turnExplainPlanDTO.getTheScheduleQty().doubleValue()<=0){
            throw new BizErrorException("排产数必须大于0");
        }
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
        mesPmExplainPlan.setProLineId(mesPmMasterPlan.getProLineId());
        mesPmExplainPlan.setWorkOrderQuantity(mesPmMasterPlan.getWorkOrderQuantity());
        mesPmExplainPlan.setProductQty(turnExplainPlanDTO.getTheScheduleQty());
        mesPmExplainPlan.setNoScheduleQty(turnExplainPlanDTO.getTheScheduleQty());
        mesPmExplainPlan.setPlanedStartDate(turnExplainPlanDTO.getPlanedStartDate());
        mesPmExplainPlan.setPlanedEndDate(turnExplainPlanDTO.getPlanedEndDate());
        if(mesPmExplainPlanService.save(mesPmExplainPlan)<=0){
            throw new BizErrorException(ErrorCodeEnum.OPT20012006);
        }
        //更新总计划数据的已排产数
        mesPmMasterPlan.setScheduledQty(new BigDecimal(scheduledQty+theScheduleQty));
        mesPmMasterPlan.setNoScheduleQty(new BigDecimal(mesPmMasterPlan.getProductQty().doubleValue()-mesPmMasterPlan.getScheduledQty().doubleValue()));
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
                mesPmExplainProcessPlan.setExplainProcessPlanCode(CodeUtils.getId("MPEPP"));
                mesPmExplainProcessPlanList.add(mesPmExplainProcessPlan);
            }
            if(mesPmExplainProcessPlanService.batchAdd(mesPmExplainProcessPlanList)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }
        return 1;
    }

    @Override
    public MasterPlanPrintWorkOrderDTO masterPlanPrintWorkOrder(Long masterPlanId) {
        MesPmMasterPlan mesPmMasterPlan = this.selectByKey(masterPlanId);
        if(StringUtils.isEmpty(mesPmMasterPlan)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005);
        }
        Long workOrderId = mesPmMasterPlan.getWorkOrderId();
        SmtWorkOrder smtWorkOrder = smtWorkOrderService.selectByKey(workOrderId);
        if(StringUtils.isEmpty(smtWorkOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005);
        }
        MasterPlanPrintWorkOrderDTO masterPlanPrintWorkOrderDTO = new MasterPlanPrintWorkOrderDTO();

        //=====查找工单流程卡
        SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
        searchSmtWorkOrderCardPool.setWorkOrderId(workOrderId);
        List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolDtoList = smtWorkOrderCardPoolService.findList(searchSmtWorkOrderCardPool);
        if(StringUtils.isNotEmpty(smtWorkOrderCardPoolDtoList)){
            SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto = smtWorkOrderCardPoolDtoList.get(0);
            masterPlanPrintWorkOrderDTO.setWorkOrderCardId(smtWorkOrderCardPoolDto.getWorkOrderCardId());
        }
        //=====

        //=====查找工单及产品信息
        SearchSmtWorkOrder searchSmtWorkOrder = new SearchSmtWorkOrder();
        searchSmtWorkOrder.setWorkOrderId(workOrderId);
        List<SmtWorkOrderDto> smtWorkOrderDtoList = smtWorkOrderService.findList(searchSmtWorkOrder);
        if(StringUtils.isNotEmpty(smtWorkOrderDtoList)){
            SmtWorkOrderDto smtWorkOrderDto = smtWorkOrderDtoList.get(0);
            masterPlanPrintWorkOrderDTO.setProductModelName(smtWorkOrderDto.getProductModelName());
            masterPlanPrintWorkOrderDTO.setMaterialQuality(smtWorkOrderDto.getMaterialQuality());
            masterPlanPrintWorkOrderDTO.setColor(smtWorkOrderDto.getColor());
            masterPlanPrintWorkOrderDTO.setWorkOrderQuantity(smtWorkOrderDto.getWorkOrderQuantity());
            masterPlanPrintWorkOrderDTO.setScheduleDate(smtWorkOrderDto.getScheduleDate());
            masterPlanPrintWorkOrderDTO.setPlannedEndTime(smtWorkOrderDto.getPlannedEndTime());

            //=====查找部件组成信息
            SearchBasePlateParts searchBasePlateParts = new SearchBasePlateParts();
            searchBasePlateParts.setMaterialId(smtWorkOrderDto.getMaterialId());
            ResponseEntity<List<BasePlatePartsDto>> feignResult = baseFeignApi.findPlatePartsList(searchBasePlateParts);
            if(StringUtils.isNotEmpty(feignResult)){
                List<BasePlatePartsDto> basePlatePartsDtoList = feignResult.getData();
                if(StringUtils.isNotEmpty(basePlatePartsDtoList)){
                    BasePlatePartsDto basePlatePartsDto = basePlatePartsDtoList.get(0);
                    masterPlanPrintWorkOrderDTO.setBasePlatePartsDetDtoList(basePlatePartsDto.getList());
                }
            }
            //=====
        }
        //=====

        //=====查找主计划对应工序计划
        List<MesPmProcessPlan> mesPmProcessPlanList = mesPmProcessPlanService.selectAll(ControllerUtil.dynamicCondition("masterPlanId", masterPlanId));
        masterPlanPrintWorkOrderDTO.setMesPmProcessPlanList(mesPmProcessPlanList);
        //=====

        return masterPlanPrintWorkOrderDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int turnWorkOrderCardPool(TurnWorkOrderCardPoolDTO turnWorkOrderCardPoolDTO) {
        MesPmMasterPlan mesPmMasterPlan = this.selectByKey(turnWorkOrderCardPoolDTO.getMasterPlanId());
        if(StringUtils.isEmpty(mesPmMasterPlan)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005);
        }
        /*if(turnWorkOrderCardPoolDTO.getScheduleQty().doubleValue()> (mesPmMasterPlan.getProductQty().subtract(mesPmMasterPlan.getTurnCardQty()).doubleValue())){
            throw new BizErrorException("投产大于该计划剩余投产数");
        }*/
        Long workOrderId = mesPmMasterPlan.getWorkOrderId();
        SmtWorkOrder smtWorkOrder = smtWorkOrderService.selectByKey(workOrderId);
        if(StringUtils.isEmpty(smtWorkOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012005);
        }
        if(StringUtils.isEmpty(turnWorkOrderCardPoolDTO.getGenerate())){
            throw new BizErrorException("请求内容请带上是否生成工单流转卡字段");
        }
        if(turnWorkOrderCardPoolDTO.getGenerate()){
            //=====查找部件组成信息
            SearchBasePlateParts searchBasePlateParts = new SearchBasePlateParts();
            searchBasePlateParts.setMaterialId(smtWorkOrder.getMaterialId());
            ResponseEntity<List<BasePlatePartsDto>> feignResult = baseFeignApi.findPlatePartsList(searchBasePlateParts);
            if(StringUtils.isEmpty(feignResult)){
                throw new BizErrorException("未找到部件组成信息");
            }
            List<BasePlatePartsDto> basePlatePartsDtoList = feignResult.getData();
            if(StringUtils.isEmpty(basePlatePartsDtoList)){
                throw new BizErrorException("未找到部件组成信息");
            }
            BasePlatePartsDto basePlatePartsDto = basePlatePartsDtoList.get(0);
            List<BasePlatePartsDetDto> basePlatePartsDetDtoList = basePlatePartsDto.getList();
            if(StringUtils.isEmpty(basePlatePartsDetDtoList)){
                throw new BizErrorException("未找到部件详细信息");
            }
            List<Long> platePartsDetIdList = turnWorkOrderCardPoolDTO.getPlatePartsDetIdList();
            for (BasePlatePartsDetDto basePlatePartsDetDto : basePlatePartsDetDtoList) {
                if(StringUtils.isNotEmpty(platePartsDetIdList) && !platePartsDetIdList.contains(basePlatePartsDetDto.getPlatePartsDetId())){
                    continue;
                }
                //部件用量
                double quantity=StringUtils.isEmpty(basePlatePartsDetDto.getQuantity())?1:basePlatePartsDetDto.getQuantity().doubleValue();
                double curSchedule=turnWorkOrderCardPoolDTO.getScheduleQty().doubleValue()*quantity;
                //=====查找此主工单对于的部件工单是否存在，如果存在是否超数
                SearchSmtWorkOrder searchSmtWorkOrder = new SearchSmtWorkOrder();
                searchSmtWorkOrder.setMaterialId(basePlatePartsDetDto.getPlatePartsDetId());
                searchSmtWorkOrder.setParentId(workOrderId);
                List<SmtWorkOrderDto> smtWorkOrderDtoList = smtWorkOrderService.findList(searchSmtWorkOrder);
                SmtWorkOrder tempsmtWorkOrder;
                if(StringUtils.isNotEmpty(smtWorkOrderDtoList)){
                    SmtWorkOrderDto smtWorkOrderDto = smtWorkOrderDtoList.get(0);
                    //更新部件工单信息
                    tempsmtWorkOrder = new SmtWorkOrder();
                    BeanUtils.copyProperties(smtWorkOrderDto,tempsmtWorkOrder);
                    if(curSchedule>(tempsmtWorkOrder.getWorkOrderQuantity().subtract(tempsmtWorkOrder.getProductionQuantity())).doubleValue()){
                        throw new BizErrorException("当前投产数大于部件工单剩余投产数：（工单）"+smtWorkOrderDto.getWorkOrderCode());
                    }
                    tempsmtWorkOrder.setProductionQuantity(tempsmtWorkOrder.getProductionQuantity().add(new BigDecimal(curSchedule)));
                    if(smtWorkOrderService.update(tempsmtWorkOrder)<=0){
                        throw new BizErrorException("更新部件工单错误");
                    }
                }else{
                    //=====根据部件信息生成工单
                    tempsmtWorkOrder = new SmtWorkOrder();
                    tempsmtWorkOrder.setParentId(workOrderId);
                    tempsmtWorkOrder.setRouteId(basePlatePartsDetDto.getRouteId());
                    tempsmtWorkOrder.setProLineId(mesPmMasterPlan.getProLineId());
                    tempsmtWorkOrder.setMaterialId(basePlatePartsDetDto.getPlatePartsDetId());
                    double workOrderQuantity=smtWorkOrder.getWorkOrderQuantity().doubleValue()*quantity;
                    if(curSchedule>workOrderQuantity){
                        throw new BizErrorException("当前投产数大于部件工单总投产数：(部件组成)"+basePlatePartsDetDto.getPlatePartsDetId());
                    }
                    tempsmtWorkOrder.setWorkOrderQuantity(new BigDecimal(workOrderQuantity));
                    tempsmtWorkOrder.setProductionQuantity(new BigDecimal(curSchedule));
                    tempsmtWorkOrder.setRemark("无BOM");
                    tempsmtWorkOrder.setBarcodeRuleSetId(smtWorkOrder.getBarcodeRuleSetId());
                    SaveWorkOrderAndBom saveWorkOrderAndBom = new SaveWorkOrderAndBom();
                    saveWorkOrderAndBom.setSmtWorkOrder(tempsmtWorkOrder);
                    saveWorkOrderAndBom.setGenerate(false);
                    if(smtWorkOrderService.saveWorkOrderDTO(saveWorkOrderAndBom)<=0){
                        throw new BizErrorException("新增部件工单错误");
                    }
                    //=====
                }
                //=====转流程卡
                SmtWorkOrderCardCollocation smtWorkOrderCardCollocation = new SmtWorkOrderCardCollocation();
                smtWorkOrderCardCollocation.setWorkOrderId(tempsmtWorkOrder.getWorkOrderId());
                smtWorkOrderCardCollocation.setProduceQuantity((int)curSchedule);
                if(smtWorkOrderCardCollocationService.save(smtWorkOrderCardCollocation)<=0){
                    throw new BizErrorException("生成部件流程卡错误");
                }
                //=====
            }
            //=====
            double curProductionQuantity = turnWorkOrderCardPoolDTO.getScheduleQty().doubleValue()*platePartsDetIdList.size();
            smtWorkOrder.setProductionQuantity(smtWorkOrder.getProductionQuantity().add(new BigDecimal(curProductionQuantity)));
            if(smtWorkOrder.getWorkOrderStatus()!=2){
                smtWorkOrder.setWorkOrderStatus(2);//生产中
            }
            smtWorkOrderService.update(smtWorkOrder);
        }

        mesPmMasterPlan.setTurnCardQty(turnWorkOrderCardPoolDTO.getScheduleQty().add(mesPmMasterPlan.getTurnCardQty()));
        mesPmMasterPlan.setTurnProcessList((byte)1);
        return this.update(mesPmMasterPlan);
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
