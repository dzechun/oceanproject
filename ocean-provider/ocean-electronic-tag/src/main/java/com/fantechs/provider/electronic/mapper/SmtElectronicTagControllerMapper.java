package com.fantechs.provider.electronic.mapper;

import com.fantechs.common.base.dto.SmtElectronicTagControllerDto;
import com.fantechs.common.base.entity.SmtElectronicTagController;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtElectronicTagControllerMapper extends MyMapper<SmtElectronicTagController> {
    List<SmtElectronicTagControllerDto> findList(Map<String, Object> map);
}