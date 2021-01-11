package com.fantechs.provider.qms.mapper;

import com.fantechs.common.base.general.dto.qms.QmsAndinStorageQuarantineDto;
import com.fantechs.common.base.general.entity.qms.QmsAndinStorageQuarantine;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface QmsAndinStorageQuarantineMapper extends MyMapper<QmsAndinStorageQuarantine> {
    List<QmsAndinStorageQuarantineDto> findList(Map<String, Object> map);
}
