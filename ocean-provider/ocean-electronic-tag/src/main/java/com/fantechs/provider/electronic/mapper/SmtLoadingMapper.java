package com.fantechs.provider.electronic.mapper;

import com.fantechs.common.base.electronic.entity.SmtLoading;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtLoadingMapper extends MyMapper<SmtLoading> {

    List<SmtLoading> findList(Map<String, Object> map);
}