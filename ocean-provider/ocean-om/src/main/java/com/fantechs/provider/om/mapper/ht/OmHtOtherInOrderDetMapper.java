package com.fantechs.provider.om.mapper.ht;

import com.fantechs.common.base.general.dto.om.OmOtherInOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtOtherInOrderDet;
import com.fantechs.common.base.general.entity.om.OmOtherInOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmHtOtherInOrderDetMapper extends MyMapper<OmHtOtherInOrderDet> {
    List<OmOtherInOrderDetDto> findList(Map<String,Object> map);
}