package com.fantechs.provider.guest.eng.mapper;

import com.fantechs.common.base.general.entity.eng.EngUserFollowContractQtyOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngUserFollowContractQtyOrderMapper extends MyMapper<EngUserFollowContractQtyOrder> {
    List<EngUserFollowContractQtyOrder> findList(Map<String, Object> map);
}