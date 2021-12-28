package com.fantechs.provider.ews.mapper;

import com.fantechs.common.base.general.dto.ews.EwsWarningPushConfigReWuiDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningPushConfigReWui;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EwsWarningPushConfigReWuiMapper extends MyMapper<EwsWarningPushConfigReWui> {
    List<EwsWarningPushConfigReWuiDto> findList(Map<String,Object> map);
}