package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.dto.basic.SmtKeyMaterialDto;
import com.fantechs.common.base.entity.basic.SmtKeyMaterial;
import com.fantechs.common.base.entity.basic.search.SearchSmtMaterial;
import com.fantechs.common.base.mybatis.MyMapper;
import com.sun.corba.se.impl.ior.OldJIDLObjectKeyTemplate;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtKeyMaterialMapper extends MyMapper<SmtKeyMaterial> {

    List<SmtKeyMaterialDto> findList(Map<String,Object> map);
}