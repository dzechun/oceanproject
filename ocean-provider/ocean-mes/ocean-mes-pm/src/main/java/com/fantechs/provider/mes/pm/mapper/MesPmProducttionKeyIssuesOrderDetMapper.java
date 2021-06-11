package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.entity.mes.pm.MesPmProducttionKeyIssuesOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesPmProducttionKeyIssuesOrderDetMapper extends MyMapper<MesPmProducttionKeyIssuesOrderDet> {
    List<MesPmProducttionKeyIssuesOrderDet> findList(Map<String, Object> map);
}