package com.fantechs.provider.baseapi.esop.mapper;

import com.fantechs.common.base.general.entity.restapi.esop.EsopWorkshop;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopWorkshopMapper extends MyMapper<EsopWorkshop> {
    List<EsopWorkshop> findList(Map<String, Object> map);
}