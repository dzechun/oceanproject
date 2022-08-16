package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.BaseProductionKeyIssuesDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseProductionKeyIssuesDetMapper extends MyMapper<BaseProductionKeyIssuesDet> {
    List<BaseProductionKeyIssuesDet> findList(Map<String,Object> map);
}