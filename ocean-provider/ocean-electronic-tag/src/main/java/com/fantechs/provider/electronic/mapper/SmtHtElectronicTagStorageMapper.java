package com.fantechs.provider.electronic.mapper;

import com.fantechs.common.base.electronic.entity.history.SmtHtElectronicTagStorage;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtHtElectronicTagStorageMapper extends MyMapper<SmtHtElectronicTagStorage> {

    List<SmtHtElectronicTagStorage> findHtList(Map<String,Object> map);
}