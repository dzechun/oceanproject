package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigMaintainOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigMaintainOrderDetMapper extends MyMapper<EamJigMaintainOrderDet> {
    List<EamJigMaintainOrderDetDto> findList(Map<String,Object> map);
}