package com.fantechs.provider.eam.mapper;
;
import com.fantechs.common.base.general.dto.eam.EamJigScrapOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigScrapOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigScrapOrderMapper extends MyMapper<EamJigScrapOrder> {
    List<EamJigScrapOrderDto> findList(Map<String,Object> map);
}