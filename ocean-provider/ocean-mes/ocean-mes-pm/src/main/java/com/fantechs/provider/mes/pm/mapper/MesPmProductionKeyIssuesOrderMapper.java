package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.entity.mes.pm.MesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesPmProductionKeyIssuesOrderMapper extends MyMapper<MesPmProductionKeyIssuesOrder> {
    List<MesPmProductionKeyIssuesOrder> findList(Map<String, Object> map);
}