package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WanbaoPlatform;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WanbaoPlatformMapper extends MyMapper<WanbaoPlatform> {
    List<WanbaoPlatform> findList(Map<String,Object> map);
}