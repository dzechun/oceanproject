package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.mes.pm.ProcessListDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtProcessListProcessDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtProcessListProcess;
import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.mes.pm.*;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.*;
import com.fantechs.provider.mes.pm.utils.BarcodeRuleUtils;
import com.fantechs.provider.mes.pm.service.SmtProcessListProcessService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Override
    public List<SmtProcessListProcessDto> findList(SearchSmtProcessListProcess searchSmtProcessListProcess) {
        return smtProcessListProcessMapper.findList(searchSmtProcessListProcess);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int startJob(SmtWorkOrderBarcodePool smtWorkOrderBarcodePool) {
        List<SmtProcessListProcess> list=new ArrayList<>();
        Long workOrderId = smtWorkOrderBarcodePool.getWorkOrderId();
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
                //彩盒号
                String packageNum=generateCode(barcodeRuleId);
                smtProcessListProcess.setPackageNum(packageNum);
                //箱号
                //栈板号
                smtProcessListProcess.setIsDelete((byte) 1);
            }
        }else {
            throw new BizErrorException(ErrorCodeEnum.OPT20012003);
        }
        return smtProcessListProcessMapper.insertList(list);
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
}
