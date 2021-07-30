package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtJig;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigRequisition;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtJigRequisitionMapper extends MyMapper<EamHtJigRequisition> {
    List<EamHtJigRequisition> findHtList(Map<String,Object> map);
}