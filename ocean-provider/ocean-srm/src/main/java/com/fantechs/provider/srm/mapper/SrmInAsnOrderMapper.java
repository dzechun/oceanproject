package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmInAsnOrderDto;
import com.fantechs.common.base.general.entity.srm.SrmInAsnOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmInAsnOrderMapper extends MyMapper<SrmInAsnOrder> {
    List<SrmInAsnOrderDto> findList(Map<String, Object> map);
}