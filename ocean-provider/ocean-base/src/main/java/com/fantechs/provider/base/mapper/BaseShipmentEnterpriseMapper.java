package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseShipmentEnterpriseDto;
import com.fantechs.common.base.general.entity.basic.BaseShipmentEnterprise;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseShipmentEnterpriseMapper extends MyMapper<BaseShipmentEnterprise> {

    List<BaseShipmentEnterpriseDto> findList(Map<String, Object> map);
}