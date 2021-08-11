package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigReturnDto;
import com.fantechs.common.base.general.entity.eam.EamJigReturn;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigReturnMapper extends MyMapper<EamJigReturn> {
    List<EamJigReturnDto> findList(Map<String,Object> map);

    Integer getReturnQty(Map<String,Object> map);
}