package com.fantechs.provider.ews.mapper;

import com.fantechs.common.base.general.dto.ews.EwsWarningUserInfoDto;
import com.fantechs.common.base.general.entity.ews.EwsWarningUserInfo;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EwsWarningUserInfoMapper extends MyMapper<EwsWarningUserInfo> {
    List<EwsWarningUserInfoDto> findList(Map<String,Object> map);
}