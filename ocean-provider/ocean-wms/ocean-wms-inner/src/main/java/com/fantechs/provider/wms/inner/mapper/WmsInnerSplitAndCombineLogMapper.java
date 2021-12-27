package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerSplitAndCombineLog;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInnerSplitAndCombineLogMapper extends MyMapper<WmsInnerSplitAndCombineLog> {
    List<WmsInnerSplitAndCombineLog> findList(Map<String,Object> map);
}