package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.entity.basic.SmtProcess;
import com.fantechs.common.base.general.dto.mes.pm.ProcessFinishedProductDTO;
import com.fantechs.common.base.general.dto.mes.pm.SmtProcessListProcessDto;
import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderCardPoolDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtProcessListProcess;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderBarcodePool;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtProcessListProcess;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
* @author Mr.Lei
* @create 2020/11/23
*/
public interface SmtProcessListProcessService extends IService<SmtProcessListProcess> {
    List<SmtProcessListProcessDto> findList(SearchSmtProcessListProcess searchSmtProcessListProcess);

    int startJob(SmtWorkOrderBarcodePool smtWorkOrderBarcodePool);
    //工序报工
    int finishedProduct(ProcessFinishedProductDTO processFinishedProductDTO);
    //查找工序信息
    SmtProcess findSmtProcess(Long id);
    //查找对应流程卡集合内过站报工数最小的只
    double findMinOutPut(Long workOrderCardPoolId);
}
