package com.fantechs.provider.ews.mapper;

import com.fantechs.common.base.general.dto.ews.EwsHtWarningPushConfigReWuiDto;
import com.fantechs.common.base.general.entity.ews.EwsHtWarningPushConfigReWui;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EwsHtWarningPushConfigReWuiMapper extends MyMapper<EwsHtWarningPushConfigReWui> {
    List<EwsHtWarningPushConfigReWuiDto> findList(Map<String,Object> map);
}