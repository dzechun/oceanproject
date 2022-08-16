package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseProductBomDto;
import com.fantechs.common.base.general.entity.basic.BaseProductBom;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

public interface BaseProductBomMapper extends MyMapper<BaseProductBom> {

    List<BaseProductBomDto> findList(Map<String,Object> map);


}