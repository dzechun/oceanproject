package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.dto.basic.SmtPackageSpecificationDto;
import com.fantechs.common.base.entity.basic.SmtPackageSpecification;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface SmtPackageSpecificationMapper extends MyMapper<SmtPackageSpecification> {

    List<SmtPackageSpecificationDto> findList(Map<String, Object> map);
}