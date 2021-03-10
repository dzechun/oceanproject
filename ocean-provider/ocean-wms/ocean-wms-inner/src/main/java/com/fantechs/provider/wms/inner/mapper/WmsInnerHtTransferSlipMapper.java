package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtTransferSlip;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerHtTransferSlipMapper extends MyMapper<WmsInnerHtTransferSlip> {

    List<WmsInnerHtTransferSlip> findHtList(Map<String, Object> map);
}