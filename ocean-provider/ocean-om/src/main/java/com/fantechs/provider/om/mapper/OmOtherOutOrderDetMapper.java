package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDto;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrder;
import com.fantechs.common.base.general.entity.om.OmOtherOutOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmOtherOutOrderDetMapper extends MyMapper<OmOtherOutOrderDet> {
    List<OmOtherOutOrderDetDto> findList(Map<String ,Object> map);
}