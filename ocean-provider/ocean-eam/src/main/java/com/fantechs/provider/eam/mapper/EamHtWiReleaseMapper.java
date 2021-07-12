package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtWiRelease;
import com.fantechs.common.base.general.entity.eam.history.EamHtWiReleaseDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtWiReleaseMapper extends MyMapper<EamHtWiRelease> {
    List<EamHtWiRelease> findHtList(Map<String,Object> map);
}