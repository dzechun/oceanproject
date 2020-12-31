package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutOtheroutDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutOtherout;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtOtherout;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutOtheroutMapper extends MyMapper<WmsOutOtherout> {

    List<WmsOutOtheroutDto> findList(Map<String, Object> map);
}