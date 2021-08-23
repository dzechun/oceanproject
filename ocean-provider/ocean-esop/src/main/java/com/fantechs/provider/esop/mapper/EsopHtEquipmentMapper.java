package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.entity.esop.history.EsopHtEquipment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopHtEquipmentMapper extends MyMapper<EsopHtEquipment> {
    List<EsopHtEquipment> findHtList(Map<String,Object> map);
}