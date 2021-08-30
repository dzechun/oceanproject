package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmHtPackingOrderDto;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmHtPackingOrderMapper extends MyMapper<SrmHtPackingOrder> {
    List<SrmHtPackingOrderDto> findList(Map<String, Object> map);
}