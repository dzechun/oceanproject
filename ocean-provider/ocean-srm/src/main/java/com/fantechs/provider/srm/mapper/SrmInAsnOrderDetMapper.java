package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDetDto;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmInAsnOrderDetMapper extends MyMapper<SrmInAsnOrderDet> {
    List<SrmInAsnOrderDetDto> findList(Map<String, Object> map);

    int batchUpdate(List<SrmInAsnOrderDetDto> list);
}
