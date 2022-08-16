package com.fantechs.provider.om.mapper.ht;

import com.fantechs.common.base.general.dto.om.OmOtherInOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtOtherInOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmHtOtherInOrderMapper extends MyMapper<OmHtOtherInOrder> {
    List<OmOtherInOrderDto> findList(Map<String,Object> map);
}