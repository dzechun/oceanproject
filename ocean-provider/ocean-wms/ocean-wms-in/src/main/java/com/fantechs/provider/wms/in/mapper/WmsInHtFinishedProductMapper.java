package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.entity.wms.in.history.WmsInHtFinishedProduct;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInHtFinishedProductMapper extends MyMapper<WmsInHtFinishedProduct> {
    List<WmsInHtFinishedProduct> findHtList(Map<String, Object> map);
}