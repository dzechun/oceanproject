package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamRequisitionOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamRequisitionOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamRequisitionOrderDetMapper extends MyMapper<EamRequisitionOrderDet> {
    List<EamRequisitionOrderDetDto> findList(Map<String,Object> map);
}