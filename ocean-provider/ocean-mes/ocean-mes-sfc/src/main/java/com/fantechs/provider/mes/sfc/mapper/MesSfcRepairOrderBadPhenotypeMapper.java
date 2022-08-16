package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.entity.mes.sfc.MesSfcRepairOrderBadPhenotype;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcRepairOrderBadPhenotypeMapper extends MyMapper<MesSfcRepairOrderBadPhenotype> {
    List<MesSfcRepairOrderBadPhenotype> findList(Map<String, Object> map);
}