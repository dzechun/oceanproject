package com.fantechs.provider.mes.pm.service.impl;

import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.pm.*;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtProcessListProcess;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderCardPool;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardPool;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;
import com.fantechs.provider.mes.pm.mapper.SmtWorkOrderCardPoolMapper;
import com.fantechs.provider.mes.pm.service.SmtProcessListProcessService;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderCardPoolService;
import com.fantechs.provider.mes.pm.service.SmtWorkOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class SmtWorkOrderCardPoolServiceImpl extends BaseService<SmtWorkOrderCardPool> implements SmtWorkOrderCardPoolService {

    @Resource
    private SmtWorkOrderCardPoolMapper smtWorkOrderCardPoolMapper;
    @Resource
    private SmtWorkOrderService smtWorkOrderService;
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
        SearchSmtWorkOrder searchSmtWorkOrder = new SearchSmtWorkOrder();
        searchSmtWorkOrder.setWorkOrderId(smtWorkOrderCardPool.getWorkOrderId());
        List<SmtWorkOrderDto> smtWorkOrderDtoList = smtWorkOrderService.findList(searchSmtWorkOrder);
        if(StringUtils.isEmpty(smtWorkOrderDtoList))
        {
            return null;
        }
        SmtWorkOrderDto smtWorkOrderDto = smtWorkOrderDtoList.get(0);
        ProcessListWorkOrderDTO processListWorkOrderDTO =new ProcessListWorkOrderDTO();
        processListWorkOrderDTO.setWorkOrderCardId(workOrderCardId);
        processListWorkOrderDTO.setWorkOrderCardPoolId(smtWorkOrderCardPool.getWorkOrderCardPoolId());
        BeanUtils.copyProperties(smtWorkOrderDto,processListWorkOrderDTO);

        //=====查找当前流程单是否已经有过站信息，有则统计已报工总数
        SearchSmtProcessListProcess searchSmtProcessListProcess = new SearchSmtProcessListProcess();
        searchSmtProcessListProcess.setWorkOrderCardPoolId(smtWorkOrderCardPool.getWorkOrderCardPoolId());
        searchSmtProcessListProcess.setStatus((byte)2);
        List<SmtProcessListProcessDto> smtProcessListProcessDtoList = smtProcessListProcessService.findList(searchSmtProcessListProcess);
        double outputTotalQty=0.0;
        if(StringUtils.isNotEmpty(smtProcessListProcessDtoList)){
            for (SmtProcessListProcessDto smtProcessListProcessDto : smtProcessListProcessDtoList) {
                outputTotalQty+=smtProcessListProcessDto.getOutputQuantity().doubleValue();
            }
            processListWorkOrderDTO.setPreQty(smtProcessListProcessDtoList.get(0).getOutputQuantity());
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
}
