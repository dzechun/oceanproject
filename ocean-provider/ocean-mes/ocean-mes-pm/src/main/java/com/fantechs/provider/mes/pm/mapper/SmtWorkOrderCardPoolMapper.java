package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.SmtWorkOrderCardPoolDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtWorkOrderCardPool;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtWorkOrderCardPool;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SmtWorkOrderCardPoolMapper extends MyMapper<SmtWorkOrderCardPool> {
    List<SmtWorkOrderCardPoolDto> findList(SearchSmtWorkOrderCardPool searchSmtWorkOrderCardPool);
    //批量更新流程卡状态
    int batchUpdateStatus(List<SmtWorkOrderCardPool> smtWorkOrderCardPoolList);
}