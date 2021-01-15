package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.entity.mes.pm.history.MesHtOrderMaterial;
import com.fantechs.common.base.general.dto.mes.pm.history.MesHtOrderMaterialDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface MesHtOrderMaterialMapper extends MyMapper<MesHtOrderMaterial> {
   //以特定过滤条件查询
   List<MesHtOrderMaterialDTO> selectFilterAll(Map<String,Object> map);

}