package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseProductFamilyDto;
import com.fantechs.common.base.general.entity.basic.BaseProductFamily;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseProductFamilyMapper extends MyMapper<BaseProductFamily> {

    List<BaseProductFamilyDto> findList(Map<String, Object> map);
}