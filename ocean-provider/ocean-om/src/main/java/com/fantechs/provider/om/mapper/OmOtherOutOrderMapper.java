package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDto;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmOtherOutOrderMapper extends MyMapper<OmOtherOutOrder> {
    List<OmOtherOutOrderDto> findList(Map<String,Object>map);

    OmOtherOutOrder findMaterial(@Param("materialId")Long materialId);
}