package com.fantechs.provider.electronic.mapper;

import com.fantechs.common.base.electronic.dto.PtlClientManageDto;

import com.fantechs.common.base.electronic.entity.PtlClientManage;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface PtlClientManageMapper extends MyMapper<PtlClientManage> {
    List<PtlClientManageDto> findList(Map<String, Object> map);

}