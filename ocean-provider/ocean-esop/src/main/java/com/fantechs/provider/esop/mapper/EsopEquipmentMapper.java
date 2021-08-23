package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.dto.esop.EsopEquipmentDto;
import com.fantechs.common.base.general.entity.esop.EsopEquipment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopEquipmentMapper extends MyMapper<EsopEquipment> {
    List<EsopEquipmentDto> findList(Map<String,Object> map);

    int batchUpdate(List<EsopEquipment> list);

    List<EsopEquipmentDto> findNoGroup(Map<String,Object> map);
}