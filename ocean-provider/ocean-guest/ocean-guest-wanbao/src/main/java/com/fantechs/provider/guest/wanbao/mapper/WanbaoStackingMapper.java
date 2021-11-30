package com.fantechs.provider.guest.wanbao.mapper;

import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.provider.guest.wanbao.dto.WanbaoStackingDto;
import com.fantechs.provider.guest.wanbao.model.WanbaoStacking;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WanbaoStackingMapper extends MyMapper<WanbaoStacking> {
    List<WanbaoStackingDto> findList(Map<String, Object> map);
}