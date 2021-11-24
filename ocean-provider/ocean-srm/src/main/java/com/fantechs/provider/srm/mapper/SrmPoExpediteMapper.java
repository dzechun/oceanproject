package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmPoExpediteDto;
import com.fantechs.common.base.general.entity.srm.SrmPoExpedite;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmPoExpediteMapper extends MyMapper<SrmPoExpedite> {
    List<SrmPoExpediteDto> findList(Map<String, Object> map);
}
