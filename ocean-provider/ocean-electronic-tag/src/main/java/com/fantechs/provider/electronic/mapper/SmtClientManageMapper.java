package com.fantechs.provider.electronic.mapper;

import com.fantechs.common.base.electronic.dto.SmtClientManageDto;

import com.fantechs.common.base.electronic.entity.SmtClientManage;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtClientManageMapper extends MyMapper<SmtClientManage> {
    List<SmtClientManageDto> findList(Map<String, Object> map);

}