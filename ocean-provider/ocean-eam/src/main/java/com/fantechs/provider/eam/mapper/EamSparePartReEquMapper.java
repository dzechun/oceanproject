package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamSparePartReEquDto;
import com.fantechs.common.base.general.entity.eam.EamSparePartReEqu;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamSparePartReEquMapper extends MyMapper<EamSparePartReEqu> {
    List<EamSparePartReEquDto> findList(Map<String,Object> map);
}