package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigRequisitionDto;
import com.fantechs.common.base.general.entity.eam.EamJigRequisition;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigRequisitionMapper extends MyMapper<EamJigRequisition> {
    List<EamJigRequisitionDto> findList(Map<String,Object> map);
}