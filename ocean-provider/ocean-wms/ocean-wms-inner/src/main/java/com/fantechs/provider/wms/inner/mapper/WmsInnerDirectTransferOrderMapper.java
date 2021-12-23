package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerDirectTransferOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerDirectTransferOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerDirectTransferOrderMapper extends MyMapper<WmsInnerDirectTransferOrder> {
    List<WmsInnerDirectTransferOrderDto> findList(Map<String,Object> map);
}