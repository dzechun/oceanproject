package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.OmOtherOutOrderDetDto;
import com.fantechs.common.base.general.dto.om.OmTransferOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmTransferOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmTransferOrderDetMapper extends MyMapper<OmTransferOrderDet> {
    List<OmTransferOrderDetDto> findList(Map<String,Object> map);

    int batchUpdate(List<OmTransferOrderDetDto> omTransferOrderDetDtos);
}