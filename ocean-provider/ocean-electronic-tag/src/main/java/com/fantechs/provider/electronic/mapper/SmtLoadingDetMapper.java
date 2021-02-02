package com.fantechs.provider.electronic.mapper;

import com.fantechs.common.base.electronic.dto.SmtLoadingDetDto;
import com.fantechs.common.base.electronic.entity.SmtLoadingDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtLoadingDetMapper extends MyMapper<SmtLoadingDet> {

    List<SmtLoadingDetDto> findList(Map<String, Object> map);
}