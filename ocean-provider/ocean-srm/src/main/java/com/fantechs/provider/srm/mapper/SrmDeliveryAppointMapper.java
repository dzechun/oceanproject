package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmDeliveryAppointDto;
import com.fantechs.common.base.general.entity.srm.SrmDeliveryAppoint;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmDeliveryAppointMapper extends MyMapper<SrmDeliveryAppoint> {
    List<SrmDeliveryAppointDto> findList(Map<String, Object> map);
}