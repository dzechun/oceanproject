package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.electronic.entity.SmtElectronicTagStorage;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/17.
 */
public interface SmtElectronicTagStorageService extends IService<SmtElectronicTagStorage> {

    List<SmtElectronicTagStorageDto> findList(Map<String, Object> map);

    Map<String,Object> importElectronicTagController(List<SmtElectronicTagStorageDto> smtElectronicTagStorageDtos);



}
