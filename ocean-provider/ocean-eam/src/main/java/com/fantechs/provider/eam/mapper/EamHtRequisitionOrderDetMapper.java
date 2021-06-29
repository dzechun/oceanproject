package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtRequisitionOrderDet;

import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtRequisitionOrderDetMapper extends MyMapper<EamHtRequisitionOrderDet> {
    List<EamHtRequisitionOrderDet> findHtList(Map<String,Object> map);
}