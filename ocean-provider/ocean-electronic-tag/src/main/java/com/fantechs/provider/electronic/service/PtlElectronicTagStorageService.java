package com.fantechs.provider.electronic.service;

import com.fantechs.common.base.electronic.dto.PtlElectronicTagStorageDto;
import com.fantechs.common.base.electronic.entity.PtlElectronicTagStorage;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/17.
 */
public interface PtlElectronicTagStorageService extends IService<PtlElectronicTagStorage> {

    List<PtlElectronicTagStorageDto> findList(Map<String, Object> map);

    Map<String,Object> importElectronicTagController(List<PtlElectronicTagStorageDto> ptlElectronicTagStorageDtos);



}
