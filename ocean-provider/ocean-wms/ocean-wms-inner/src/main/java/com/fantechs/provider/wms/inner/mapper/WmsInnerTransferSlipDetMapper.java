package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerTransferSlipDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerTransferSlipDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerTransferSlipDetMapper extends MyMapper<WmsInnerTransferSlipDet> {

    List<WmsInnerTransferSlipDetDto> findList(Map<String, Object> map);
}