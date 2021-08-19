package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigScrapOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamJigScrapOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigScrapOrderDetMapper extends MyMapper<EamJigScrapOrderDet> {
    List<EamJigScrapOrderDetDto> findList(Map<String,Object> map);
}