package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseProductProcessReM;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductProcessReM;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseProductProcessReMMapper extends MyMapper<BaseProductProcessReM> {
    List<BaseProductProcessReM> findList(Map<String,Object> map);
}