package com.fantechs.provider.wms.storageBills.mapper;

import com.fantechs.common.base.entity.storage.WmsStorageBills;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsStorageBillsMapper extends MyMapper<WmsStorageBills> {
    //通过ID查找用户名称
   String selectUserName(Object id);
    //以特定过滤条件查询
    List<WmsStorageBills> selectFilterAll(Map<String,Object> map);
}