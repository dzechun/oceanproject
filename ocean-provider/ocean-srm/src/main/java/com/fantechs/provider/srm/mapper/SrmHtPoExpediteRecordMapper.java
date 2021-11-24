package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.entity.srm.history.SrmHtPoExpediteRecord;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmHtPoExpediteRecordMapper extends MyMapper<SrmHtPoExpediteRecord> {
    List<SrmHtPoExpediteRecord> findList(Map<String, Object> map);
}
