package com.fantechs.provider.guest.wanbao.mapper;

import com.fantechs.common.base.general.dto.wanbao.WanbaoStackingDetDto;
import com.fantechs.common.base.general.entity.wanbao.WanbaoStackingDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WanbaoStackingDetMapper extends MyMapper<WanbaoStackingDet> {
    List<WanbaoStackingDetDto> findList(Map<String, Object> map);
}