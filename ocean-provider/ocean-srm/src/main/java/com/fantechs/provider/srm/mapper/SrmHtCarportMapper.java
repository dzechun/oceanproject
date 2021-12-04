package com.fantechs.provider.srm.mapper;

import com.fantechs.common.base.general.dto.srm.SrmHtCarportDto;
import com.fantechs.common.base.general.entity.srm.history.SrmHtCarport;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SrmHtCarportMapper extends MyMapper<SrmHtCarport> {
    List<SrmHtCarportDto> findList(Map<String, Object> map);
}