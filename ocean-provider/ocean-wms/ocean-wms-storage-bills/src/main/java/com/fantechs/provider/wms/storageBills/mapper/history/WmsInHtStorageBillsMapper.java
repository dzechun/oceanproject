package com.fantechs.provider.wms.storageBills.mapper.history;

import com.fantechs.common.base.entity.apply.history.WmsInHtStorageBills;
import com.fantechs.common.base.dto.apply.history.WmsInHtStorageBillsDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInHtStorageBillsMapper extends MyMapper<WmsInHtStorageBills> {
    //通过ID查找用户名称
    String selectUserName(Object id);

    //以特定过滤条件查询
    List<WmsInHtStorageBillsDTO> selectFilterAll(Map<String, Object> map);

}