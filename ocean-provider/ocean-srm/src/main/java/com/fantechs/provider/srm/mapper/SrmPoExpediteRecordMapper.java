package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmPoExpediteRecordDto;
import com.fantechs.common.base.general.entity.srm.SrmPoExpediteRecord;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmPoExpediteRecordMapper extends MyMapper<SrmPoExpediteRecord> {
    List<SrmPoExpediteRecordDto> findList(Map<String, Object> map);
}
