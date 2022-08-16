package com.fantechs.provider.om.mapper.ht;

import com.fantechs.common.base.general.dto.om.OmHtTransferOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtTransferOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/8/26
 */
@Mapper
public interface OmHtTransferOrderMapper extends MyMapper<OmHtTransferOrder> {
    List<OmHtTransferOrderDto> findHtList(Map<String,Object> map);
}
