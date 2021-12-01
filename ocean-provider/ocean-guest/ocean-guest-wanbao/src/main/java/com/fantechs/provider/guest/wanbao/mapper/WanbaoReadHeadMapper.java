package com.fantechs.provider.guest.wanbao.mapper;

import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.provider.guest.wanbao.dto.WanbaoReadHeadDto;
import com.fantechs.provider.guest.wanbao.model.WanbaoReadHead;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WanbaoReadHeadMapper extends MyMapper<WanbaoReadHead> {
    List<WanbaoReadHeadDto> findList(Map<String, Object> map);
}