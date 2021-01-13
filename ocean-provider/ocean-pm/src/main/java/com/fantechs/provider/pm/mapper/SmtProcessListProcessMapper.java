package com.fantechs.provider.pm.mapper;

import com.fantechs.common.base.dto.apply.ProcessListDto;
import com.fantechs.common.base.dto.apply.SmtProcessListProcessDto;
import com.fantechs.common.base.entity.apply.SmtProcessListProcess;
import com.fantechs.common.base.entity.apply.search.SearchSmtProcessListProcess;
import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtProcessListProcessMapper extends MyMapper<SmtProcessListProcess> {
    List<SmtProcessListProcessDto> findList(SearchSmtProcessListProcess searchSmtProcessListProcess);

    List<SmtRouteProcess> select_smt_route_process(SmtRouteProcess smtRouteProcess);

    List<ProcessListDto> findProcess(Long workOrderId);
}