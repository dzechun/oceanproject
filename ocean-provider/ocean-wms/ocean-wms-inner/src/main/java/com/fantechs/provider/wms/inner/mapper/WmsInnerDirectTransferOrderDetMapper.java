package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerDirectTransferOrderDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerDirectTransferOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerDirectTransferOrderDetMapper extends MyMapper<WmsInnerDirectTransferOrderDet> {
    List<WmsInnerDirectTransferOrderDetDto> findList(Map<String,Object> map);
}