package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.*;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtProcessListProcess;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.SmtProcessListProcess;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderCardPool;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardPool;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.mes.pm.mapper.SmtWorkOrderCardPoolMapper;
import com.fantechs.provider.mes.pm.service.SmtProcessListProcessService;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderCardPoolService;
import com.fantechs.provider.mes.pm.service.MesPmWorkOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class SmtWorkOrderCardPoolServiceImpl extends BaseService<SmtWorkOrderCardPool> implements SmtWorkOrderCardPoolService {

    @Resource
    private SmtWorkOrderCardPoolMapper smtWorkOrderCardPoolMapper;
    @Resource
    private MesPmWorkOrderService mesPmWorkOrderService;
    @Resource
    private SmtProcessListProcessService smtProcessListProcessService;
    @Resource
    private BasicFeignApi basicFeignApi;

    @Override
    public List<SmtWorkOrderCardPoolDto> findList(SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool) {
        return smtWorkOrderCardPoolMapper.findList(searchSmtWorkOrderCardPool);
    }

    @Override
    public ProcessListWorkOrderDTO selectWorkOrderDtoByWorkOrderCardId(String workOrderCardId) {
        Example example = new Example(SmtWorkOrderCardPool.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria criteria1 = example.createCriteria();
        criteria1.andEqualTo("isDelete",1).orIsNull("isDelete");
        criteria.andEqualTo("workOrderCardId",workOrderCardId);
        example.and(criteria1);
        List<SmtWorkOrderCardPool> smtWorkOrderCardPoolList = smtWorkOrderCardPoolMapper.selectByExample(example);
        if(StringUtils.isEmpty(smtWorkOrderCardPoolList)){
            return null;
        }
        SmtWorkOrderCardPool smtWorkOrderCardPool = smtWorkOrderCardPoolList.get(0);
        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        searchMesPmWorkOrder.setWorkOrderId(smtWorkOrderCardPool.getWorkOrderId());
        List<MesPmWorkOrderDto> smtWorkOrderDtoList = mesPmWorkOrderService.findList(searchMesPmWorkOrder);
        if(StringUtils.isEmpty(smtWorkOrderDtoList))
        {
            return null;
        }
        MesPmWorkOrderDto smtWorkOrderDto = smtWorkOrderDtoList.get(0);
        ProcessListWorkOrderDTO processListWorkOrderDTO =new ProcessListWorkOrderDTO();
        processListWorkOrderDTO.setWorkOrderCardId(workOrderCardId);
        processListWorkOrderDTO.setWorkOrderCardPoolId(smtWorkOrderCardPool.getWorkOrderCardPoolId());
        BeanUtils.copyProperties(smtWorkOrderDto,processListWorkOrderDTO);

        //=====查找当前流程单是否已经有过站信息，有则统计已报工总数
        SearchSmtProcessListProcess searchSmtProcessListProcess = new SearchSmtProcessListProcess();
        searchSmtProcessListProcess.setWorkOrderCardPoolId(smtWorkOrderCardPool.getWorkOrderCardPoolId());
        searchSmtProcessListProcess.setStatus((byte)2);
        searchSmtProcessListProcess.setProcessType((byte)2);
        List<SmtProcessListProcessDto> smtProcessListProcessDtoList = smtProcessListProcessService.findList(searchSmtProcessListProcess);
        double outputTotalQty=0.0;
        double preQty=0.0;
        if(StringUtils.isNotEmpty(smtProcessListProcessDtoList)){
            smtProcessListProcessDtoList.sort(Comparator.comparing(SmtProcessListProcess::getProcessListProcessId));
            //最后一个工序报工信息
            SmtProcessListProcessDto lastProcessListProcessDto = smtProcessListProcessDtoList.get(smtProcessListProcessDtoList.size() - 1);
            outputTotalQty=lastProcessListProcessDto.getOutputQuantity().doubleValue();

            //找到当前工序的上一个工序报工情况
            for (int i = smtProcessListProcessDtoList.size()-1; i > 0; i--) {
                SmtProcessListProcessDto smtProcessListProcessDto = smtProcessListProcessDtoList.get(i);
                if(!smtProcessListProcessDto.getProcessId().equals(lastProcessListProcessDto.getProcessId())){
                    preQty=smtProcessListProcessDto.getOutputQuantity().doubleValue();
                    break;
                }
            }
            processListWorkOrderDTO.setPreQty(new BigDecimal(preQty));
        }

        processListWorkOrderDTO.setOutputTotalQty(new BigDecimal(outputTotalQty));
        //=====
        return processListWorkOrderDTO;
    }

    @Override
    public int batchUpdateStatus(List<SmtWorkOrderCardPool> smtWorkOrderCardPoolList) {
        return smtWorkOrderCardPoolMapper.batchUpdateStatus(smtWorkOrderCardPoolList);
    }

    @Override
    public List<NoPutIntoCardDTO> getNoPutIntoCard(Long parentId) {
        List<NoPutIntoCardDTO> noPutIntoCardDTOList = smtWorkOrderCardPoolMapper.getNoPutIntoCard(parentId);
        Date date = new Date();
        if(StringUtils.isNotEmpty(noPutIntoCardDTOList)){
            for (NoPutIntoCardDTO noPutIntoCardDTO : noPutIntoCardDTOList) {
                ResponseEntity<List<SmtRouteProcess>> result = basicFeignApi.findConfigureRout(noPutIntoCardDTO.getRouteId());
                if(result.getCode()!=0){
                    throw new BizErrorException(result.getMessage());
                }
                List<SmtRouteProcess> routeProcessList = result.getData();
                StringBuffer sb=new StringBuffer();
                if(StringUtils.isNotEmpty(routeProcessList)){
                    for (int i = 0; i < routeProcessList.size(); i++) {
                        sb.append(routeProcessList.get(i).getProcessName());
                        if(i<routeProcessList.size()-1){
                            sb.append("-");
                        }
                    }
                    noPutIntoCardDTO.setRouteProcess(sb.toString());
                }
                noPutIntoCardDTO.setPrintDate(date);
            }

        }
        return noPutIntoCardDTOList;
    }


    @Override
    public List<NoPutIntoCardDTO> getAppointIntoCard(SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool) {

        List<NoPutIntoCardDTO> noPutIntoCardDTOList = smtWorkOrderCardPoolMapper.getAppointIntoCard(searchSmtWorkOrderCardPool);
        Date date = new Date();
        if(StringUtils.isNotEmpty(noPutIntoCardDTOList)){
            for (NoPutIntoCardDTO noPutIntoCardDTO : noPutIntoCardDTOList) {
                ResponseEntity<List<SmtRouteProcess>> result = basicFeignApi.findConfigureRout(noPutIntoCardDTO.getRouteId());
                if(result.getCode()!=0){
                    throw new BizErrorException(result.getMessage());
                }
                List<SmtRouteProcess> routeProcessList = result.getData();
                StringBuffer sb=new StringBuffer();
                if(StringUtils.isNotEmpty(routeProcessList)){
                    for (int i = 0; i < routeProcessList.size(); i++) {
                        sb.append(routeProcessList.get(i).getProcessName());
                        if(i<routeProcessList.size()-1){
                            sb.append("-");
                        }
                    }
                    noPutIntoCardDTO.setRouteProcess(sb.toString());
                }
                noPutIntoCardDTO.setPrintDate(date);
            }

        }
        return noPutIntoCardDTOList;
    }

    @Override
    public ProcessListWorkOrderDTO selectWorkOrderDtoByWorkOrderCardPoolIdAndProcessId(String workOrderCardPoolId, String processId) {
        ProcessListWorkOrderDTO processListWorkOrderDTO = new ProcessListWorkOrderDTO();
        processListWorkOrderDTO.setWorkOrderCardPoolId(Long.valueOf(workOrderCardPoolId));

        //=====查找当前流程单是否已经有过站信息，有则统计已报工总数
        SearchSmtProcessListProcess searchSmtProcessListProcess = new SearchSmtProcessListProcess();
        searchSmtProcessListProcess.setWorkOrderCardPoolId(Long.valueOf(workOrderCardPoolId));
        searchSmtProcessListProcess.setStatus((byte) 2);
        searchSmtProcessListProcess.setProcessType((byte) 2);
        List<SmtProcessListProcessDto> smtProcessListProcessDtoList = smtProcessListProcessService.findList(searchSmtProcessListProcess);

        double preQty = 0.0;
        if (StringUtils.isNotEmpty(smtProcessListProcessDtoList)) {
            smtProcessListProcessDtoList.sort(Comparator.comparing(SmtProcessListProcess::getProcessListProcessId));

            //找到当前工序报工情况
            for (SmtProcessListProcessDto smtProcessListProcessDto : smtProcessListProcessDtoList) {
                if (smtProcessListProcessDto.getProcessId().equals(Long.valueOf(processId))) {
                    preQty = smtProcessListProcessDto.getOutputQuantity().doubleValue();
                    break;
                }
            }
            processListWorkOrderDTO.setPreQty(new BigDecimal(preQty));
            //=====
            return processListWorkOrderDTO;
        }

        return null;
    }

}
