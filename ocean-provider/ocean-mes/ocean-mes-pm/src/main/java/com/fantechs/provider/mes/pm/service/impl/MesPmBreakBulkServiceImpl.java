package com.fantechs.provider.mes.pm.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.MesPmBreakBulkDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmBreakBulkPrintDto;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmBreakBulk;
import com.fantechs.common.base.general.entity.mes.pm.*;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.pm.mapper.*;
import com.fantechs.provider.mes.pm.service.MesPmBreakBulkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mr.lei
 * @date 2021-01-18 11:30:42
 */
@Service
public class MesPmBreakBulkServiceImpl extends BaseService<MesPmBreakBulk> implements MesPmBreakBulkService {

    @Resource
    private MesPmBreakBulkMapper mesPmBreakBulkMapper;
    @Resource
    private MesPmBreakBulkDetMapper mesPmBreakBulkDetMapper;
    @Resource
    private SmtWorkOrderMapper smtWorkOrderMapper;
    @Resource
    private SmtWorkOrderCardPoolMapper smtWorkOrderCardPoolMapper;
    @Resource
    private SmtProcessListProcessMapper smtProcessListProcessMapper;

    @Override
    public List<MesPmBreakBulkDto> findList(SearchMesPmBreakBulk searchMesPmBreakBulk) {
        List<MesPmBreakBulkDto> list = mesPmBreakBulkMapper.findList(searchMesPmBreakBulk);
        list.forEach(li->{
            Example example = new Example(MesPmBreakBulkDet.class);
            example.createCriteria().andEqualTo("breakBulkId",li.getBreakBulkId());
            List<MesPmBreakBulkDet> dtoList = mesPmBreakBulkDetMapper.selectByExample(example);
            li.setModifiedTime(new Date());
            li.setMesPmBreakBulkDets(dtoList);
        });
        return list;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public MesPmBreakBulk saveBreak(MesPmBreakBulk record) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if(StringUtils.isEmpty(record.getMesPmBreakBulkDets())){
            throw new BizErrorException(ErrorCodeEnum.GL99990100);
        }
//        Example codeEx = new Example(MesPmBreakBulk.class);
//        codeEx.createCriteria().andEqualTo("batchNo",record.getBatchNo());
//        if(mesPmBreakBulkMapper.selectByExample(codeEx).size()>0 && record.getBreakBulkType()==(byte)1){
//            throw new BizErrorException("该流程单已经拆批");
//        }
        record.setBreakBulkCode(CodeUtils.getId("BREAK"));
        //合批作业生成母批次号
        if(record.getBreakBulkType()==(byte)2){
            String batch = record.getMesPmBreakBulkDets().get(0).getChildLotNo();
            record.setBatchNo(batch.substring(0,batch.length()-2));
        }else{
            if(StringUtils.isEmpty(record.getBatchNo())){
                throw new BizErrorException("批次不能为空");
            }
        }
        //查询当前工序
        Long processId = mesPmBreakBulkMapper.sleProcess(record.getBatchNo());
        if(StringUtils.isEmpty(processId)){
             throw new BizErrorException("获取当前工序失败！");
        }

        //获取品质确认人员
        String qmsName = mesPmBreakBulkMapper.confirmation(record.getBatchNo(),processId);

        record.setProcessId(processId);
        record.setCreateTime(new Date());
        record.setCreateUserId(user.getUserId());
        record.setModifiedTime(new Date());
        record.setModifiedUserId(user.getUserId());
        int num = mesPmBreakBulkMapper.insertUseGeneratedKeys(record);
        record.setQualityName(qmsName);

        SmtWorkOrderCardPool sm = new SmtWorkOrderCardPool();
        sm.setWorkOrderCardId(record.getBatchNo());
        SmtWorkOrderCardPool smtWorkOrderCardPool = smtWorkOrderCardPoolMapper.selectOne(sm);

        boolean isUp = false;

        int counter = mesPmBreakBulkMapper.batchCount(record.getBatchNo());
        //添加明细
        int i = counter>0?counter+1:1;
        for (MesPmBreakBulkDet mesPmBreakBulkDet : record.getMesPmBreakBulkDets()) {

            //拆批作业生产子批次号≤µ˚≥
            if(record.getBreakBulkType()==(byte)1){
                DecimalFormat df=new DecimalFormat("00");
                String no = df.format(i);
                mesPmBreakBulkDet.setChildLotNo(record.getBatchNo()+no);
                mesPmBreakBulkDet.setStatus((byte)1);
            }else{
                //合批：状态：2：已合批生成工单
                mesPmBreakBulkDet.setStatus((byte)2);
            }
            mesPmBreakBulkDet.setBreakBulkId(record.getBreakBulkId());
            mesPmBreakBulkDet.setCreateTime(new Date());
            mesPmBreakBulkDet.setCreateUserId(user.getUserId());
            mesPmBreakBulkDet.setModifiedTime(new Date());
            mesPmBreakBulkDet.setModifiedUserId(user.getUserId());
            mesPmBreakBulkDetMapper.insertUseGeneratedKeys(mesPmBreakBulkDet);

            if(record.getBreakBulkType()==(byte)1){


                Example example = new Example(SmtProcessListProcess.class);
                example.createCriteria().andEqualTo("workOrderCardPoolId",smtWorkOrderCardPool.getWorkOrderCardPoolId());
                List<SmtProcessListProcess> processListProcesses = smtProcessListProcessMapper.selectByExample(example);
                if(processListProcesses.size()<1){
                    throw new BizErrorException("获取流程单失败");
                }
                SmtProcessListProcess up = processListProcesses.get(processListProcesses.size()-1);
                up.setProcessListProcessId(null);
                if(!isUp){
                    //生成流转卡
                    SmtWorkOrderCardPool sms = new SmtWorkOrderCardPool();
                    BeanUtil.copyProperties(smtWorkOrderCardPool,sms);
                    sms.setWorkOrderCardPoolId(null);
                    sms.setType((byte)3);
                    sms.setParentId(smtWorkOrderCardPool.getWorkOrderCardPoolId());
                    sms.setWorkOrderCardId(mesPmBreakBulkDet.getChildLotNo());
                    smtWorkOrderCardPoolMapper.insertUseGeneratedKeys(sms);
                    //生成流程单up
                    //过站中
                    up.setProcessType((byte)2);
                    up.setStatus((byte)1);
                    up.setOutputQuantity(new BigDecimal(0));
                    up.setCurOutputQty(mesPmBreakBulkDet.getBreakBulkQty());
                    up.setProcessListProcessCode(CodeUtils.getId("SPLP"));
                    up.setStartWorkQty(mesPmBreakBulkDet.getBreakBulkQty());
                    up.setWorkOrderCardPoolId(sms.getWorkOrderCardPoolId());
                    smtProcessListProcessMapper.insertSelective(up);
                    isUp = true;
                }else{
                    //生成流转卡
                    SmtWorkOrderCardPool sms = new SmtWorkOrderCardPool();
                    BeanUtil.copyProperties(smtWorkOrderCardPool,sms);
                    sms.setWorkOrderCardPoolId(null);
                    sms.setType((byte)3);
                    sms.setParentId(smtWorkOrderCardPool.getWorkOrderCardPoolId());
                    sms.setWorkOrderCardId(mesPmBreakBulkDet.getChildLotNo());
                    smtWorkOrderCardPoolMapper.insertUseGeneratedKeys(sms);
                    //生成流程单down
                    up.setProcessType((byte)2);
                    up.setStatus((byte)1);
                    up.setOutputQuantity(new BigDecimal(0));
                    up.setCurOutputQty(mesPmBreakBulkDet.getBreakBulkQty());
                    up.setProcessListProcessCode(CodeUtils.getId("SPLP"));
                    up.setStartWorkQty(mesPmBreakBulkDet.getBreakBulkQty());
                    up.setWorkOrderCardPoolId(sms.getWorkOrderCardPoolId());
                    smtProcessListProcessMapper.insertSelective(up);
                }
            }
            i++;
        }
        if(record.getBreakBulkType()==(byte)2){
            //合批生成流转卡
            Example example = new Example(SmtProcessListProcess.class);
            example.createCriteria().andEqualTo("workOrderCardPoolId",smtWorkOrderCardPool.getWorkOrderCardPoolId());
            List<SmtProcessListProcess> processListProcesses = smtProcessListProcessMapper.selectByExample(example);
            if(processListProcesses.size()<1){
                throw new BizErrorException("获取流程单失败");
            }

            List<SmtProcessListProcess> processListStart = processListProcesses.stream().filter(pl->pl.getProcessType()==(byte)1).collect(Collectors.toList());
            List<SmtProcessListProcess> processListRe = processListProcesses.stream().filter(pl->pl.getProcessType()==(byte)2).collect(Collectors.toList());

            //开工最后一个过站记录
            SmtProcessListProcess start = processListStart.get(processListStart.size()-1);
            start.setProcessListProcessId(null);
            start.setStartWorkQty(record.getBreakBulkBatchQty() );
            start.setCurOutputQty(start.getCurOutputQty().add(record.getBreakBulkBatchQty()));
            start.setProcessListProcessCode(CodeUtils.getId("SPLP"));
            start.setWorkOrderCardPoolId(smtWorkOrderCardPool.getWorkOrderCardPoolId());
            smtProcessListProcessMapper.insertSelective(start);

            //报工最后一个过站记录
            SmtProcessListProcess re = processListRe.get(processListRe.size()-1);
            re.setProcessListProcessId(null);
            re.setOutputQuantity(record.getBreakBulkBatchQty());
            re.setCurOutputQty(re.getCurOutputQty().add(record.getBreakBulkBatchQty()));
            re.setProcessListProcessCode(CodeUtils.getId("SPLP"));
            re.setWorkOrderCardPoolId(smtWorkOrderCardPool.getWorkOrderCardPoolId());
            smtProcessListProcessMapper.insertSelective(re);

        }
        return record;
    }

    @Override
    public MesPmBreakBulkPrintDto reprint(SearchMesPmBreakBulk searchMesPmBreakBulk) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(user)){
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        if(searchMesPmBreakBulk.getBreakBulkType()==(byte)1){
            Example example = new Example(MesPmBreakBulkDet.class);
            example.createCriteria().andEqualTo("childLotNo",searchMesPmBreakBulk.getBatchNo());
            MesPmBreakBulkDet mesPmBreakBulkDet = new MesPmBreakBulkDet();
            mesPmBreakBulkDet.setModifiedUserId(user.getUserId());
            mesPmBreakBulkDet.setModifiedTime(new Date());
            int num =mesPmBreakBulkDetMapper.updateByExampleSelective(mesPmBreakBulkDet,example);
            if(num>0){
                MesPmBreakBulkPrintDto mesPmBreakBulkPrintDto = mesPmBreakBulkDetMapper.reprintDet(searchMesPmBreakBulk);
                //获取品质确认人员
                String qmsName = mesPmBreakBulkMapper.confirmation(mesPmBreakBulkPrintDto.getBatchNo(),mesPmBreakBulkPrintDto.getProcessId());
                mesPmBreakBulkPrintDto.setQualityName(qmsName);
                mesPmBreakBulkPrintDto.setPrintDate(new Date());
                Long routeId = mesPmBreakBulkPrintDto.getRouteId();
                //查询工艺路线配置
                List<BaseRouteProcess> routeProcesses = smtWorkOrderMapper.selectRouteProcessByRouteId(routeId);
                if (StringUtils.isNotEmpty(routeProcesses)) {
                    StringBuffer sb =new StringBuffer();
                    for (BaseRouteProcess routeProcess : routeProcesses) {
                        sb.append(routeProcess.getProcessName()+"-");
                    }
                    mesPmBreakBulkPrintDto.setProcessLink(sb.substring(0,sb.length()-1));
                }
                return mesPmBreakBulkPrintDto;
            }else {
                throw new BizErrorException("更新打印日期失败");
            }
        }else if (searchMesPmBreakBulk.getBreakBulkType()==(byte)2){
            Example example = new Example(MesPmBreakBulk.class);
            example.createCriteria().andEqualTo("batchNo",searchMesPmBreakBulk.getBatchNo());
            MesPmBreakBulk mesPmBreakBulk = new MesPmBreakBulkDto();
            mesPmBreakBulk.setModifiedUserId(user.getUserId());
            mesPmBreakBulk.setModifiedTime(new Date());
            int num =mesPmBreakBulkMapper.updateByExampleSelective(mesPmBreakBulk,example);
            if(num>0){
                MesPmBreakBulkPrintDto mesPmBreakBulkPrintDto = mesPmBreakBulkMapper.reprint(searchMesPmBreakBulk);
                //获取品质确认人员
                String qmsName = mesPmBreakBulkMapper.confirmation(mesPmBreakBulkPrintDto.getBatchNo(),mesPmBreakBulkPrintDto.getProcessId());
                mesPmBreakBulkPrintDto.setQualityName(qmsName);
                mesPmBreakBulkPrintDto.setPrintDate(new Date());
                Long routeId = mesPmBreakBulkPrintDto.getRouteId();
                //查询工艺路线配置
                List<BaseRouteProcess> routeProcesses = smtWorkOrderMapper.selectRouteProcessByRouteId(routeId);
                if (StringUtils.isNotEmpty(routeProcesses)) {
                    StringBuffer sb =new StringBuffer();
                    for (BaseRouteProcess routeProcess : routeProcesses) {
                        sb.append(routeProcess.getProcessName()+"-");
                    }
                    mesPmBreakBulkPrintDto.setProcessLink(sb.substring(0,sb.length()-1));
                }
                return mesPmBreakBulkPrintDto;
            }else {
                throw new BizErrorException("更新打印日期失败");
            }

        }
        return null;
    }
}
