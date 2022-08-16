package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.OmSalesCodeReSpcDto;
import com.fantechs.common.base.general.entity.om.OmSalesCodeReSpc;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmSalesCodeReSpcMapper extends MyMapper<OmSalesCodeReSpc> {
    List<OmSalesCodeReSpcDto> findList(Map<String, Object> map);
}