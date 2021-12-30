package com.fantechs.provider.ews.mapper;

import com.fantechs.common.base.general.dto.ews.EwsHtWarningPushConfigDto;
import com.fantechs.common.base.general.entity.ews.EwsHtWarningPushConfig;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EwsHtWarningPushConfigMapper extends MyMapper<EwsHtWarningPushConfig> {
    List<EwsHtWarningPushConfigDto> findList(Map<String,Object> map);
}