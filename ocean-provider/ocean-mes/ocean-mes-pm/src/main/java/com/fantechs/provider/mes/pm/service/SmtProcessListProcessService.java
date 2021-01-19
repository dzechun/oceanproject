package com.fantechs.provider.mes.pm.service;

import com.fantechs.common.base.general.dto.mes.pm.SmtProcessListProcessDto;
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
    int finishedProduct();
}
