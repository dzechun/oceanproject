package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigMaintainOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigMaintainOrderMapper extends MyMapper<EamJigMaintainOrder> {
    List<EamJigMaintainOrderDto> findList(Map<String,Object> map);
}