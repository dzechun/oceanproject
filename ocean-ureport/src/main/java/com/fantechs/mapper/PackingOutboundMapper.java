package com.fantechs.mapper;

import com.fantechs.entity.PackingOutboundModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/10/27
 */
@Mapper
public interface PackingOutboundMapper {
    List<PackingOutboundModel> findList(Map<String,Object> map);
}
