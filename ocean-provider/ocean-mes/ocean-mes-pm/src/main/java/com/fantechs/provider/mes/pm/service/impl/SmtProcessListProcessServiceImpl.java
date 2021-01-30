package com.fantechs.provider.mes.pm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtProcess;
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
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardPool;
import com.fantechs.common.base.general.dto.qms.QmsQualityConfirmationDto;
import com.fantechs.common.base.general.entity.mes.pm.*;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsQualityConfirmation;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.api.qms.QmsFeignApi;
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
import java.util.*;

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
    private SmtProcessListProcessService smtProcessListProcessService;
    @Resource
    private OutFeignApi outFeignApi;
    @Resource
    private QmsFeignApi qmsFeignApi;


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

    public List<SmtProcessListProcess> selectAll(Map<String,Object> map) {
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
    public int stationToScan(ProcessFinishedProductDTO processFinishedProductDTO){
        if(processFinishedProductDTO.getCurOutputQty().doubleValue()<=0){
            throw new BizErrorException("报工数需要大于0");
        }
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
        if(StringUtils.isNotEmpty(smtWorkOrder.getParentId()) && smtWorkOrder.getParentId()!=0){
            parentWorkOrderId=smtWorkOrder.getParentId();
        }
        //根据工单找到执行计划（这个地方需要注意，按照东鹏的需求，一个工单只会存在一个执行计划），但是如果是其他的需求有变化的话就需要调整
        //执行计划必须要有个查询标准，才能找到对应的数据
        List<MesPmExplainPlanDTO> mesPmExplainPlanDTOList = mesPmExplainPlanService.selectFilterAll(ControllerUtil.dynamicCondition("workOrderId", parentWorkOrderId));
        if(StringUtils.isEmpty(mesPmExplainPlanDTOList) || mesPmExplainPlanDTOList.size()>1){
            throw new BizErrorException("未找到执行计划或数据错误，无法进行数据反写");
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
        //找到工序信息判断当前工序是否拥有相关权限
        SmtProcess smtProcess = this.findSmtProcess(processFinishedProductDTO.getProcessId());
        if(StringUtils.isEmpty(smtProcess)){
            throw new BizErrorException("未找到工序信息："+processFinishedProductDTO.getProcessId());
        }
        //判断当前工序是否有相应的操作权限
        if(processFinishedProductDTO.getProcessType()==1 && smtProcess.getIsStartScan() ==0){
            throw new BizErrorException("当前工序不允许开工");
        }
        if(processFinishedProductDTO.getProcessType()==2 && smtProcess.getIsJobScan() ==0){
            throw new BizErrorException("当前工序不允许报工");
        }
        //判断当前工序是否在当前工单所属工艺下
        int count = smtProcessListProcessMapper.isExistProcessInWorkOrder(smtWorkOrder.getWorkOrderId(), processFinishedProductDTO.getProcessId());
        if(count<=0){
            throw new BizErrorException("当前工序不允许操作当前流程卡："+smtWorkOrderCardPool.getWorkOrderCardId());
        }

        MesPmExplainProcessPlan mesPmExplainProcessPlan = new MesPmExplainProcessPlan();
        mesPmExplainProcessPlan.setExplainProcessPlanId(mesPmExplainProcessPlanDTO.getExplainProcessPlanId());
        //=====

        if(processFinishedProductDTO.getPutInto()==1){
            //开料
            if(StringUtils.isNotEmpty(smtWorkOrderCardPool.getParentId()) && smtWorkOrderCardPool.getParentId()!=0){
                throw new BizErrorException("只有工单流转卡允许进行开料");
            }
            //====生成部件流程卡
            TurnWorkOrderCardPoolDTO turnWorkOrderCardPoolDTO = new TurnWorkOrderCardPoolDTO();
            turnWorkOrderCardPoolDTO.setGenerate(true);
            turnWorkOrderCardPoolDTO.setMasterPlanId(mesPmExplainPlan.getMasterPlanId());
            turnWorkOrderCardPoolDTO.setScheduleQty(processFinishedProductDTO.getCurOutputQty());
            turnWorkOrderCardPoolDTO.setPlatePartsDetIdList(processFinishedProductDTO.getPlatePartsDetIdList());
            if(mesPmMasterPlanService.turnWorkOrderCardPool(turnWorkOrderCardPoolDTO)<=0){
                throw new BizErrorException("投产失败");
            }
            //====
            smtWorkOrder.setProductionQuantity(smtWorkOrder.getProductionQuantity().add(processFinishedProductDTO.getCurOutputQty()));
            if(smtWorkOrder.getWorkOrderStatus()!=2){
                smtWorkOrder.setWorkOrderStatus(2);//生产中
            }
            smtWorkOrderService.update(smtWorkOrder);
            //====将所有生成的部件流程卡过站开工确认
            SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
            searchSmtWorkOrderCardPool.setParentId(smtWorkOrderCardPool.getWorkOrderCardPoolId());
            searchSmtWorkOrderCardPool.setCardStatus((byte)0);
            List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolDtoList = smtWorkOrderCardPoolService.findList(searchSmtWorkOrderCardPool);
            if(StringUtils.isEmpty(smtWorkOrderCardPoolDtoList)){
                throw new BizErrorException("开料失败");
            }

            SysUser sysUser = this.currentUser();
            List<SmtProcessListProcess> smtProcessListProcessList=new LinkedList<>();
            List<SmtWorkOrderCardPool> smtWorkOrderCardPoolList=new LinkedList<>();
            for (SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto : smtWorkOrderCardPoolDtoList) {
                SmtProcessListProcess smtProcessListProcess = new SmtProcessListProcess();
                smtProcessListProcess.setProcessListProcessCode(CodeUtils.getId("SPLP"));
                smtProcessListProcess.setWorkOrderCardPoolId(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
                smtProcessListProcess.setProcessId(processFinishedProductDTO.getProcessId());
                smtProcessListProcess.setProcessType((byte)1);
                smtProcessListProcess.setStatus((byte)2);
                Date date = new Date();
                smtProcessListProcess.setInboundTime(date);
                smtProcessListProcess.setStartWorkQty(processFinishedProductDTO.getCurOutputQty());
                smtProcessListProcess.setOutputQuantity(new BigDecimal(0));
                smtProcessListProcess.setCurOutputQty(processFinishedProductDTO.getCurOutputQty());
                //批量保存不会判断属性空字段，会覆盖数据库默认数据，以下是设置默认数据
                smtProcessListProcess.setIsDelete((byte)1);
                smtProcessListProcess.setCreateTime(date);
                smtProcessListProcess.setCreateUserId(sysUser.getUserId());
                smtProcessListProcess.setModifiedTime(date);
                smtProcessListProcess.setIsHold((byte)0);
                smtProcessListProcessList.add(smtProcessListProcess);
                SmtWorkOrderCardPool smtWorkOrderCardPool1 = new SmtWorkOrderCardPool();
                smtWorkOrderCardPool1.setWorkOrderCardPoolId(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
                smtWorkOrderCardPool1.setCardStatus((byte)3);
                smtWorkOrderCardPoolList.add(smtWorkOrderCardPool1);
            }
            smtWorkOrderCardPool.setCardStatus((byte)1);//工单流转卡投产中
            smtWorkOrderCardPoolService.update(smtWorkOrderCardPool);
            smtWorkOrderCardPoolService.batchUpdateStatus(smtWorkOrderCardPoolList);
            smtProcessListProcessService.batchSave(smtProcessListProcessList);
            Date date = new Date();
            mesPmExplainProcessPlan.setActualStartDate(date);
            mesPmExplainProcessPlanService.update(mesPmExplainProcessPlan);
            return 1;
        }

        //以下是工序的开工报工

        Long firstProcessIdWS = smtProcessListProcessMapper.firstProcessIdInWSection(processFinishedProductDTO.getProcessId(),smtWorkOrder.getRouteId());
        Long firstProcessIdR = smtProcessListProcessMapper.firstProcessIdInRoute(smtWorkOrder.getRouteId());
        if(StringUtils.isNotEmpty(smtWorkOrder.getParentId()) && smtWorkOrder.getParentId()!=0){
            SmtWorkOrder parentWorkOrder = smtWorkOrderService.selectByKey(smtWorkOrder.getParentId());
            firstProcessIdR = smtProcessListProcessMapper.firstProcessIdInRoute(parentWorkOrder.getRouteId());
        }
        SmtProcessListProcess curProcessListProcesse=null;//当前过站记录
        SmtProcessListProcess curStartProcessListProcesse=null;//当前开工过站记录
        SmtProcessListProcess preProcessListProcesse=null;//上一道过站记录
        BigDecimal outPutQtyToal=processFinishedProductDTO.getCurOutputQty();//开工总数
        Date date = new Date();//进站时间
        if(processFinishedProductDTO.getProcessType()==1 && (StringUtils.isEmpty(smtWorkOrderCardPool.getType()) || smtWorkOrderCardPool.getType()!=3)){
            //首工序不需要开工
            if(firstProcessIdR == processFinishedProductDTO.getProcessId()){
                throw new BizErrorException("开料工序不需要进行开工确认");
            }
            //非工段首工序不允许开工
            if(firstProcessIdWS != processFinishedProductDTO.getProcessId()){
                throw new BizErrorException("非工段首工序不允许开工");
            }
            //未经过首工序开料的流程卡不允许操作
            if(smtWorkOrderCardPool.getCardStatus() !=1) {
                throw new BizErrorException("当前流程卡未经首工序报工");
            }
            //流程卡已开工，这个时候工序已经进行到下一工段，再进行开工作业，则需要取品质确认数
            //====查找品质确认数
            SearchQmsQualityConfirmation searchQmsQualityConfirmation = new SearchQmsQualityConfirmation();
            searchQmsQualityConfirmation.setWorkOrderCardPoolId(smtWorkOrderCardPool.getWorkOrderCardPoolId());
            searchQmsQualityConfirmation.setQualityType((byte)1);
            searchQmsQualityConfirmation.setAffirmStatus((byte)2);
            ResponseEntity<List<QmsQualityConfirmationDto>> result = qmsFeignApi.findQualityConfirmationList(searchQmsQualityConfirmation);
            if(StringUtils.isEmpty(result) || result.getCode()!=0){
                throw new BizErrorException(result.getMessage());
            }
            List<QmsQualityConfirmationDto> qualityConfirmationDtoList = result.getData();
            if(StringUtils.isEmpty(qualityConfirmationDtoList)){
                throw new BizErrorException("未找到品质确认数据");
            }
            QmsQualityConfirmationDto qmsQualityConfirmationDto = qualityConfirmationDtoList.get(qualityConfirmationDtoList.size() - 1);
            BigDecimal okQty=qmsQualityConfirmationDto.getQualifiedQuantity();
            //====
            List<SmtProcessListProcess> tempProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                    "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                    "processId", processFinishedProductDTO.getProcessId(),
                    "processType",1));
            if(StringUtils.isNotEmpty(tempProcessListProcesseList)){
                curProcessListProcesse=tempProcessListProcesseList.get(0);
                if(curProcessListProcesse.getStatus()==2){
                    throw new BizErrorException("当前流程卡已开工，请勿重复提交");
                }
                outPutQtyToal=outPutQtyToal.add(curProcessListProcesse.getStartWorkQty());
            }else{
                curProcessListProcesse=new SmtProcessListProcess();
                curProcessListProcesse.setProcessListProcessCode(CodeUtils.getId("SPLP"));
                curProcessListProcesse.setWorkOrderCardPoolId(smtWorkOrderCardPool.getWorkOrderCardPoolId());
                curProcessListProcesse.setProcessId(processFinishedProductDTO.getProcessId());
                curProcessListProcesse.setInboundTime(date);
                smtProcessListProcessMapper.insertSelective(curProcessListProcesse);
            }
            if(outPutQtyToal.compareTo(okQty)>0){
                throw new BizErrorException("开工确认数不能大于品质确认数:"+okQty);
            }
        }else if(processFinishedProductDTO.getProcessType()==2 && (StringUtils.isEmpty(smtWorkOrderCardPool.getType()) || smtWorkOrderCardPool.getType()!=3)){
            List<SmtProcessListProcess> tempProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                    "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                    "processId", processFinishedProductDTO.getProcessId(),
                    "processType",2,
                    "status",2));
            if(StringUtils.isNotEmpty(tempProcessListProcesseList)){
                throw new BizErrorException("当前流程卡已报工，请勿重复提交");
            }
            //报工
            if(smtWorkOrderCardPool.getCardStatus() !=1 && firstProcessIdR != processFinishedProductDTO.getProcessId()){
                throw new BizErrorException("流程卡尚未开工，不允许报工");
            }

            if(StringUtils.isEmpty(smtWorkOrderCardPool.getParentId()) || smtWorkOrderCardPool.getParentId()==0){
                //工单流程卡报工
                workOrderCarReport(smtWorkOrderCardPool.getWorkOrderCardPoolId(),processFinishedProductDTO.getProcessId(),processFinishedProductDTO.getCurOutputQty().doubleValue(),mesPmExplainProcessPlanDTO);
                return 1;
            }
            if(firstProcessIdR == processFinishedProductDTO.getProcessId() || firstProcessIdWS == processFinishedProductDTO.getProcessId()){
                //首工序，则它的报工数不能大于开工确认数
                tempProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                        "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                        "processId", processFinishedProductDTO.getProcessId(),
                        "processType",1,
                        "status",2));
                if(StringUtils.isEmpty(tempProcessListProcesseList)){
                    throw new BizErrorException("该流程卡尚未开工确认");
                }
                curStartProcessListProcesse=tempProcessListProcesseList.get(0);
                if(StringUtils.isEmpty(curStartProcessListProcesse.getStartWorkQty()) || curStartProcessListProcesse.getStartWorkQty().doubleValue()==0){
                    throw new BizErrorException("首工序报工请先开工");
                }
                tempProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                        "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                        "processId", processFinishedProductDTO.getProcessId(),
                        "processType",2));
                if(StringUtils.isNotEmpty(tempProcessListProcesseList)){
                    curProcessListProcesse=tempProcessListProcesseList.get(0);
                    outPutQtyToal=outPutQtyToal.add(curProcessListProcesse.getOutputQuantity());
                }else{
                    curProcessListProcesse=new SmtProcessListProcess();
                    curProcessListProcesse.setProcessListProcessCode(CodeUtils.getId("SPLP"));
                    curProcessListProcesse.setWorkOrderCardPoolId(smtWorkOrderCardPool.getWorkOrderCardPoolId());
                    curProcessListProcesse.setProcessId(processFinishedProductDTO.getProcessId());
                    curProcessListProcesse.setInboundTime(date);
                    smtProcessListProcessMapper.insertSelective(curProcessListProcesse);
                }
                if(outPutQtyToal.compareTo(curStartProcessListProcesse.getStartWorkQty())>0){
                    throw new BizErrorException("首工序报工不能大于开工数");
                }
                if(firstProcessIdR == processFinishedProductDTO.getProcessId()){
                    //首工序进行报工，将流程卡状态从开料调整为投产中
                    smtWorkOrderCardPool.setCardStatus((byte)1);
                    smtWorkOrderCardPoolService.update(smtWorkOrderCardPool);
                }
            }else{
                //中间工序报工，报工数不能大于上工序报工数
                tempProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                        "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                        "processId", processFinishedProductDTO.getProcessId(),
                        "processType",2));
                if(StringUtils.isNotEmpty(tempProcessListProcesseList)){
                    curProcessListProcesse=tempProcessListProcesseList.get(0);
                    outPutQtyToal=outPutQtyToal.add(curProcessListProcesse.getOutputQuantity());
                    preProcessListProcesse = smtProcessListProcessMapper.findUp(curProcessListProcesse.getProcessListProcessId());
                }else{
                    curProcessListProcesse=new SmtProcessListProcess();
                    curProcessListProcesse.setProcessListProcessCode(CodeUtils.getId("SPLP"));
                    curProcessListProcesse.setWorkOrderCardPoolId(smtWorkOrderCardPool.getWorkOrderCardPoolId());
                    curProcessListProcesse.setProcessId(processFinishedProductDTO.getProcessId());
                    curProcessListProcesse.setInboundTime(date);
                    smtProcessListProcessMapper.insertSelective(curProcessListProcesse);
                }
                if(StringUtils.isEmpty(preProcessListProcesse)){
                    tempProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                            "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                            "processType",2,
                            "status",2));
                    if(StringUtils.isEmpty(tempProcessListProcesseList)){
                        throw new BizErrorException("该流程卡未找到报工信息");
                    }
                    preProcessListProcesse=tempProcessListProcesseList.get(tempProcessListProcesseList.size()-1);
                }
                if(outPutQtyToal.compareTo(preProcessListProcesse.getOutputQuantity())>0){
                    throw new BizErrorException("工序报工不能大于上工序报工数");
                }
            }
        }else if(StringUtils.isNotEmpty(smtWorkOrderCardPool.getType()) && smtWorkOrderCardPool.getType() == 3){
            //当前扫描的流程卡为报工退回的流程卡，只能走报工，且报工数不能大于自身的开工数
            List<SmtProcessListProcess> tempProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                    "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                    "processId", processFinishedProductDTO.getProcessId(),
                    "processType",2,
                    "status",1));
            if(StringUtils.isEmpty(tempProcessListProcesseList)){
                throw new BizErrorException("未找到当前工序退回流程卡");
            }
            curProcessListProcesse=tempProcessListProcesseList.get(0);
            outPutQtyToal=outPutQtyToal.add(curProcessListProcesse.getOutputQuantity());
            if(outPutQtyToal.compareTo(curProcessListProcesse.getStartWorkQty())>0){
                throw new BizErrorException("报工数不能大于退回数");
            }
        }

        if(processFinishedProductDTO.getProcessType()==1){
            curProcessListProcesse.setStartWorkQty(outPutQtyToal);
        }else if(processFinishedProductDTO.getProcessType()==2){
            curProcessListProcesse.setOutputQuantity(outPutQtyToal);
        }
        curProcessListProcesse.setCurOutputQty(processFinishedProductDTO.getCurOutputQty());
        curProcessListProcesse.setProcessType(processFinishedProductDTO.getProcessType());
        curProcessListProcesse.setStatus((processFinishedProductDTO.getOperation()));
        if(processFinishedProductDTO.getOperation()==2){
            curProcessListProcesse.setOutboundTime(new Date());
        }
        this.update(curProcessListProcesse);

        //=====执行计划与工序的数据回写
        mesPmExplainProcessPlan.setFinishedQty(outPutQtyToal);
        if(processFinishedProductDTO.getProcessType()==2 && processFinishedProductDTO.getOperation()==2){
            mesPmExplainProcessPlan.setActualEndDate(new Date());
        }
        mesPmExplainProcessPlanService.update(mesPmExplainProcessPlan);

        mesPmExplainPlan.setFinishedQty(outPutQtyToal);
        mesPmExplainPlanService.update(mesPmExplainPlan);
        //=====

        if(processFinishedProductDTO.getProcessType()==1 && processFinishedProductDTO.getOperation()==2){
            if(processFinishedProductDTO.getProcessId() == firstProcessIdWS && processFinishedProductDTO.getProcessId() != firstProcessIdR)
            sendMaterial(smtWorkOrder.getWorkOrderId(),smtWorkOrder.getMaterialId(),outPutQtyToal.doubleValue());
        }
        return 1;
    }

    private void back(){
/*
        //=====判断是否是操作开料
        if(processFinishedProductDTO.getPutInto()==1){
            if(StringUtils.isNotEmpty(smtWorkOrderCardPool.getParentId()) && smtWorkOrderCardPool.getParentId()!=0){
                throw new BizErrorException("只有工单流转卡允许进行开料");
            }
            *//*double workOrderQuantity = smtWorkOrder.getWorkOrderQuantity().doubleValue();
            double productQuantity = smtWorkOrder.getProductionQuantity().doubleValue();
            if(processFinishedProductDTO.getCurOutputQty().doubleValue()>(workOrderQuantity-productQuantity)){
                throw new BizErrorException("投料数不能大于工单剩余投产数");
            }*//*
            //====生成部件流程卡
            TurnWorkOrderCardPoolDTO turnWorkOrderCardPoolDTO = new TurnWorkOrderCardPoolDTO();
            turnWorkOrderCardPoolDTO.setGenerate(true);
            turnWorkOrderCardPoolDTO.setMasterPlanId(mesPmExplainPlan.getMasterPlanId());
            turnWorkOrderCardPoolDTO.setScheduleQty(processFinishedProductDTO.getCurOutputQty());
            turnWorkOrderCardPoolDTO.setPlatePartsDetIdList(processFinishedProductDTO.getPlatePartsDetIdList());
            if(mesPmMasterPlanService.turnWorkOrderCardPool(turnWorkOrderCardPoolDTO)<=0){
                throw new BizErrorException("投产失败");
            }
            //====
            if(smtWorkOrder.getWorkOrderStatus()!=2){
                smtWorkOrder.setWorkOrderStatus(2);//生产中
                smtWorkOrderService.update(smtWorkOrder);
            }
            //====将所有生成的部件流程卡过站开工确认
            SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
            searchSmtWorkOrderCardPool.setParentId(smtWorkOrderCardPool.getWorkOrderCardPoolId());
            searchSmtWorkOrderCardPool.setCardStatus((byte)0);
            List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolDtoList = smtWorkOrderCardPoolService.findList(searchSmtWorkOrderCardPool);
            if(StringUtils.isEmpty(smtWorkOrderCardPoolDtoList)){
                throw new BizErrorException("开料失败");
            }

            SysUser sysUser = this.currentUser();
            List<SmtProcessListProcess> smtProcessListProcessList=new LinkedList<>();
            List<SmtWorkOrderCardPool> smtWorkOrderCardPoolList=new LinkedList<>();
            for (SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto : smtWorkOrderCardPoolDtoList) {
                SmtProcessListProcess smtProcessListProcess = new SmtProcessListProcess();
                smtProcessListProcess.setProcessListProcessCode(CodeUtils.getId("SPLP"));
                smtProcessListProcess.setWorkOrderCardPoolId(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
                smtProcessListProcess.setProcessId(processFinishedProductDTO.getProcessId());
                smtProcessListProcess.setProcessType((byte)1);
                smtProcessListProcess.setStatus((byte)1);
                Date date = new Date();
                smtProcessListProcess.setInboundTime(date);
                smtProcessListProcess.setStartWorkQty(processFinishedProductDTO.getCurOutputQty());
                smtProcessListProcess.setCurOutputQty(processFinishedProductDTO.getCurOutputQty());
                //批量保存不会判断属性空字段，会覆盖数据库默认数据，以下是设置默认数据
                smtProcessListProcess.setIsDelete((byte)1);
                smtProcessListProcess.setCreateTime(date);
                smtProcessListProcess.setCreateUserId(sysUser.getUserId());
                smtProcessListProcess.setModifiedTime(date);
                smtProcessListProcess.setIsHold((byte)0);
                smtProcessListProcessList.add(smtProcessListProcess);
                SmtWorkOrderCardPool smtWorkOrderCardPool1 = new SmtWorkOrderCardPool();
                smtWorkOrderCardPool1.setWorkOrderCardPoolId(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
                smtWorkOrderCardPool1.setCardStatus((byte)3);
                smtWorkOrderCardPoolList.add(smtWorkOrderCardPool1);
            }
            smtWorkOrderCardPool.setCardStatus((byte)1);//工单流转卡投产中
            smtWorkOrderCardPoolService.update(smtWorkOrderCardPool);
            smtWorkOrderCardPoolService.batchUpdateStatus(smtWorkOrderCardPoolList);
            smtProcessListProcessService.batchSave(smtProcessListProcessList);
            Date date = new Date();
            mesPmExplainProcessPlan.setActualStartDate(date);
            mesPmExplainProcessPlanService.update(mesPmExplainProcessPlan);
            //====
        }else{

            //进行开工报工等操作
            //=====判断操作是否正常
            if(smtWorkOrderCardPool.getCardStatus()==0 || smtWorkOrderCardPool.getCardStatus()==3){
                //流程卡未开工
                if(processFinishedProductDTO.getProcessType()!=1){
                    throw new BizErrorException("流程卡尚未开工，不允许报工");
                }

                //流程卡开工确认数不能够大于工单剩余完成数量
                double workOrderQuantity = smtWorkOrder.getWorkOrderQuantity().doubleValue();
                double productQuantity = smtWorkOrder.getProductionQuantity().doubleValue();
                if(processFinishedProductDTO.getCurOutputQty().doubleValue()>(workOrderQuantity-productQuantity)){
                    throw new BizErrorException("开工数不能大于工单剩余投产数");
                }
                smtWorkOrder.setProductionQuantity(smtWorkOrder.getProductionQuantity().add(processFinishedProductDTO.getCurOutputQty()));
                smtWorkOrderService.update(smtWorkOrder);
                smtWorkOrderCardPool.setCardStatus((byte)1);
                smtWorkOrderCardPoolService.update(smtWorkOrderCardPool);
            }
            if(processFinishedProductDTO.getProcessType()==2 && (StringUtils.isEmpty(smtWorkOrderCardPool.getParentId()) || smtWorkOrderCardPool.getParentId()==0)){
                //工单流程卡报工
                workOrderCarReport(smtWorkOrderCardPool.getWorkOrderCardPoolId(),processFinishedProductDTO.getProcessId(),processFinishedProductDTO.getCurOutputQty().doubleValue(),mesPmExplainProcessPlanDTO);
            }
            //流程卡已经开工，这个时候的工段首工序开工确认数或者报工数不允许大于上工序的报工数
            List<SmtProcessListProcess> preProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                    "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId()));
            SmtProcessListProcess preProcessListProcess=null;
            if(StringUtils.isNotEmpty(preProcessListProcesseList)){
                //有过站记录，取最近的一条，查看开工数或者是报工数
                preProcessListProcess = preProcessListProcesseList.get(preProcessListProcesseList.size() - 1);
                if(processFinishedProductDTO.getCurOutputQty().compareTo(preProcessListProcess.getOutputQuantity())>0){
                    throw new BizErrorException("本次开工或报工不允许超过开工或上工序报工数");
                }
            }
            //=====
            List<SmtProcessListProcess> smtProcessListProcesses = this.selectAll(ControllerUtil.dynamicCondition(
                    "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                    "processId", processFinishedProductDTO.getProcessId()));
            SmtProcessListProcess smtProcessListProcess;
            if(StringUtils.isEmpty(smtProcessListProcesses)){
                smtProcessListProcess = new SmtProcessListProcess();
                smtProcessListProcess.setStatus(processFinishedProductDTO.getOperation());
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
                BigDecimal curOutPutTotal= new BigDecimal(smtProcessListProcess.getOutputQuantity().doubleValue()+processFinishedProductDTO.getCurOutputQty().doubleValue());
                if(StringUtils.isNotEmpty(preProcessListProcess)){
                    if(curOutPutTotal.compareTo(preProcessListProcess.getOutputQuantity())>0){
                        throw new BizErrorException("累计开工或报工不允许超过开工或上工序报工数");
                    }
                }
                smtProcessListProcess.setStatus(processFinishedProductDTO.getOperation());
                smtProcessListProcess.setOutputQuantity(curOutPutTotal);
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
                sendMaterial(smtWorkOrder.getWorkOrderId(),smtWorkOrder.getMaterialId(),smtProcessListProcess.getOutputQuantity().doubleValue());
            }
        }*/
        //=====
    }

    /**
     *  反写发料计划
     * @param workOrderId
     * @param materialId
     * @param outPutQty
     * @return
     */
    private int sendMaterial(Long workOrderId,Long materialId,double outPutQty){
        //工序开工需要进行发料计划实发数反写
        WmsOutProductionMaterial wmsOutProductionMaterial = new WmsOutProductionMaterial();
        wmsOutProductionMaterial.setWorkOrderId(workOrderId);
        wmsOutProductionMaterial.setMaterialId(materialId);
        wmsOutProductionMaterial.setRealityQty(new BigDecimal(outPutQty));
        ResponseEntity responseEntity = outFeignApi.updateByWorkOrderId(wmsOutProductionMaterial);
        if(StringUtils.isEmpty(responseEntity)){
            throw new BizErrorException("反写发料实发数接口出错");
        }
        if(responseEntity.getCode()!=0){
            throw new BizErrorException(responseEntity.getMessage());
        }
        return 1;
    }

    /**
     *  工单流程卡报工
     * @param id 工单流程卡ID
     * @param curProcessId 当前报工工序
     * @param outPutQty 报工数
     * @param mesPmExplainProcessPlan 执行计划工序，方便数据反写
     * @return
     */
    private int workOrderCarReport(Long id,Long curProcessId,double outPutQty,MesPmExplainProcessPlan mesPmExplainProcessPlan){
        //工单流程卡报工即对所有的部件流程卡进行报工
        SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
        searchSmtWorkOrderCardPool.setParentId(id);
        List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolDtoList = smtWorkOrderCardPoolService.findList(searchSmtWorkOrderCardPool);
        if(StringUtils.isEmpty(smtWorkOrderCardPoolDtoList)){
            throw new BizErrorException("工单流转卡未找到部件流转卡");
        }
        Date date = new Date();
        List<SmtWorkOrderCardPool> smtWorkOrderCardPoolList=new LinkedList<>();
        for (SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto : smtWorkOrderCardPoolDtoList) {
            if(smtWorkOrderCardPoolDto.getCardStatus()==2){
                continue;
            }
            //找到所有已经报工的部件流程卡里面最小的报工数
            Double minOutPut = findMinOutPut(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
            if(minOutPut !=0 && outPutQty>minOutPut){
                throw new BizErrorException("工单流程卡报工数不允许大于已报工部件流程卡的报工数");
            }
            //找到对应的工单
            SmtWorkOrder smtWorkOrder = smtWorkOrderService.selectByKey(smtWorkOrderCardPoolDto.getWorkOrderId());
            if(StringUtils.isEmpty(smtWorkOrder)){
                throw new BizErrorException("部件流程卡未找到对应工单数据+"+smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
            }
            //判断当前工序是否在当前工单所属工艺下
            int count = smtProcessListProcessMapper.isExistProcessInWorkOrder(smtWorkOrder.getWorkOrderId(), curProcessId);
            if(count<=0){
                continue;
            }
            //判断当前工序在当前流程卡是否已经提交
            SearchSmtProcessListProcess searchSmtProcessListProcess = new SearchSmtProcessListProcess();
            searchSmtProcessListProcess.setWorkOrderCardPoolId(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
            searchSmtProcessListProcess.setProcessId(curProcessId);
            searchSmtProcessListProcess.setProcessType((byte)2);
            List<SmtProcessListProcessDto> smtProcessListProcessDtoList = smtProcessListProcessService.findList(searchSmtProcessListProcess);
            SmtProcessListProcess smtProcessListProcess = new SmtProcessListProcess();
            if(StringUtils.isNotEmpty(smtProcessListProcessDtoList)){
                SmtProcessListProcessDto smtProcessListProcessDto = smtProcessListProcessDtoList.get(0);
                if(smtProcessListProcessDto.getStatus()==2 ){
                    continue;
                }
                BeanUtils.autoFillEqFields(smtProcessListProcessDto,smtProcessListProcess);
                double outputQuantity = StringUtils.isEmpty(smtProcessListProcess.getOutputQuantity())?0.0:smtProcessListProcess.getOutputQuantity().doubleValue();
                if((outputQuantity+outPutQty)>smtWorkOrder.getWorkOrderQuantity().doubleValue()){
                    throw new BizErrorException("报工数不允许大于工单生产数："+smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
                }
                smtProcessListProcess.setOutputQuantity(new BigDecimal(outputQuantity+outPutQty));
                smtProcessListProcess.setOutboundTime(date);
                smtProcessListProcessService.update(smtProcessListProcess);

            }else{
                if(outPutQty>smtWorkOrder.getWorkOrderQuantity().doubleValue()){
                    throw new BizErrorException("报工数不允许大于工单生产数："+smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
                }
                smtProcessListProcess.setProcessListProcessCode(CodeUtils.getId("SPLP"));
                smtProcessListProcess.setWorkOrderCardPoolId(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
                smtProcessListProcess.setProcessId(curProcessId);
                smtProcessListProcess.setProcessType((byte)2);
                smtProcessListProcess.setStatus((byte)2);
                smtProcessListProcess.setInboundTime(date);
                smtProcessListProcess.setOutboundTime(date);
                smtProcessListProcess.setOutputQuantity(new BigDecimal(outPutQty));
                smtProcessListProcess.setCurOutputQty(new BigDecimal(outPutQty));
                smtProcessListProcessMapper.insertSelective(smtProcessListProcess);
            }
            SmtWorkOrderCardPool smtWorkOrderCardPool = new SmtWorkOrderCardPool();
            smtWorkOrderCardPool.setWorkOrderCardPoolId(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
            smtWorkOrderCardPool.setCardStatus((byte)1);
            smtWorkOrderCardPoolList.add(smtWorkOrderCardPool);
        }
        mesPmExplainProcessPlan.setActualStartDate(date);
        mesPmExplainProcessPlan.setActualEndDate(date);
        mesPmExplainProcessPlan.setFinishedQty(new BigDecimal(smtWorkOrderCardPoolDtoList.size()*outPutQty));
        mesPmExplainProcessPlanService.update(mesPmExplainProcessPlan);
        if(StringUtils.isNotEmpty(smtWorkOrderCardPoolList)){
            smtWorkOrderCardPoolService.batchUpdateStatus(smtWorkOrderCardPoolList);
        }

        return 1;
    }

    @Override
    public SmtProcess findSmtProcess(Long id) {
        return smtProcessListProcessMapper.findSmtProcess(id);
    }

    @Override
    public double findMinOutPut(Long workOrderCardPoolId) {
        return smtProcessListProcessMapper.findMinOutPut(workOrderCardPoolId);
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
