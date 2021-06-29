package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamRequisitionOrderDto;
import com.fantechs.common.base.general.entity.eam.EamRequisitionOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamRequisitionOrderMapper extends MyMapper<EamRequisitionOrder> {
    List<EamRequisitionOrderDto> findList(Map<String,Object> map);
}