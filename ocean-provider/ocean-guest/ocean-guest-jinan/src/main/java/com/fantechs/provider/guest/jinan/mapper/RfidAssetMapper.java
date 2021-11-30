package com.fantechs.provider.guest.jinan.mapper;

import com.fantechs.common.base.general.entity.jinan.RfidAsset;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface RfidAssetMapper extends MyMapper<RfidAsset> {
    List<RfidAsset> findList(Map<String, Object> map);
}