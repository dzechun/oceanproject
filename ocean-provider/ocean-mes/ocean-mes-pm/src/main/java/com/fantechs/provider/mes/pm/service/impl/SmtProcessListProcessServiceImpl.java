package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.pm.*;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSetDet;
import com.fantechs.common.base.general.dto.mes.pm.ProcessFinishedProductDTO;
import com.fantechs.common.base.general.dto.mes.pm.ProcessListDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtProcessListProcessDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtProcessListProcess;
import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.mes.pm.*;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.wms.out.OutFeignApi;
import com.fantechs.provider.mes.pm.mapper.*;
import com.fantechs.provider.mes.pm.service.*;
import com.fantechs.provider.mes.pm.utils.BarcodeRuleUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/11/23
*/
@Service
public class SmtProcessListProcessServiceImpl  extends BaseService<SmtProcessListProcess> implements SmtProcessListProcessService {

    @Resource
    private SmtProcessListProcessMapper smtProcessListProcessMapper;
    @Resource
    private SmtWorkOrderCardPoolMapper smtWorkOrderCardPoolMapper;
    @Resource
    private SmtWorkOrderCardPoolService smtWorkOrderCardPoolService;
    @Resource
    private SmtWorkOrderMapper smtWorkOrderMapper;
    @Resource
    private SmtWorkOrderService smtWorkOrderService;
    @Resource
    private SmtBarcodeRuleSpecMapper smtBarcodeRuleSpecMapper;
    @Resource
    private SmtBarcodeRuleSetDetMapper smtBarcodeRuleSetDetMapper;
    @Resource
    private SmtBarcodeRuleMapper smtBarcodeRuleMapper;
    @Resource
    private MesPmMasterPlanService mesPmMasterPlanService;
    @Resource
    private MesPmExplainPlanService mesPmExplainPlanService;
    @Resource
    private MesPmExplainProcessPlanService mesPmExplainProcessPlanService;
    @Resource
    private OutFeignApi outFeignApi;


    @Override
    public List<SmtProcessListProcessDto> findList(SearchSmtProcessListProcess searchSmtProcessListProcess) {
        return smtProcessListProcessMapper.findList(searchSmtProcessListProcess);
    }


    private int saveOBJ(SmtProcessListProcess smtProcessListProcess) {
        SysUser sysUser = this.currentUser();
        smtProcessListProcess.setCreateUserId(sysUser.getUserId());
        smtProcessListProcess.setIsDelete((byte)1);
        smtProcessListProcess.setProcessListProcessCode(CodeUtils.getId("SPLP"));
        return smtProcessListProcessMapper.insertSelective(smtProcessListProcess);
    }

    private List<SmtProcessListProcess> selectAll(Map<String,Object> map) {
        Example example = new Example(SmtProcessListProcess.class);
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
        return smtProcessListProcessMapper.selectByExample(example);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int startJob(SmtWorkOrderBarcodePool smtWorkOrderBarcodePool) {
        List<SmtProcessListProcess> list=new ArrayList<>();
        Long workOrderId = smtWorkOrderBarcodePool.getWorkOrderId();
        //获取工单信息
        SmtWorkOrderDto smtWorkOrderDto = smtWorkOrderMapper.selectByWorkOrderId(workOrderId);

        Long packageNumRuleId =null;
        //通过条码集合找到对应的条码规则、流转卡规则
        SearchSmtBarcodeRuleSetDet searchSmtBarcodeRuleSetDet = new SearchSmtBarcodeRuleSetDet();
        searchSmtBarcodeRuleSetDet.setBarcodeRuleSetId(smtWorkOrderDto.getBarcodeRuleSetId());
        List<SmtBarcodeRuleSetDetDto> smtBarcodeRuleSetDetList = smtBarcodeRuleSetDetMapper.findList(searchSmtBarcodeRuleSetDet);
        if (StringUtils.isEmpty(smtBarcodeRuleSetDetList)) {
            throw new BizErrorException("没有找到相关的条码集合规则");
        }
        for (SmtBarcodeRuleSetDet smtBarcodeRuleSetDet : smtBarcodeRuleSetDetList) {
            SmtBarcodeRule smtBarcodeRule = smtBarcodeRuleMapper.selectByPrimaryKey(smtBarcodeRuleSetDet.getBarcodeRuleId());
            if (StringUtils.isEmpty(smtBarcodeRule)) {
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
            //彩盒条码规则
            if (smtBarcodeRule.getBarcodeRuleCategoryId() == 3) {
                packageNumRuleId = smtBarcodeRule.getBarcodeRuleId();
                continue;
            }


        }

        Long barcodeRuleId = smtWorkOrderBarcodePool.getBarcodeRuleId();
        //查询该工单对应工艺路线下的工序
        List<ProcessListDto> processListDtos=smtProcessListProcessMapper.findProcess(workOrderId);
        if(StringUtils.isNotEmpty(processListDtos)){
            for (ProcessListDto processListDto : processListDtos) {
                SmtProcessListProcess smtProcessListProcess=new SmtProcessListProcess();
                Long processId = processListDto.getProcessId();
                smtProcessListProcess.setWorkOrderCardPoolId(smtWorkOrderBarcodePool.getWorkOrderCardPoolId());
                smtProcessListProcess.setWorkOrderBarcodePoolId(smtWorkOrderBarcodePool.getWorkOrderBarcodePoolId());
                smtProcessListProcess.setProcessId(processId);
                smtProcessListProcess.setStatus((byte) 0);
                smtProcessListProcess.setIsHold((byte) 0);
                smtProcessListProcess.setProcessListProcessCode(CodeUtils.getId("SPLP"));
                //彩盒号
                if(StringUtils.isNotEmpty(packageNumRuleId)){
                    String packageNum=generateCode(barcodeRuleId);
                    smtProcessListProcess.setPackageNum(packageNum);
                }
                //箱号
                //栈板号
                smtProcessListProcess.setIsDelete((byte) 1);
                list.add(smtProcessListProcess);
            }
        }else {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        return smtProcessListProcessMapper.insertList(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int finishedProduct(ProcessFinishedProductDTO processFinishedProductDTO) {
        //=====判断工单的状态
        SmtWorkOrderCardPool smtWorkOrderCardPool = smtWorkOrderCardPoolService.selectByKey(processFinishedProductDTO.getWorkOrderCardPoolId());
        if(StringUtils.isEmpty(smtWorkOrderCardPool)){
            throw new BizErrorException("未找到对应流程卡");
        }
        SmtWorkOrder smtWorkOrder = smtWorkOrderService.selectByKey(smtWorkOrderCardPool.getWorkOrderId());
        if(StringUtils.isEmpty(smtWorkOrder)){
            throw new BizErrorException("未找到对应工单");
        }
        if(smtWorkOrder.getWorkOrderStatus() != 0 && smtWorkOrder.getWorkOrderStatus() != 2){
            throw new BizErrorException("工单状态异常");
        }
        //=====

        //=====找到执行计划及执行计划工序，判断当前工序是否属于计划中
        Long parentWorkOrderId=smtWorkOrder.getWorkOrderId();
        //先判断当前流程卡工单是否存在父级
        if(StringUtils.isNotEmpty(smtWorkOrder.getParentId())){
            parentWorkOrderId=smtWorkOrder.getParentId();
        }
        //根据工单找到执行计划（这个地方需要注意，按照东鹏的需求，一个工单只会存在一个执行计划），但是如果是其他的需求有变化的话就需要调整
        //执行计划必须要有个查询标准，才能找到对应的数据
        List<MesPmExplainPlanDTO> mesPmExplainPlanDTOList = mesPmExplainPlanService.selectFilterAll(ControllerUtil.dynamicCondition("workOrderId", parentWorkOrderId));
        if(StringUtils.isEmpty(mesPmExplainPlanDTOList) || mesPmExplainPlanDTOList.size()>1){
            throw new BizErrorException("为找打执行计划或数据错误，无法进行数据反写");
        }
        MesPmExplainPlan mesPmExplainPlan = mesPmExplainPlanDTOList.get(0);
        //在执行计划内找打当前工序
        List<MesPmExplainProcessPlanDTO> mesPmExplainProcessPlanDTOS = mesPmExplainProcessPlanService.selectFilterAll(ControllerUtil.dynamicCondition(
                "explainPlanId", mesPmExplainPlan.getExplainPlanId(),
                "processId", processFinishedProductDTO.getProcessId()));
        if(StringUtils.isEmpty(mesPmExplainProcessPlanDTOS)){
            throw new BizErrorException("当前工序不在执行计划内，不允许操作");
        }
        MesPmExplainProcessPlanDTO mesPmExplainProcessPlanDTO = mesPmExplainProcessPlanDTOS.get(0);
        //判断当前工序是否有相应的操作权限
        if(processFinishedProductDTO.getProcessType()==1 && mesPmExplainProcessPlanDTO.getIsStartScan() ==0){
            throw new BizErrorException("当前工序不允许开工");
        }
        if(processFinishedProductDTO.getProcessType()==2 && mesPmExplainProcessPlanDTO.getIsJobScan() ==0){
            throw new BizErrorException("当前工序不允许报工");
        }

        MesPmExplainProcessPlan mesPmExplainProcessPlan = new MesPmExplainProcessPlan();
        mesPmExplainProcessPlan.setExplainProcessPlanId(mesPmExplainProcessPlanDTO.getExplainProcessPlanId());
        //=====

        //=====判断是否扫描的主工单
        //是，则需要按照排产数生成部件工单及流程卡
        //否，则进行部件流程卡过站
        if(StringUtils.isEmpty(smtWorkOrderCardPool.getParentId())){
            //是
            double workOrderQuantity = smtWorkOrder.getWorkOrderQuantity().doubleValue();
            double productQuantity = smtWorkOrder.getProductionQuantity().doubleValue();
            if(processFinishedProductDTO.getCurOutputQty().doubleValue()>(workOrderQuantity-productQuantity)){
                throw new BizErrorException("投料数不能大于工单剩余投产数");
            }
            TurnWorkOrderCardPoolDTO turnWorkOrderCardPoolDTO = new TurnWorkOrderCardPoolDTO();
            turnWorkOrderCardPoolDTO.setGenerate(true);
            turnWorkOrderCardPoolDTO.setMasterPlanId(mesPmExplainPlan.getMasterPlanId());
            turnWorkOrderCardPoolDTO.setScheduleQty(processFinishedProductDTO.getCurOutputQty());
            if(mesPmMasterPlanService.turnWorkOrderCardPool(turnWorkOrderCardPoolDTO)<=0){
                throw new BizErrorException("投产失败");
            }
            if(smtWorkOrder.getWorkOrderStatus()!=2){
                smtWorkOrder.setWorkOrderStatus(2);
                smtWorkOrderService.update(smtWorkOrder);
            }
        }else{
            //=====判断操作是否正常
            if(smtWorkOrderCardPool.getCardStatus()==0){
                //流程卡未开工
                if(processFinishedProductDTO.getProcessType()!=1){
                    throw new BizErrorException("流程卡尚未开工，不允许报工");
                }
                smtWorkOrderCardPool.setCardStatus((byte)1);
                if(smtWorkOrderCardPoolService.update(smtWorkOrderCardPool)<=0){
                    throw new BizErrorException("更新流程卡状态失败");
                }
                //流程卡开工确认数不能够大于工单剩余完成数量
                double workOrderQuantity = smtWorkOrder.getWorkOrderQuantity().doubleValue();
                double productQuantity = smtWorkOrder.getProductionQuantity().doubleValue();
                if(processFinishedProductDTO.getCurOutputQty().doubleValue()>(workOrderQuantity-productQuantity)){
                    throw new BizErrorException("开工数不能大于工单剩余投产数");
                }
                smtWorkOrder.setProductionQuantity(smtWorkOrder.getProductionQuantity().add(processFinishedProductDTO.getCurOutputQty()));
                smtWorkOrderService.update(smtWorkOrder);

            }else{
                //流程卡已经开工，这个时候的工段首工序开工确认数或者报工数不允许大于上工序的报工数
                List<SmtProcessListProcess> oldProcessListProcesses = this.selectAll(ControllerUtil.dynamicCondition(
                        "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId()));
                if(StringUtils.isNotEmpty(oldProcessListProcesses)){
                    //有过站记录，取最近的一条，查看开工数或者是报工数
                    SmtProcessListProcess smtProcessListProcess = oldProcessListProcesses.get(oldProcessListProcesses.size() - 1);
                    if(processFinishedProductDTO.getCurOutputQty().compareTo(smtProcessListProcess.getOutputQuantity())>0){
                        throw new BizErrorException("本次开工或报工不允许超过开工或上工序报工数");
                    }
                }
            }
            //=====
            List<SmtProcessListProcess> smtProcessListProcesses = this.selectAll(ControllerUtil.dynamicCondition(
                    "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                    "processId", processFinishedProductDTO.getProcessId()));
            SmtProcessListProcess smtProcessListProcess;
            if(StringUtils.isEmpty(smtProcessListProcesses)){
                smtProcessListProcess = new SmtProcessListProcess();
                smtProcessListProcess.setStatus((byte)processFinishedProductDTO.getOperation());
                smtProcessListProcess.setWorkOrderCardPoolId(processFinishedProductDTO.getWorkOrderCardPoolId());
                smtProcessListProcess.setProcessId(processFinishedProductDTO.getProcessId());
                smtProcessListProcess.setOutputQuantity(processFinishedProductDTO.getCurOutputQty());
                smtProcessListProcess.setCurOutputQty(processFinishedProductDTO.getCurOutputQty());
                smtProcessListProcess.setProcessType(processFinishedProductDTO.getProcessType());
                if(this.saveOBJ(smtProcessListProcess)<=0){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012006);
                }
                //执行计划工序数据反写
                mesPmExplainProcessPlan.setFinishedQty(processFinishedProductDTO.getCurOutputQty());
                mesPmExplainProcessPlan.setActualStartDate(new Date());
            }else{
                smtProcessListProcess = smtProcessListProcesses.get(0);
                if(smtProcessListProcess.getStatus()==2){
                    //如果工序当前流程卡已经是提交状态，但是本次操作是报工，且当前流程卡是开工就可以继续报工
                    if(processFinishedProductDTO.getProcessType()==1 || smtProcessListProcess.getProcessType()==2)
                        throw new BizErrorException("当前流程卡你已提交，请勿提交");
                }
                smtProcessListProcess.setStatus((byte)processFinishedProductDTO.getOperation());
                smtProcessListProcess.setOutputQuantity(new BigDecimal(smtProcessListProcess.getOutputQuantity().doubleValue()+processFinishedProductDTO.getCurOutputQty().doubleValue()));
                smtProcessListProcess.setCurOutputQty(processFinishedProductDTO.getCurOutputQty());
                smtProcessListProcess.setProcessType(processFinishedProductDTO.getProcessType());
                if(this.update(smtProcessListProcess)<=0){
                    throw new BizErrorException(ErrorCodeEnum.OPT20012006);
                }
                //执行计划工序数据反写
                mesPmExplainProcessPlan.setFinishedQty(mesPmExplainProcessPlanDTO.getFinishedQty().add(processFinishedProductDTO.getCurOutputQty()));
            }

            if(processFinishedProductDTO.getProcessType()==2 && processFinishedProductDTO.getOperation()==2){
                mesPmExplainProcessPlan.setActualEndDate(new Date());
            }
            mesPmExplainProcessPlanService.update(mesPmExplainProcessPlan);

            //执行计划数据回写
            mesPmExplainPlan.setFinishedQty(mesPmExplainPlan.getFinishedQty().add(processFinishedProductDTO.getCurOutputQty()));
            mesPmExplainPlanService.update(mesPmExplainPlan);


            if(processFinishedProductDTO.getProcessType()==1 && processFinishedProductDTO.getOperation()==2){
                //工序开工需要进行发料计划实发数反写
                WmsOutProductionMaterial wmsOutProductionMaterial = new WmsOutProductionMaterial();
                wmsOutProductionMaterial.setWorkOrderId(smtWorkOrder.getWorkOrderId());
                wmsOutProductionMaterial.setMaterialId(smtWorkOrder.getMaterialId());
                wmsOutProductionMaterial.setRealityQty(smtProcessListProcess.getOutputQuantity());
                ResponseEntity responseEntity = outFeignApi.updateByWorkOrderId(wmsOutProductionMaterial);
                if(StringUtils.isEmpty(responseEntity)){
                    throw new BizErrorException("反写发料实发数接口出错");
                }
                if(responseEntity.getCode()!=0){
                    throw new BizErrorException(responseEntity.getMessage());
                }
            }
        }
        //=====



        return 1;
    }

    @Transactional(rollbackFor = Exception.class)
    public String generateCode(Long barcodeRuleId) {
        String maxCode=null;
        Example example= new Example(SmtBarcodeRuleSpec.class);
        example.createCriteria().andEqualTo("barcodeRuleId",barcodeRuleId);
        List<SmtBarcodeRuleSpec> ruleSpecs = smtBarcodeRuleSpecMapper.selectByExample(example);
        if(StringUtils.isNotEmpty(ruleSpecs)){
            maxCode= BarcodeRuleUtils.getMaxSerialNumber(ruleSpecs, maxCode);
            maxCode= BarcodeRuleUtils.analysisCode(ruleSpecs, maxCode, null);
        }
       return maxCode;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int save(SmtProcessListProcess record) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        SmtWorkOrderCardPool smtWorkOrderCardPool = smtWorkOrderCardPoolMapper.selectByPrimaryKey(record.getWorkOrderCardPoolId());
        if(StringUtils.isEmpty(smtWorkOrderCardPool)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        SmtWorkOrder smtWorkOrder = smtWorkOrderMapper.selectByWorkOrderId(smtWorkOrderCardPool.getWorkOrderId());
        if(StringUtils.isEmpty(smtWorkOrder)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        SmtRouteProcess smtRouteProcess = new SmtRouteProcess();
        smtRouteProcess.setRouteId(smtWorkOrder.getRouteId());
        List<SmtRouteProcess> smtRouteProcessList = smtProcessListProcessMapper.select_smt_route_process(smtRouteProcess);
        List<SmtProcessListProcess> listProcesses = new ArrayList<>();
        for (SmtRouteProcess routeProcess : smtRouteProcessList) {
            record.setProcessId(routeProcess.getProcessId());
            record.setCreateTime(new Date());
            record.setCreateUserId(currentUser.getUserId());
            record.setModifiedTime(new Date());
            record.setModifiedUserId(currentUser.getUserId());
            listProcesses.add(record);
        }
        return smtProcessListProcessMapper.insertList(listProcesses);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SmtProcessListProcess entity) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        entity.setModifiedTime(new Date());
        entity.setModifiedUserId(currentUser.getUserId());
        return smtProcessListProcessMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(String ids) {
        SysUser currentUser = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(currentUser)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        String[] items = ids.split(",");
        for (String item : items) {
            SmtProcessListProcess smtProcessListProcess = smtProcessListProcessMapper.selectByPrimaryKey(item);
            if(StringUtils.isEmpty(smtProcessListProcess)){
                throw new BizErrorException(ErrorCodeEnum.OPT20012003);
            }
        }
        return smtProcessListProcessMapper.deleteByIds(ids);
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
}
