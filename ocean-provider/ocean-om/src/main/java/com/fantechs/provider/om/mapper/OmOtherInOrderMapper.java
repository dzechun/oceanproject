package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.OmOtherInOrderDto;
import com.fantechs.common.base.general.entity.om.OmOtherInOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmOtherInOrderMapper extends MyMapper<OmOtherInOrder> {
    List<OmOtherInOrderDto> findList(Map<String,Object> map);

    OmOtherInOrder findMaterial(@Param("materialId")Long materialId);
}