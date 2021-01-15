package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BasePlatePartsDto;
import com.fantechs.common.base.general.entity.basic.BasePlateParts;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BasePlatePartsMapper extends MyMapper<BasePlateParts> {
    List<BasePlatePartsDto> findList(Map<String, Object> map);
}
