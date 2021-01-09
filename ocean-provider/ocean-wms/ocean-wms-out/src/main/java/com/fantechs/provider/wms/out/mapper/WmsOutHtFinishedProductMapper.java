package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtFinishedProduct;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutHtFinishedProductMapper extends MyMapper<WmsOutHtFinishedProduct> {
    List<WmsOutHtFinishedProduct> finHTList(Map<String, Object> map);
}