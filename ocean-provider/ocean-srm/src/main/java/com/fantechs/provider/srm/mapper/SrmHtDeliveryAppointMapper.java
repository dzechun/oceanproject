package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmHtDeliveryAppointDto;
import com.fantechs.common.base.general.entity.srm.history.SrmHtDeliveryAppoint;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmHtDeliveryAppointMapper extends MyMapper<SrmHtDeliveryAppoint> {
    List<SrmHtDeliveryAppointDto> findList(Map<String, Object> map);
}