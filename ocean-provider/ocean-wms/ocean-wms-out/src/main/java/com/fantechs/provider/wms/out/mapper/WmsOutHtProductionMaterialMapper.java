package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterial;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtProductionMaterial;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutHtProductionMaterialMapper extends MyMapper<WmsOutHtProductionMaterial> {
    List<WmsOutProductionMaterial> findHtList(Map<String, Object> map);
}