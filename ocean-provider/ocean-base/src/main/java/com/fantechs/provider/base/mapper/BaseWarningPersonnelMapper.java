package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseWarningPersonnel;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseWarningPersonnelMapper extends MyMapper<BaseWarningPersonnel> {

    List<BaseWarningPersonnel> findList(Map<String, Object> map);
}