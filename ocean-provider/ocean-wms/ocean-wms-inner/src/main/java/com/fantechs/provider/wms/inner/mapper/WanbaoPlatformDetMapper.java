package com.fantechs.provider.wms.inner.mapper;

import com.fantechs.common.base.general.dto.wms.inner.WanbaoPlatformDet;
import com.fantechs.common.base.general.dto.wms.inner.WanbaoPlatformDetDto;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WanbaoPlatformDetMapper extends MyMapper<WanbaoPlatformDet> {
    List<WanbaoPlatformDetDto> findList(Map<String,Object> map);
}