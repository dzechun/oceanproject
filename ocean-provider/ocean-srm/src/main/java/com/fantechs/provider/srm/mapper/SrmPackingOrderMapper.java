package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmPackingOrderDto;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmPackingOrderMapper extends MyMapper<SrmPackingOrder> {
    List<SrmPackingOrderDto> findList(Map<String, Object> map);
}