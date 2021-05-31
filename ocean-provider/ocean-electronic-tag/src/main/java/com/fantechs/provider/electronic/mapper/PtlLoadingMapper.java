package com.fantechs.provider.electronic.mapper;

import com.fantechs.common.base.electronic.entity.PtlLoading;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PtlLoadingMapper extends MyMapper<PtlLoading> {

    List<PtlLoading> findList(Map<String, Object> map);
}