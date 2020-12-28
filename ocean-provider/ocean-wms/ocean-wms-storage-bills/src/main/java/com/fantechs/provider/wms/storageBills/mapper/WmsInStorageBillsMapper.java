package com.fantechs.provider.wms.storageBills.mapper;

import com.fantechs.common.base.dto.storage.WmsInStorageBillsDTO;
import com.fantechs.common.base.entity.storage.WmsInStorageBills;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInStorageBillsMapper extends MyMapper<WmsInStorageBills> {
    //通过ID查找用户名称
   String selectUserName(@Param("id") Object id);
    //以特定过滤条件查询
    List<WmsInStorageBillsDTO> selectFilterAll(Map<String,Object> map);
    //以特定过滤条件查询
    List<WmsInStorageBillsDTO> pdaSelectFilterAll(Map<String,Object> map);
}