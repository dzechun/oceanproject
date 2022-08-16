package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseMaterialOwnerDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterialOwner;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseMaterialOwnerMapper extends MyMapper<BaseMaterialOwner> {
    List<BaseMaterialOwnerDto> findList(Map<String, Object> map);
}