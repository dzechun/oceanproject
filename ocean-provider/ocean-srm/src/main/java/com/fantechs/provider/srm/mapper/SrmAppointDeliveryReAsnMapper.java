package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmAppointDeliveryReAsnDto;
import com.fantechs.common.base.general.entity.srm.SrmAppointDeliveryReAsn;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmAppointDeliveryReAsnMapper extends MyMapper<SrmAppointDeliveryReAsn> {
    List<SrmAppointDeliveryReAsnDto> findList(Map<String, Object> map);
}