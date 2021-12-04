package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmInHtAsnOrderDetDto;
import com.fantechs.common.base.general.entity.srm.history.SrmInHtAsnOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmInHtAsnOrderDetMapper extends MyMapper<SrmInHtAsnOrderDet> {
    List<SrmInHtAsnOrderDetDto> findList(Map<String, Object> map);
}