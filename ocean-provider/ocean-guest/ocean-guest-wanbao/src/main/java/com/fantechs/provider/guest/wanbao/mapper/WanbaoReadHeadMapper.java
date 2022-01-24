package com.fantechs.provider.guest.wanbao.mapper;

import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.common.base.general.dto.wanbao.WanbaoReadHeadDto;
import com.fantechs.common.base.general.entity.wanbao.WanbaoReadHead;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WanbaoReadHeadMapper extends MyMapper<WanbaoReadHead> {
    List<WanbaoReadHeadDto> findList(Map<String, Object> map);
}