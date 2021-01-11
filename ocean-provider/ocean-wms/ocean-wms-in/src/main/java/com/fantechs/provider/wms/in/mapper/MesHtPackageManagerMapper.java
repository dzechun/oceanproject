package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.entity.basic.history.MesHtPackageManager;
import com.fantechs.common.base.dto.basic.history.MesHtPackageManagerDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface MesHtPackageManagerMapper extends MyMapper<MesHtPackageManager> {
   //以特定过滤条件查询
   List<MesHtPackageManagerDTO> selectFilterAll(Map<String,Object> map);

}