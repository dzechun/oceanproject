package com.fantechs.provider.om.mapper.ht;

import com.fantechs.common.base.general.dto.om.OmHtTransferOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtTransferOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/8/26
 */
@Mapper
public interface OmHtTransferOrderDetMapper extends MyMapper<OmHtTransferOrderDet> {
    List<OmHtTransferOrderDetDto> findHtList(Map<String ,Object> map);
}
