package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.history.SmtHtPackageSpecification;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtHtPackageSpecificationMapper extends MyMapper<SmtHtPackageSpecification> {

    List<SmtHtPackageSpecification> findHtList(Map<String, Object> map);
}