package com.fantechs.provider.electronic.mapper;

import com.fantechs.common.base.electronic.dto.PtlJobOrderDetDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PtlJobOrderDetMapper extends MyMapper<PtlJobOrderDet> {
    List<PtlJobOrderDetDto> findList(Map<String, Object> map);

    int batchUpdate(List<PtlJobOrderDet> ptlJobOrderDetList);
}