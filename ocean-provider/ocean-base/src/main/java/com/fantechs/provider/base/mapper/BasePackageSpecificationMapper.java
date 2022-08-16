package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BasePackageSpecificationDto;
import com.fantechs.common.base.general.entity.basic.BasePackageSpecification;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;


@Mapper
public interface BasePackageSpecificationMapper extends MyMapper<BasePackageSpecification> {

    List<BasePackageSpecificationDto> findList(Map<String, Object> map);
    List<BasePackageSpecificationDto> findByMaterialProcess(Map<String, Object> map);
}