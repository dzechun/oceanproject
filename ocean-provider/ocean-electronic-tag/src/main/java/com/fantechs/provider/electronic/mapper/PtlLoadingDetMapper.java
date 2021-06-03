package com.fantechs.provider.electronic.mapper;

import com.fantechs.common.base.electronic.dto.PtlLoadingDetDto;
import com.fantechs.common.base.electronic.entity.PtlLoadingDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PtlLoadingDetMapper extends MyMapper<PtlLoadingDet> {

    List<PtlLoadingDetDto> findList(Map<String, Object> map);
}