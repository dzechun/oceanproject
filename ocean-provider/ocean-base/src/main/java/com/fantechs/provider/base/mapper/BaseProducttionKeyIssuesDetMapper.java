package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.BaseProducttionKeyIssuesDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseProducttionKeyIssuesDetMapper extends MyMapper<BaseProducttionKeyIssuesDet> {
    List<BaseProducttionKeyIssuesDet> findList(Map<String,Object> map);
}