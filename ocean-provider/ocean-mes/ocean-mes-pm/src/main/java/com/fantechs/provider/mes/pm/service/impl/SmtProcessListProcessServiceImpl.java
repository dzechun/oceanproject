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
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.*;
import com.fantechs.provider.mes.pm.utils.BarcodeRuleUtils;
import com.fantechs.provider.mes.pm.service.SmtProcessListProcessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
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
    private SmtWorkOrderMapper smtWorkOrderMapper;
    @Resource
    private SmtBarcodeRuleSpecMapper smtBarcodeRuleSpecMapper;
    @Resource
    private SmtBarcodeRuleSetDetMapper smtBarcodeRuleSetDetMapper;
    @Resource
    private SmtBarcodeRuleMapper smtBarcodeRuleMapper;


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
    public int finishedProduct(ProcessFinishedProductDTO processFinishedProductDTO) {
        List<SmtProcessListProcess> smtProcessListProcesses = this.selectAll(ControllerUtil.dynamicCondition(
                "workOrderCardPoolId", processFinishedProductDTO.getWorkOrderCardPoolId(),
                "processId", processFinishedProductDTO.getProcessId()));
        if(StringUtils.isEmpty(smtProcessListProcesses)){
            SmtProcessListProcess smtProcessListProcess = new SmtProcessListProcess();
            smtProcessListProcess.setStatus((byte)processFinishedProductDTO.getOperation());
            smtProcessListProcess.setWorkOrderCardPoolId(processFinishedProductDTO.getWorkOrderCardPoolId());
            smtProcessListProcess.setProcessId(processFinishedProductDTO.getProcessId());
            smtProcessListProcess.setOutputQuantity(processFinishedProductDTO.getCurOutputQty());
            smtProcessListProcess.setCurOutputQty(processFinishedProductDTO.getCurOutputQty());
            smtProcessListProcess.setProcessType(processFinishedProductDTO.getProcessType());
            if(this.saveOBJ(smtProcessListProcess)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }else{
            SmtProcessListProcess smtProcessListProcess = smtProcessListProcesses.get(0);
            if(smtProcessListProcess.getStatus()==2){
                throw new BizErrorException("当前流程卡你已提交，请勿提交");
            }
            smtProcessListProcess.setStatus((byte)processFinishedProductDTO.getOperation());
            smtProcessListProcess.setOutputQuantity(new BigDecimal(smtProcessListProcess.getOutputQuantity().doubleValue()+processFinishedProductDTO.getCurOutputQty().doubleValue()));
            smtProcessListProcess.setCurOutputQty(processFinishedProductDTO.getCurOutputQty());
            smtProcessListProcess.setProcessType(processFinishedProductDTO.getProcessType());
            if(this.update(smtProcessListProcess)<=0){
                throw new BizErrorException(ErrorCodeEnum.OPT20012006);
            }
        }
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
