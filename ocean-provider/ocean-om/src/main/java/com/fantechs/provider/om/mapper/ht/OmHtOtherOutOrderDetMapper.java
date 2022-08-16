package com.fantechs.provider.om.mapper.ht;

import com.fantechs.common.base.general.dto.om.OmHtOtherOutOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtOtherOutOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/8/26
 */
@Mapper
public interface OmHtOtherOutOrderDetMapper extends MyMapper<OmHtOtherOutOrderDet> {
    List<OmHtOtherOutOrderDto> findHtList(Map<String ,Object> map);
}
