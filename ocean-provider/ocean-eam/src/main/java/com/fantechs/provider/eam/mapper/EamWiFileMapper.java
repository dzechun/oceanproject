package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.EamWiFile;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamWiFileMapper extends MyMapper<EamWiFile> {
    List<EamWiFile> findList(Map<String,Object> map);
}