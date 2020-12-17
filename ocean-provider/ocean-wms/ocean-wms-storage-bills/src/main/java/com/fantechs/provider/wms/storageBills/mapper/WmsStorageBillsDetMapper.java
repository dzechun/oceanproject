package com.fantechs.provider.wms.storageBills.mapper;

import com.fantechs.common.base.dto.storage.WmsStorageBillsDetDTO;
import com.fantechs.common.base.entity.storage.WmsStorageBillsDet;
import org.apache.ibatis.annotations.Mapper;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

@Mapper
public interface WmsStorageBillsDetMapper extends MyMapper<WmsStorageBillsDet> {
    //通过ID查找用户名称
   String selectUserName(Object id);
    //通过仓库清单ID找到所有对应的物料信息
    List<WmsStorageBillsDetDTO> selectDTOByBillId(Long billId);
}