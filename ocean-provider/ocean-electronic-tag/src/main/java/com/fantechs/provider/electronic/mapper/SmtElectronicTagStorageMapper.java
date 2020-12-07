package com.fantechs.provider.electronic.mapper;

import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.electronic.entity.SmtElectronicTagController;
import com.fantechs.common.base.electronic.entity.SmtElectronicTagStorage;
import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtElectronicTagStorageMapper extends MyMapper<SmtElectronicTagStorage> {
    List<SmtElectronicTagStorageDto> findList(Map<String, Object> map);
    SmtElectronicTagController findElectronicTagControllerByCode(String storageCode);

    //通过电子标签控制器id查询储位信息
    List<SmtStorage> findByElectronicTagControllerId(Long electronicTagControllerId);
}