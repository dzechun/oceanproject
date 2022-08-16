package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseWorkerDto;
import com.fantechs.common.base.general.entity.basic.BaseWorker;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseWorkerMapper extends MyMapper<BaseWorker> {
    List<BaseWorkerDto> findList(Map<String, Object> map);
    BaseWorkerDto selectDtoByKey(Long id);
}