package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.dto.basic.SmtMaterialPackageDto;
import com.fantechs.common.base.entity.basic.SmtMaterialPackage;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterialPackage;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtMaterialPackageMapper extends MyMapper<SmtMaterialPackage> {

    List<SmtMaterialPackageDto> findList(SearchSmtMaterialPackage searchSmtMaterialPackage);
}