package com.fantechs.provider.imes.storage.mapper;

import com.fantechs.common.base.dto.storage.SmtStorageInventoryDetDto;
import com.fantechs.common.base.entity.storage.SmtStorageInventoryDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtStorageInventoryDetMapper extends MyMapper<SmtStorageInventoryDet> {

    List<SmtStorageInventoryDetDto> findList(Map<String, Object> map);

    List<SmtStorageInventoryDetDto> findById(Long storageInventoryId);

}
