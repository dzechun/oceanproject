package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.history.SmtHtKeyMaterial;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtHtKeyMaterialMapper extends MyMapper<SmtHtKeyMaterial> {

    List<SmtHtKeyMaterial> findHtList(Map<String, Object> map);
}