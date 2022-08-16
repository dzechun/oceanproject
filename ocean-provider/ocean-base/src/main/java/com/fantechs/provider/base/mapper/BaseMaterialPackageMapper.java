package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseMaterialPackageDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterialPackage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterialPackage;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseMaterialPackageMapper extends MyMapper<BaseMaterialPackage> {

    List<BaseMaterialPackageDto> findList(Map<String, Object> map);
}