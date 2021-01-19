package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.MesPmBreakBulkDetDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmBreakBulkDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author mr.lei
 */
@Mapper
public interface MesPmBreakBulkDetMapper extends MyMapper<MesPmBreakBulkDet> {
    /**
     * 列表查询详情
     * @param map
     * @return
     */
    List<MesPmBreakBulkDetDto> findList(Map<String, Object> map);
}