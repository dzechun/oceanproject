package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseBadnessPhenotypeDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessPhenotype;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseBadnessPhenotypeMapper extends MyMapper<BaseBadnessPhenotype> {

    List<BaseBadnessPhenotypeDto> findList(Map<String, Object> map);

}
