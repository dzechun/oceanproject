package com.fantechs.provider.om.mapper.ht;

import com.fantechs.common.base.general.dto.om.OmHtSalesCodeReSpcDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesCodeReSpc;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmHtSalesCodeReSpcMapper extends MyMapper<OmHtSalesCodeReSpc> {
    List<OmHtSalesCodeReSpcDto> findList(Map<String, Object> map);
}