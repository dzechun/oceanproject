package com.fantechs.provider.wms.storageBills.mapper.history;

import com.fantechs.common.base.entity.apply.history.WmsInHtStorageBillsDet;
import com.fantechs.common.base.dto.apply.history.WmsInHtStorageBillsDetDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInHtStorageBillsDetMapper extends MyMapper<WmsInHtStorageBillsDet> {
    //通过ID查找用户名称
   String selectUserName(Object id);
   //以特定过滤条件查询
   List<WmsInHtStorageBillsDetDTO> selectFilterAll(@Param("storageBillsId") Long storageBillsId);

}