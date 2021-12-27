package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.OmTransferOrderDto;
import com.fantechs.common.base.general.entity.om.OmTransferOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmTransferOrderMapper extends MyMapper<OmTransferOrder> {
    List<OmTransferOrderDto> findList(Map<String,Object> map);
}