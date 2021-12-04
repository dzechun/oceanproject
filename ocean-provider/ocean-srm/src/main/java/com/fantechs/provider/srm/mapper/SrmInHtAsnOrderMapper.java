package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmInHtAsnOrderDto;
import com.fantechs.common.base.general.entity.srm.history.SrmInHtAsnOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmInHtAsnOrderMapper extends MyMapper<SrmInHtAsnOrder> {
    List<SrmInHtAsnOrderDto> findList(Map<String, Object> map);
}