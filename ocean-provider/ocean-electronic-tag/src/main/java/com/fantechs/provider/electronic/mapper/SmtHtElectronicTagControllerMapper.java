package com.fantechs.provider.electronic.mapper;

import com.fantechs.common.base.entity.history.SmtHtElectronicTagController;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtHtElectronicTagControllerMapper extends MyMapper<SmtHtElectronicTagController> {

    List<SmtHtElectronicTagController> findHtList(Map<String,Object> map);
}