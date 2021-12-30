package com.fantechs.provider.ews.mapper;

import com.fantechs.common.base.general.dto.ews.EwsHtWarningUserInfoDto;
import com.fantechs.common.base.general.entity.ews.EwsHtWarningUserInfo;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EwsHtWarningUserInfoMapper extends MyMapper<EwsHtWarningUserInfo> {
    List<EwsHtWarningUserInfoDto> findList(Map<String,Object> map);
}