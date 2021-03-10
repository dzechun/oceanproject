package com.fantechs.provider.mes.pm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.basic.SmtProcess;
import com.fantechs.common.base.general.dto.mes.pm.*;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmMatchingOrder;
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
import com.fantechs.common.base.general.entity.qms.QmsQualityConfirmation;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsQualityConfirmation;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
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
import java.util.stream.Collectors;

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
    private MesPmMatchingOrderService mesPmMatchingOrderService;
    @Resource
    private OutFeignApi outFeignApi;
    @Resource
    private QmsFeignApi qmsFeignApi;
    @Resource
    private BasicFeignApi basicFeignApi;


    @Override
    public List<SmtProcessListProcessDto> findList(SearchSmtProcessListProcess searchSmtProcessListProcess) {
        List<SmtProcessListProcessDto> smtProcessListProcessDtoList = smtProcessListProcessMapper.findList(searchSmtProcessListProcess);
        if(StringUtils.isNotEmpty(smtProcessListProcessDtoList)){
            for (SmtProcessListProcessDto smtProcessListProcessDto : smtProcessListProcessDtoList) {
                if(StringUtils.isEmpty(smtProcessListProcessDto.getMaterialCode())){
                    MaterialAndPartsDTO materialAndPartsDTO = this.findPartsInformation(smtProcessListProcessDto.getWorkOrderCardPoolId());
                    BeanUtils.autoFillEqFields(materialAndPartsDTO,smtProcessListProcessDto);
                }
            }
        }
        return smtProcessListProcessDtoList;
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

        //=====取出当前工序的上工序
        ResponseEntity<List<SmtRouteProcess>> result = basicFeignApi.findConfigureRout(smtWorkOrder.getRouteId());
        if(result.getCode()!=0){
            throw new BizErrorException(result.getMessage());
        }
        List<SmtRouteProcess> preProcessList = result.getData();
        Long preProcessId=null;
        for (int i = 0; i < preProcessList.size(); i++) {
            SmtRouteProcess temp = preProcessList.get(i);
            if(temp.getProcessId()==processFinishedProductDTO.getProcessId() && i>0){

                //====找到上一个必过的工序
                int tempI=i;
                while (tempI>0){
                    tempI--;
                    if(preProcessList.get(tempI).getIsMustPass()==0){
                        continue;
                    }
                    preProcessId=preProcessList.get(tempI).getProcessId();
                    break;
                }
                //====
                break;
            }
        }
        //=====

        MesPmExplainProcessPlan mesPmExplainProcessPlan = new MesPmExplainProcessPlan();
        mesPmExplainProcessPlan.setExplainProcessPlanId(mesPmExplainProcessPlanDTO.getExplainProcessPlanId());
        //=====


        if(processFinishedProductDTO.getPutInto()==1){
            //开料
            this.outPut(processFinishedProductDTO,mesPmExplainPlan);
            mesPmExplainProcessPlan.setActualStartDate(new Date());
            mesPmExplainProcessPlanService.update(mesPmExplainProcessPlan);
            return 1;
        }

        if(processFinishedProductDTO.getProcessType()==1) {
            if(StringUtils.isEmpty(smtWorkOrderCardPool.getParentId()) || smtWorkOrderCardPool.getParentId()==0){
                //工单流程卡开工确认
                workOrderStartWork(preProcessId,processFinishedProductDTO);
                return 1;
            }
            this.startWork(preProcessId,smtWorkOrder,processFinishedProductDTO,false);
        }else if(processFinishedProductDTO.getProcessType()==2){
            if(StringUtils.isEmpty(smtWorkOrderCardPool.getParentId()) || smtWorkOrderCardPool.getParentId()==0){
                //工单流程卡报工
                workOrderCarReport(smtWorkOrder.getRouteId(),preProcessId,processFinishedProductDTO);
                return 1;
            }
            this.finishWork(smtWorkOrder.getRouteId(),preProcessId,processFinishedProductDTO,false);
        }
        //以下注释暂时保留，功能测试完毕可删除
        /*if(StringUtils.isEmpty(smtWorkOrderCardPool.getType()) || smtWorkOrderCardPool.getType()!=3){
            if(processFinishedProductDTO.getProcessType()==1) {
                if(StringUtils.isEmpty(smtWorkOrderCardPool.getParentId()) || smtWorkOrderCardPool.getParentId()==0){
                    //工单流程卡开工确认
                    workOrderStartWork(preProcessId,processFinishedProductDTO);
                    return 1;
                }
                this.startWork(preProcessId,smtWorkOrder,processFinishedProductDTO,false,false);
            }else if(processFinishedProductDTO.getProcessType()==2){
                if(StringUtils.isEmpty(smtWorkOrderCardPool.getParentId()) || smtWorkOrderCardPool.getParentId()==0){
                    //工单流程卡报工
                    workOrderCarReport(smtWorkOrder.getRouteId(),preProcessId,processFinishedProductDTO);
                    return 1;
                }
                this.finishWork(smtWorkOrder.getRouteId(),preProcessId,processFinishedProductDTO,false);
            }
        }
        else if(StringUtils.isNotEmpty(smtWorkOrderCardPool.getType()) && smtWorkOrderCardPool.getType() == 3){
            //当前扫描的流程卡为报工退回的流程卡，只能走报工，且报工数不能大于自身的开工数
            List<SmtProcessListProcess> tempProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                    "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                    "processId", processFinishedProductDTO.getProcessId(),
                    "processType",2,
                    "status",2));
            BigDecimal startWorkQtyTotal=new BigDecimal(0);
            BigDecimal outPutQtyTotal=processFinishedProductDTO.getCurOutputQty();
            if(StringUtils.isNotEmpty(tempProcessListProcesseList)){
                for (SmtProcessListProcess smtProcessListProcess : tempProcessListProcesseList) {
                    outPutQtyTotal=outPutQtyTotal.add(smtProcessListProcess.getOutputQuantity());
                }
            }
            tempProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                    "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                    "processId", processFinishedProductDTO.getProcessId(),
                    "processType",2,
                    "status",1));
            if(StringUtils.isNotEmpty(tempProcessListProcesseList)){
                for (SmtProcessListProcess smtProcessListProcess : tempProcessListProcesseList) {
                    startWorkQtyTotal=smtProcessListProcess.getStartWorkQty();
                }
            }
            if(outPutQtyTotal.compareTo(startWorkQtyTotal)>0){
                throw new BizErrorException("报工数不能大于退回数");
            }
            SmtProcessListProcess curProcessListProcesse=new SmtProcessListProcess();
            curProcessListProcesse.setStartWorkQty(startWorkQtyTotal);
            curProcessListProcesse.setStaffId(processFinishedProductDTO.getStaffId());
            curProcessListProcesse.setCurOutputQty(processFinishedProductDTO.getCurOutputQty());
            curProcessListProcesse.setProcessType(processFinishedProductDTO.getProcessType());
            curProcessListProcesse.setStatus((processFinishedProductDTO.getOperation()));
            Date date = new Date();
            if(processFinishedProductDTO.getOperation()==2){
                curProcessListProcesse.setOutboundTime(date);
                curProcessListProcesse.setOutputQuantity(outPutQtyTotal);
            }
            //=====是否存在保存进行中的数据
            tempProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                    "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                    "processId", processFinishedProductDTO.getProcessId(),
                    "processType",1,
                    "status",1));
            if(StringUtils.isNotEmpty(tempProcessListProcesseList)){
                SmtProcessListProcess smtProcessListProcess = tempProcessListProcesseList.get(0);
                curProcessListProcesse.setProcessListProcessId(smtProcessListProcess.getProcessListProcessId());
                this.update(curProcessListProcesse);
            }else{
                curProcessListProcesse.setWorkOrderCardPoolId(processFinishedProductDTO.getWorkOrderCardPoolId());
                curProcessListProcesse.setProcessId(processFinishedProductDTO.getProcessId());
                curProcessListProcesse.setInboundTime(date);
                this.saveOBJ(curProcessListProcesse);
            }
            //=====
        }*/


        //=====执行计划与工序的数据回写
        if(processFinishedProductDTO.getProcessType()==2 && processFinishedProductDTO.getOperation()==2){
            if(StringUtils.isEmpty(mesPmExplainProcessPlan.getFinishedQty())){
                mesPmExplainProcessPlan.setFinishedQty(processFinishedProductDTO.getCurOutputQty());
            }else{
                mesPmExplainProcessPlan.setFinishedQty(mesPmExplainProcessPlan.getFinishedQty().add(processFinishedProductDTO.getCurOutputQty()));
            }
        }
        if(processFinishedProductDTO.getProcessType()==2 && processFinishedProductDTO.getOperation()==2){
            mesPmExplainProcessPlan.setActualEndDate(new Date());
        }
        mesPmExplainProcessPlanService.update(mesPmExplainProcessPlan);
        if(StringUtils.isEmpty(mesPmExplainPlan.getFinishedQty())){
            mesPmExplainPlan.setFinishedQty(processFinishedProductDTO.getCurOutputQty());
        }else{
            mesPmExplainPlan.setFinishedQty(mesPmExplainPlan.getFinishedQty().add(processFinishedProductDTO.getCurOutputQty()));
        }
        mesPmExplainPlanService.update(mesPmExplainPlan);
        //=====
        return 1;
    }

    /**
     * 投产开料
     * @param processFinishedProductDTO
     * @param mesPmExplainPlan
     * @return
     */
    private int outPut(ProcessFinishedProductDTO processFinishedProductDTO,MesPmExplainPlan mesPmExplainPlan){
        SysUser sysUser = this.currentUser();
        SmtWorkOrderCardPool smtWorkOrderCardPool = smtWorkOrderCardPoolService.selectByKey(processFinishedProductDTO.getWorkOrderCardPoolId());
        if(StringUtils.isEmpty(smtWorkOrderCardPool)){
            throw new BizErrorException("未找到对应流程卡");
        }
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

        //====将所有生成的部件流程卡过站开工确认
        SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
        searchSmtWorkOrderCardPool.setParentId(smtWorkOrderCardPool.getWorkOrderCardPoolId());
        searchSmtWorkOrderCardPool.setCardStatus((byte)0);
        List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolDtoList = smtWorkOrderCardPoolService.findList(searchSmtWorkOrderCardPool);
        if(StringUtils.isEmpty(smtWorkOrderCardPoolDtoList)){
            throw new BizErrorException("开料失败");
        }


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
            smtProcessListProcess.setStartWorkQty(smtWorkOrderCardPoolDto.getOutPutQty());
            smtProcessListProcess.setOutputQuantity(new BigDecimal(0));
            smtProcessListProcess.setCurOutputQty(smtWorkOrderCardPoolDto.getOutPutQty());
            //批量保存不会判断属性空字段，会覆盖数据库默认数据，以下是设置默认数据
            smtProcessListProcess.setIsDelete((byte)1);
            smtProcessListProcess.setCreateTime(date);
            smtProcessListProcess.setCreateUserId(sysUser.getUserId());
            smtProcessListProcess.setModifiedTime(date);
            smtProcessListProcess.setIsHold((byte)0);
            smtProcessListProcess.setStaffId(processFinishedProductDTO.getStaffId());
            smtProcessListProcessList.add(smtProcessListProcess);
            SmtWorkOrderCardPool smtWorkOrderCardPool1 = new SmtWorkOrderCardPool();
            smtWorkOrderCardPool1.setWorkOrderCardPoolId(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
            smtWorkOrderCardPool1.setCardStatus((byte)3);
            smtWorkOrderCardPool1.setType((byte)2);
            smtWorkOrderCardPoolList.add(smtWorkOrderCardPool1);
        }
        smtWorkOrderCardPool.setCardStatus((byte)1);//工单流转卡投产中
        smtWorkOrderCardPoolService.update(smtWorkOrderCardPool);
        smtWorkOrderCardPoolService.batchUpdateStatus(smtWorkOrderCardPoolList);
        smtProcessListProcessService.batchSave(smtProcessListProcessList);
        return 1;
    }

    /**
     * 开工确认
     * @param preProcessId
     * @param smtWorkOrder
     * @param processFinishedProductDTO
     * @return
     */
    private int startWork(Long preProcessId,SmtWorkOrder smtWorkOrder,ProcessFinishedProductDTO processFinishedProductDTO,boolean startRemain){
        Long curProcessId=processFinishedProductDTO.getProcessId();
        Long routeId=smtWorkOrder.getRouteId();
        Long firstProcessIdWS = smtProcessListProcessMapper.firstProcessIdInWSection(curProcessId,routeId);
        Long firstProcessIdR = smtProcessListProcessMapper.firstProcessIdInRoute(routeId);

        //首工序不需要开工
        if(firstProcessIdR == curProcessId){
            throw new BizErrorException("开料工序不需要进行开工确认");
        }
        //非工段首工序不允许开工
        if(firstProcessIdWS != curProcessId){
            throw new BizErrorException("非工段首工序不允许开工");
        }
        //流程卡已开工，这个时候工序已经进行到下一工段，再进行开工作业，则需要取品质确认数
        //====查找品质确认数
        ResponseEntity<QmsQualityConfirmation> result = qmsFeignApi.getQualityQuantity(processFinishedProductDTO.getWorkOrderCardPoolId(), preProcessId);
        if(StringUtils.isEmpty(result) || result.getCode()!=0){
            throw new BizErrorException(result.getMessage());
        }
        QmsQualityConfirmation qualityConfirmation = result.getData();
        if(StringUtils.isEmpty(qualityConfirmation)){
            if(startRemain){
                //如果是工单流程卡开工，又找不到品质确认数，直接跳过。因为可能此部件流程卡还尚未开始做等等原因
                return 1;
            }
            throw new BizErrorException("未找到品质确认数据");
        }
        BigDecimal okQty=qualityConfirmation.getTotalQualified();
        if(StringUtils.isEmpty(okQty) || okQty.doubleValue()<=0){
            throw new BizErrorException("未品质确认，不允许开工");
        }
        //====
        BigDecimal startWorkQtyTotal=processFinishedProductDTO.getCurOutputQty();
        BigDecimal curStartWorkQty=processFinishedProductDTO.getCurOutputQty();
        List<SmtProcessListProcess> tempProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                "processId", curProcessId,
                "processType",1,
                "status",2));
        if(StringUtils.isNotEmpty(tempProcessListProcesseList)){
            for (SmtProcessListProcess smtProcessListProcess : tempProcessListProcesseList) {
                startWorkQtyTotal=startWorkQtyTotal.add(smtProcessListProcess.getCurOutputQty());
            }
        }
        if(startRemain){
            //====本次开工数如果大于剩余开工数，则剩余开工数一次性全部报，如果小于剩余开工数，则正常进行
            startWorkQtyTotal=startWorkQtyTotal.subtract(processFinishedProductDTO.getCurOutputQty());
            BigDecimal remainQty=okQty.subtract(startWorkQtyTotal);
            if(remainQty.doubleValue()==0){
                return 1;
            }
            if(remainQty.compareTo(processFinishedProductDTO.getCurOutputQty())>=0){
                startWorkQtyTotal=startWorkQtyTotal.add(processFinishedProductDTO.getCurOutputQty());
                processFinishedProductDTO.setCurOutputQty(new BigDecimal(0));
            }else if(remainQty.compareTo(processFinishedProductDTO.getCurOutputQty())<0){
                startWorkQtyTotal=startWorkQtyTotal.add(remainQty);
                curStartWorkQty=remainQty;
                processFinishedProductDTO.setCurOutputQty(processFinishedProductDTO.getCurOutputQty().subtract(remainQty));
            }

        }
        if(startWorkQtyTotal.compareTo(okQty)>0){
            throw new BizErrorException("开工确认数不能大于品质确认数:"+okQty);
        }
        SmtProcessListProcess curProcessListProcesse=new SmtProcessListProcess();
        curProcessListProcesse.setStaffId(processFinishedProductDTO.getStaffId());
        curProcessListProcesse.setCurOutputQty(curStartWorkQty);
        curProcessListProcesse.setProcessType(processFinishedProductDTO.getProcessType());
        curProcessListProcesse.setStatus((processFinishedProductDTO.getOperation()));
        Date date = new Date();
        if(processFinishedProductDTO.getOperation()==2){
            curProcessListProcesse.setStartWorkQty(startWorkQtyTotal);
            curProcessListProcesse.setOutboundTime(date);
        }
        //=====是否存在保存进行中的数据
        tempProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                "processId", curProcessId,
                "processType",1,
                "status",1));
        if(StringUtils.isNotEmpty(tempProcessListProcesseList)){
            SmtProcessListProcess smtProcessListProcess = tempProcessListProcesseList.get(0);
            curProcessListProcesse.setProcessListProcessId(smtProcessListProcess.getProcessListProcessId());
            this.update(curProcessListProcesse);
        }else{
            curProcessListProcesse.setWorkOrderCardPoolId(processFinishedProductDTO.getWorkOrderCardPoolId());
            curProcessListProcesse.setProcessId(processFinishedProductDTO.getProcessId());
            curProcessListProcesse.setInboundTime(date);
            this.saveOBJ(curProcessListProcesse);
        }
        //=====

        if(processFinishedProductDTO.getProcessType()==1 && processFinishedProductDTO.getOperation()==2){
            if(processFinishedProductDTO.getProcessId() == firstProcessIdWS && processFinishedProductDTO.getProcessId() != firstProcessIdR)
                sendMaterial(preProcessId,smtWorkOrder.getWorkOrderId(),smtWorkOrder.getMaterialId(),curStartWorkQty.doubleValue());
        }
        return 1;
    }

    /**
     * 报工
     * @param routeId
     * @param preProcessId
     * @param processFinishedProductDTO
     * @param finishRemain 是否一次性完成剩余
     * @return
     */
    private int finishWork(Long routeId,Long preProcessId,ProcessFinishedProductDTO processFinishedProductDTO,boolean finishRemain){
        SysUser sysUser = this.currentUser();
        Long curProcessId=processFinishedProductDTO.getProcessId();
        List<SmtProcessListProcess> tempProcessListProcesseList ;
        SmtWorkOrderCardPool smtWorkOrderCardPool = smtWorkOrderCardPoolService.selectByKey(processFinishedProductDTO.getWorkOrderCardPoolId());
        if(StringUtils.isEmpty(smtWorkOrderCardPool)){
            throw new BizErrorException("未找到对应流程卡");
        }
        Long firstProcessIdWS = smtProcessListProcessMapper.firstProcessIdInWSection(curProcessId,routeId);
        Long firstProcessIdR = smtProcessListProcessMapper.firstProcessIdInRoute(routeId);

        //报工
        if(smtWorkOrderCardPool.getCardStatus() !=1 && firstProcessIdR != processFinishedProductDTO.getProcessId()){
            //如果状态为3，且是首工序的话是允许报工的，这个时候是因为首工序进行了开料再报工
            throw new BizErrorException("流程卡尚未开工，不允许报工");
        }
        BigDecimal startWorkQtyTotal=new BigDecimal(0);
        BigDecimal outPutQtyToal=processFinishedProductDTO.getCurOutputQty();
        BigDecimal curOutPutQty=processFinishedProductDTO.getCurOutputQty();

        SmtProcessListProcess preProcessListProcesse=null;//上一道报工工序记录
        SmtProcessListProcess curProcessListProcesse=new SmtProcessListProcess();//上一道报工工序记录
        //已报工完成的记录
        List<SmtProcessListProcess> finishProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                "processId", processFinishedProductDTO.getProcessId(),
                "processType",2,
                "status",2));
        if(StringUtils.isNotEmpty(finishProcessListProcesseList)){
            for (SmtProcessListProcess smtProcessListProcess : finishProcessListProcesseList) {
                outPutQtyToal=outPutQtyToal.add(smtProcessListProcess.getCurOutputQty());
            }
        }

        if(firstProcessIdR == processFinishedProductDTO.getProcessId() || firstProcessIdWS == processFinishedProductDTO.getProcessId()){
            //首工序，则它的报工数不能大于开工确认数
            tempProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                    "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                    "processId", processFinishedProductDTO.getProcessId(),
                    "processType",1,
                    "status",2));
            if(StringUtils.isEmpty(tempProcessListProcesseList)){
                if(finishRemain){
                    //如果是工单流程卡报工，需要过滤掉没有开工确认的流程卡，有可能该流程卡还尚未开始做
                    return 1;
                }
                throw new BizErrorException("该流程卡尚未开工确认");
            }
            for (SmtProcessListProcess smtProcessListProcess : tempProcessListProcesseList) {
                startWorkQtyTotal=startWorkQtyTotal.add(smtProcessListProcess.getCurOutputQty());
            }
            if(finishRemain){
                //====本次报工数如果大于剩余报工数，则剩余报工数一次性全部报，如果小于剩余报工数，则正常进行
                outPutQtyToal=outPutQtyToal.subtract(processFinishedProductDTO.getCurOutputQty());
                BigDecimal remainQty=startWorkQtyTotal.subtract(outPutQtyToal);
                if(remainQty.doubleValue()==0){
                    return 1;
                }
                if(remainQty.compareTo(processFinishedProductDTO.getCurOutputQty())>=0){
                    outPutQtyToal=outPutQtyToal.add(processFinishedProductDTO.getCurOutputQty());
                    processFinishedProductDTO.setCurOutputQty(new BigDecimal(0));
                }else if(remainQty.compareTo(processFinishedProductDTO.getCurOutputQty())<0){
                    outPutQtyToal=outPutQtyToal.add(remainQty);
                    curOutPutQty=remainQty;
                    processFinishedProductDTO.setCurOutputQty(processFinishedProductDTO.getCurOutputQty().subtract(remainQty));
                }

            }
            if(outPutQtyToal.compareTo(startWorkQtyTotal)>0){
                throw new BizErrorException("首工序报工不能大于开工数");
            }
            if(firstProcessIdR == processFinishedProductDTO.getProcessId()){
                //首工序进行报工，将流程卡状态从开料调整为投产中
                smtWorkOrderCardPool.setCardStatus((byte)1);
                smtWorkOrderCardPool.setModifiedUserId(sysUser.getUserId());
                smtWorkOrderCardPoolService.update(smtWorkOrderCardPool);
            }
        }else{
            //上工序已报工完成的记录
            if(StringUtils.isEmpty(preProcessId)){
                if(finishRemain){
                    //如果是工单流程卡报工，需要过滤掉没有开工确认的流程卡，有可能该流程卡还尚未开始做
                    return 1;
                }
                throw new BizErrorException("上工序未进行报工："+processFinishedProductDTO.getWorkOrderCardPoolId());
            }
            List<SmtProcessListProcess> preProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                    "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                    "processId", preProcessId,
                    "processType",2,
                    "status",2));
            if(StringUtils.isEmpty(preProcessListProcesseList)){
                if(StringUtils.isNotEmpty(smtWorkOrderCardPool.getType()) && smtWorkOrderCardPool.getType()==3){
                    //流程卡属于拆批流程卡，又未找到上工序的报工情况，那判断为当前流程卡拆批后首次扫描，那么不能大于自身开工数（与拆批程序协同完成）
                    tempProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                            "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                            "processId", processFinishedProductDTO.getProcessId(),
                            "processType",2,
                            "status",1));
                    if(StringUtils.isNotEmpty(tempProcessListProcesseList)){
                        for (SmtProcessListProcess smtProcessListProcess : tempProcessListProcesseList) {
                            startWorkQtyTotal=smtProcessListProcess.getStartWorkQty();
                        }
                        preProcessListProcesse=new SmtProcessListProcess();
                        preProcessListProcesse.setOutputQuantity(startWorkQtyTotal);
                    }else{
                        throw new BizErrorException("拆批数据不正确");
                    }
                }else{
                    if(finishRemain){
                        //如果是工单流程卡报工，需要过滤掉没有开工确认的流程卡，有可能该流程卡还尚未开始做
                        return 1;
                    }
                    throw new BizErrorException("上工序未进行报工："+processFinishedProductDTO.getWorkOrderCardPoolId());
                }
            }else{
                preProcessListProcesse=preProcessListProcesseList.get(preProcessListProcesseList.size()-1);
            }

            if(finishRemain){
                //====本次报工数如果大于剩余报工数，则剩余报工数一次性全部报，如果小于剩余报工数，则正常进行
                outPutQtyToal=outPutQtyToal.subtract(processFinishedProductDTO.getCurOutputQty());
                BigDecimal remainQty=preProcessListProcesse.getOutputQuantity().subtract(outPutQtyToal);
                if(remainQty.doubleValue()==0){
                    return 1;
                }
                if(remainQty.compareTo(processFinishedProductDTO.getCurOutputQty())>=0){
                    outPutQtyToal=outPutQtyToal.add(processFinishedProductDTO.getCurOutputQty());
                    processFinishedProductDTO.setCurOutputQty(new BigDecimal(0));
                }else if(remainQty.compareTo(processFinishedProductDTO.getCurOutputQty())<0){
                    outPutQtyToal=outPutQtyToal.add(remainQty);
                    curOutPutQty=remainQty;
                    processFinishedProductDTO.setCurOutputQty(processFinishedProductDTO.getCurOutputQty().subtract(remainQty));
                }

            }
            //中间工序报工，报工数不能大于上工序报工数
            if(outPutQtyToal.compareTo(preProcessListProcesse.getOutputQuantity())>0){
                throw new BizErrorException("工序报工不能大于上工序报工数");
            }
        }

        curProcessListProcesse.setStaffId(processFinishedProductDTO.getStaffId());
        curProcessListProcesse.setCurOutputQty(curOutPutQty);
        curProcessListProcesse.setProcessType(processFinishedProductDTO.getProcessType());
        curProcessListProcesse.setStatus((processFinishedProductDTO.getOperation()));
        Date date = new Date();
        if(processFinishedProductDTO.getOperation()==2){
            curProcessListProcesse.setOutputQuantity(outPutQtyToal);
            curProcessListProcesse.setOutboundTime(date);
        }
        //=====是否存在保存进行中的数据
        tempProcessListProcesseList = this.selectAll(ControllerUtil.dynamicCondition(
                "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                "processId", curProcessId,
                "processType",2,
                "status",1));
        if(StringUtils.isNotEmpty(tempProcessListProcesseList)){
            SmtProcessListProcess smtProcessListProcess = tempProcessListProcesseList.get(0);
            curProcessListProcesse.setProcessListProcessId(smtProcessListProcess.getProcessListProcessId());
            this.update(curProcessListProcesse);
        }else{
            curProcessListProcesse.setWorkOrderCardPoolId(processFinishedProductDTO.getWorkOrderCardPoolId());
            curProcessListProcesse.setProcessId(processFinishedProductDTO.getProcessId());
            curProcessListProcesse.setInboundTime(date);
            this.saveOBJ(curProcessListProcesse);
        }
        //=====
        return 1;
    }

    /**
     *  反写发料计划
     * @param workOrderId
     * @param materialId
     * @param outPutQty
     * @return
     */
    private int sendMaterial(Long preProcessId,Long workOrderId,Long materialId,double outPutQty){
        //工序开工需要进行发料计划实发数反写
        WmsOutProductionMaterial wmsOutProductionMaterial = new WmsOutProductionMaterial();
        wmsOutProductionMaterial.setProcessId(preProcessId);
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
     * 工单开工确认
     * @param preProcessId
     * @param processFinishedProductDTO
     * @return
     */
    private int workOrderStartWork(Long preProcessId,ProcessFinishedProductDTO processFinishedProductDTO){
        //=====工单流程卡开工，判断当前工序是否属于工艺路线下最后一个工段
        SmtWorkOrderCardPool smtWorkOrderCardPool = smtWorkOrderCardPoolService.selectByKey(processFinishedProductDTO.getWorkOrderCardPoolId());
        SmtWorkOrder smtWorkOrder = smtWorkOrderService.selectByKey(smtWorkOrderCardPool.getWorkOrderId());
        int resultCount = smtProcessListProcessMapper.isLastSectionInRoute(smtWorkOrder.getRouteId(), processFinishedProductDTO.getProcessId());
        if(resultCount>0){
            SearchMesPmMatchingOrder searchMesPmMatchingOrder = new SearchMesPmMatchingOrder();
            searchMesPmMatchingOrder.setWorkOrderId(smtWorkOrder.getWorkOrderId());
            List<MesPmMatchingOrderDto> mesPmMatchingOrderDtoList = mesPmMatchingOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchMesPmMatchingOrder));
            if(StringUtils.isEmpty(mesPmMatchingOrderDtoList)){
                throw new BizErrorException("未找到配套数");
            }
            MesPmMatchingOrderDto mesPmMatchingOrderDto = mesPmMatchingOrderDtoList.get(0);
            if(processFinishedProductDTO.getCurOutputQty().compareTo(mesPmMatchingOrderDto.getAlreadyMatchingQuantity())>0){
                throw new BizErrorException("工单流程卡开工数不能大于已配套数");
            }
        }
        //=====

        //工单流程卡开工确认即对所有的部件流程卡进行开工确认
        SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
        searchSmtWorkOrderCardPool.setParentId(processFinishedProductDTO.getWorkOrderCardPoolId());
        List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolDtoList = smtWorkOrderCardPoolService.findList(searchSmtWorkOrderCardPool);
        if(StringUtils.isEmpty(smtWorkOrderCardPoolDtoList)){
            throw new BizErrorException("工单流转卡未找到部件流转卡");
        }
        //将找到的所有部件流程卡按照部件工单进行一个分类
        Map<Long, List<SmtWorkOrderCardPoolDto>> map = smtWorkOrderCardPoolDtoList.stream().collect(Collectors.groupingBy(SmtWorkOrderCardPool::getWorkOrderId));
        Set<Long> longSet = map.keySet();
        int result=0;//进行过报工的流程卡
        for (Long workOrderId : longSet) {
            List<SmtWorkOrderCardPoolDto> temp = map.get(workOrderId);
            //根据工单找到部件的用量
            BigDecimal quantity = smtProcessListProcessMapper.getQuantityByWorkOrderId(workOrderId);
            BigDecimal remainQty=processFinishedProductDTO.getCurOutputQty().multiply(quantity);
            for (SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto : temp) {
                smtWorkOrder = smtWorkOrderService.selectByKey(smtWorkOrderCardPoolDto.getWorkOrderId());
                ProcessFinishedProductDTO tempProcessFinishedProductDTO = new ProcessFinishedProductDTO();
                BeanUtils.autoFillEqFields(processFinishedProductDTO,tempProcessFinishedProductDTO);
                tempProcessFinishedProductDTO.setWorkOrderCardPoolId(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
                tempProcessFinishedProductDTO.setCurOutputQty(remainQty);
                this.startWork(preProcessId,smtWorkOrder,tempProcessFinishedProductDTO,true);
                remainQty=tempProcessFinishedProductDTO.getCurOutputQty();
                if(remainQty.doubleValue()==0){
                    result++;
                    break;
                }
            }

            //如果剩余数量变量，且不等于0，说明超开工了。如果是等于开工数，则认为该流程卡已经全部完成，跳过
            if(remainQty.doubleValue()>0){
                //当前流程卡全部报工完成后还有剩余，说明此次工单报工超出了
                throw new BizErrorException("当前部件开工超开工："+remainQty.doubleValue()+"，工单："+workOrderId);
            }
        }
        if(result==0){
            throw new BizErrorException("所有部件流程卡已经开工完成");
        }
        return 1;
    }

    /**
     * 工单流程卡报工
     * @param routeId
     * @param preProcessId
     * @param processFinishedProductDTO
     * @return
     */
    private int workOrderCarReport(Long routeId,Long preProcessId,ProcessFinishedProductDTO processFinishedProductDTO){
        //工单流程卡报工即对所有的部件流程卡进行报工
        SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool = new SearchSmtWorkOrderCardPool();
        searchSmtWorkOrderCardPool.setParentId(processFinishedProductDTO.getWorkOrderCardPoolId());
        List<SmtWorkOrderCardPoolDto> smtWorkOrderCardPoolDtoList = smtWorkOrderCardPoolService.findList(searchSmtWorkOrderCardPool);
        if(StringUtils.isEmpty(smtWorkOrderCardPoolDtoList)){
            throw new BizErrorException("工单流转卡未找到部件流转卡");
        }
        //将找到的所有部件流程卡按照部件工单进行一个分类
        Map<Long, List<SmtWorkOrderCardPoolDto>> map = smtWorkOrderCardPoolDtoList.stream().collect(Collectors.groupingBy(SmtWorkOrderCardPool::getWorkOrderId));
        Set<Long> longSet = map.keySet();
        int result=0;//进行过报工的流程卡
        for (Long workOrderId : longSet) {
            List<SmtWorkOrderCardPoolDto> temp = map.get(workOrderId);
            //根据工单找到部件的用量
            BigDecimal quantity = smtProcessListProcessMapper.getQuantityByWorkOrderId(workOrderId);
            if(StringUtils.isEmpty(quantity) || quantity.doubleValue()==0){
                throw new BizErrorException("当前工单部件用量错误："+workOrderId);
            }
            BigDecimal remainQty=processFinishedProductDTO.getCurOutputQty().multiply(quantity);
            //====计算当前部件的报工总数是否超出已开料部件的开料总数
            BigDecimal startQtyTotal=new BigDecimal(0);
            for (SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto : temp) {
                startQtyTotal=startQtyTotal.add(smtWorkOrderCardPoolDto.getOutPutQty());
            }
            if(remainQty.compareTo(startQtyTotal)>0){
                throw new BizErrorException("当前部件流程卡报工总数超出开料总数："+workOrderId);
            }
            //====

            for (SmtWorkOrderCardPoolDto smtWorkOrderCardPoolDto : temp) {
                ProcessFinishedProductDTO tempProcessFinishedProductDTO = new ProcessFinishedProductDTO();
                BeanUtils.autoFillEqFields(processFinishedProductDTO,tempProcessFinishedProductDTO);
                tempProcessFinishedProductDTO.setWorkOrderCardPoolId(smtWorkOrderCardPoolDto.getWorkOrderCardPoolId());
                tempProcessFinishedProductDTO.setCurOutputQty(remainQty);
                this.finishWork(routeId,preProcessId, tempProcessFinishedProductDTO,true);
                remainQty=tempProcessFinishedProductDTO.getCurOutputQty();
                if(remainQty.doubleValue()==0){
                    result++;
                    break;
                }
            }

            //如果剩余数量变量，且不等于0，说明超报工了。如果是等于报工数，则认为该流程卡已经全部完成，跳过
            if(remainQty.doubleValue()>0){
                //当前流程卡全部报工完成后还有剩余，说明此次工单报工超出了
                throw new BizErrorException("当前部件报工超报工："+remainQty.doubleValue()+"，工单："+workOrderId);
            }
        }
        if(result==0){
            throw new BizErrorException("所有部件流程卡已经报工完成");
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

    @Override
    public MaterialAndPartsDTO findPartsInformation(Long workOrderCardPoolId) {
        return smtProcessListProcessMapper.findPartsInformation(workOrderCardPoolId);
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
