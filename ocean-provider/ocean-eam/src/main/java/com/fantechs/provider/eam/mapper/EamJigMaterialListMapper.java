package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigMaterialListDto;
import com.fantechs.common.base.general.entity.eam.EamJigMaterialList;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigMaterialListMapper extends MyMapper<EamJigMaterialList> {
    List<EamJigMaterialListDto> findList(Map<String,Object> map);

    List<EamJigMaterialListDto> findExportList(Map<String,Object> map);

}