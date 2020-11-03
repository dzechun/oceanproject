package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.dto.basic.SmtPackingUnitDto;
import com.fantechs.common.base.entity.basic.SmtPackingUnit;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtPackingUnitMapper extends MyMapper<SmtPackingUnit> {

    List<SmtPackingUnitDto> findList(Map<String,Object> map);
}