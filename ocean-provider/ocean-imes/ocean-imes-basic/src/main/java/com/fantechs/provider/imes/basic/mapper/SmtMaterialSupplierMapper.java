package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.dto.basic.SmtMaterialSupplierDto;
import com.fantechs.common.base.entity.basic.SmtMaterialSupplier;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterialSupplier;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtMaterialSupplierMapper extends MyMapper<SmtMaterialSupplier> {
    List<SmtMaterialSupplierDto> findList(SearchSmtMaterialSupplier searchSmtMaterialSupplier);
}