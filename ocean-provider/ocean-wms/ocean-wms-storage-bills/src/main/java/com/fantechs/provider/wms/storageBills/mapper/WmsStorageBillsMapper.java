package com.fantechs.provider.wms.storageBills.mapper;

import com.fantechs.common.base.dto.storage.WmsStorageBillsDTO;
import com.fantechs.common.base.entity.storage.WmsStorageBills;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsStorageBillsMapper extends MyMapper<WmsStorageBills> {
    //通过ID查找用户名称
   String selectUserName(@Param("id") Object id);
    //以特定过滤条件查询
    List<WmsStorageBillsDTO> selectFilterAll(Map<String,Object> map);
}