package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.dto.SmtElectronicTagControllerDto;
import com.fantechs.common.base.electronic.entity.SmtElectronicTagController;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/16.
 */

public interface SmtElectronicTagControllerService extends IService<SmtElectronicTagController> {

    List<SmtElectronicTagControllerDto> findList(Map<String, Object> map);
}
