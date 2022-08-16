package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BasePartsInformationDto;
import com.fantechs.common.base.general.entity.basic.BasePartsInformation;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BasePartsInformationMapper extends MyMapper<BasePartsInformation> {
    List<BasePartsInformationDto> findList(Map<String, Object> map);
}
