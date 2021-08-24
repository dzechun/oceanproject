package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.entity.esop.EsopWiFile;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopWiFileMapper extends MyMapper<EsopWiFile> {
    List<EsopWiFile> findList(Map<String,Object> map);
}