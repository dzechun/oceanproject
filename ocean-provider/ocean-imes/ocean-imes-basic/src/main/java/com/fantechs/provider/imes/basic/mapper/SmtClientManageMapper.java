package com.fantechs.provider.imes.basic.mapper;

import com.fantechs.common.base.dto.basic.SmtClientManageDto;
import com.fantechs.common.base.dto.basic.SmtFactoryDto;
import com.fantechs.common.base.mybatis.MyMapper;
import com.fantechs.common.base.entity.basic.SmtClientManage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SmtClientManageMapper extends MyMapper<SmtClientManage> {
    List<SmtClientManageDto> findList(Map<String, Object> map);

}