package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.entity.mes.pm.history.MesPmHtProductionKeyIssuesOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesPmHtProductionKeyIssuesOrderMapper extends MyMapper<MesPmHtProductionKeyIssuesOrder> {
    List<MesPmHtProductionKeyIssuesOrder> findHtList(Map<String, Object> map);
}