package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.entity.basic.SmtProcess;
import com.fantechs.common.base.general.dto.mes.pm.*;
import com.fantechs.common.base.general.entity.mes.pm.SmtProcessListProcess;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderBarcodePool;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtProcessListProcess;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderCardPool;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/11/23
*/
public interface SmtProcessListProcessService extends IService<SmtProcessListProcess> {
    List<SmtProcessListProcessDto> findList(SearchSmtProcessListProcess searchSmtProcessListProcess);
    List<SmtProcessListProcess> selectAll(Map<String,Object> map);
    int startJob(SmtWorkOrderBarcodePool smtWorkOrderBarcodePool);
    //过站操作
    int stationToScan(ProcessFinishedProductDTO processFinishedProductDTO);
    //查找工序信息
    SmtProcess findSmtProcess(Long id);
    //查找对应流程卡集合内过站报工数最小的只
    double findMinOutPut(Long workOrderCardPoolId);
    //查找流程卡对应的部件信息及产品信息
    MaterialAndPartsDTO findPartsInformation(Long workOrderCardPoolId);

}
