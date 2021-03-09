package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerTransferSlipDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlip;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerTransferSlipMapper extends MyMapper<WmsInnerTransferSlip> {

    List<WmsInnerTransferSlipDto> findList(Map<String, Object> map);
}