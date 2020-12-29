package com.fantechs.provider.wms.storageBills.mapper;

import com.fantechs.common.base.dto.storage.WmsInStorageBillsDetDTO;
import com.fantechs.common.base.entity.storage.WmsInStorageBillsDet;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WmsInStorageBillsDetMapper extends MyMapper<WmsInStorageBillsDet> {
    //通过ID查找用户名称
   String selectUserName(@Param("id") Object id);
    //通过仓库清单ID找到所有对应的物料信息
    List<WmsInStorageBillsDetDTO> selectFilterAll(@Param("storageBillsId") Long storageBillsId);
}