package com.fantechs.provider.electronic.mapper;

import com.fantechs.common.base.electronic.dto.PtlElectronicTagStorageDto;
import com.fantechs.common.base.electronic.entity.PtlElectronicTagStorage;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PtlElectronicTagStorageMapper extends MyMapper<PtlElectronicTagStorage> {
    List<PtlElectronicTagStorageDto> findList(Map<String, Object> map);
}