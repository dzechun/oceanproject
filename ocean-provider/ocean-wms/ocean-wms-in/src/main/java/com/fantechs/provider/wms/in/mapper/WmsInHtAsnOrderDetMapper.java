package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtAsnOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtAsnOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/8/26
 */
@Mapper
public interface WmsInHtAsnOrderDetMapper extends MyMapper<WmsInHtAsnOrderDet> {
    List<WmsInHtAsnOrderDetDto> findHtList(Map<String ,Object> map);
}
