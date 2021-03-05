package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.entity.basic.SmtWarehousePersonnel;
import com.fantechs.common.base.entity.basic.search.SearchSmtWarehousePersonnel;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtWarehousePersonnelMapper extends MyMapper<SmtWarehousePersonnel> {

    List<SmtWarehousePersonnel> findList(SearchSmtWarehousePersonnel searchSmtWarehousePersonnel);
}