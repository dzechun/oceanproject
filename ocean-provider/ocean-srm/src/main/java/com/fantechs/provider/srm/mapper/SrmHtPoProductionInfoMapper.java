package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.entity.srm.history.SrmHtPoProductionInfo;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmHtPoProductionInfoMapper extends MyMapper<SrmHtPoProductionInfo> {
    List<SrmHtPoProductionInfo> findList(Map<String, Object> map);
}
