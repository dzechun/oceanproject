package com.fantechs.provider.ews.mapper;

import com.fantechs.common.base.general.dto.ews.EwsWarningPushConfigDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningPushConfig;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EwsWarningPushConfigMapper extends MyMapper<EwsWarningPushConfig> {
    List<EwsWarningPushConfigDto> findList(Map<String,Object> map);
}