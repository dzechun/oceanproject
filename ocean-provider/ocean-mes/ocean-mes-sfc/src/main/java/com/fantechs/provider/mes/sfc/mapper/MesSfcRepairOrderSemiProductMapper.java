package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.entity.mes.sfc.MesSfcRepairOrderSemiProduct;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcRepairOrderSemiProductMapper extends MyMapper<MesSfcRepairOrderSemiProduct> {
    List<MesSfcRepairOrderSemiProduct> findList(Map<String, Object> map);
}