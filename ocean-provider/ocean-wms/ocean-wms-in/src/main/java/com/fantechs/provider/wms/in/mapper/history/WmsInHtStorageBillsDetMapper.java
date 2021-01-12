package com.fantechs.provider.wms.in.mapper.history;

import com.fantechs.common.base.entity.basic.history.WmsInHtStorageBillsDet;
import com.fantechs.common.base.dto.basic.history.WmsInHtStorageBillsDetDTO;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WmsInHtStorageBillsDetMapper extends MyMapper<WmsInHtStorageBillsDet> {
    //通过ID查找用户名称
   String selectUserName(Object id);
   //以特定过滤条件查询
   List<WmsInHtStorageBillsDetDTO> selectFilterAll(@Param("storageBillsId") Long storageBillsId);

}