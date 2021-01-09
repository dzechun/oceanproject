package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.entity.apply.history.MesHtOrderMaterial;
import com.fantechs.common.base.dto.apply.history.MesHtOrderMaterialDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface MesHtOrderMaterialMapper extends MyMapper<MesHtOrderMaterial> {
   //以特定过滤条件查询
   List<MesHtOrderMaterialDTO> selectFilterAll(Map<String,Object> map);

}