package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtAsnOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtAsnOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/8/26
 */
@Mapper
public interface WmsInHtAsnOrderMapper extends MyMapper<WmsInHtAsnOrder> {
    List<WmsInHtAsnOrderDto> findHtList(Map<String,Object> map);
}
