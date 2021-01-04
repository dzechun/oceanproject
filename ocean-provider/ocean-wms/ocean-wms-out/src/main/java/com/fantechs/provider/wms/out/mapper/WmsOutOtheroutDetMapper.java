package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutOtheroutDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutOtheroutDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtOtheroutDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutOtheroutDetMapper extends MyMapper<WmsOutOtheroutDet> {

    List<WmsOutOtheroutDetDto> findList(Map<String, Object> map);

    int batchUpdate(List<WmsOutOtheroutDet> wmsOutOtheroutDets);
}