package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseWarehousePersonnel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehousePersonnel;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BaseWarehousePersonnelMapper extends MyMapper<BaseWarehousePersonnel> {

    List<BaseWarehousePersonnel> findList(SearchBaseWarehousePersonnel searchBaseWarehousePersonnel);
}