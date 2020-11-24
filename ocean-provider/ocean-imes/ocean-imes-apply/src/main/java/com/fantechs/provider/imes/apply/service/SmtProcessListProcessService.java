package com.fantechs.provider.imes.apply.service;

import com.fantechs.common.base.dto.apply.SmtProcessListProcessDto;
import com.fantechs.common.base.entity.apply.SmtProcessListProcess;
import com.fantechs.common.base.entity.apply.SmtWorkOrderBarcodePool;
import com.fantechs.common.base.entity.apply.search.SearchSmtProcessListProcess;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
* @author Mr.Lei
* @create 2020/11/23
*/
public interface SmtProcessListProcessService extends IService<SmtProcessListProcess> {
    List<SmtProcessListProcessDto> findList(SearchSmtProcessListProcess searchSmtProcessListProcess);

    int startJob(SmtWorkOrderBarcodePool smtWorkOrderBarcodePool);
}
