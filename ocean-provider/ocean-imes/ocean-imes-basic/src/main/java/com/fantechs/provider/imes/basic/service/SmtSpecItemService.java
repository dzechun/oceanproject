package com.fantechs.provider.imes.basic.service;



import com.fantechs.common.base.dto.basic.SmtSpecItemExcelDTO;
import com.fantechs.common.base.entity.basic.SmtSpecItem;
import com.fantechs.common.base.entity.basic.search.SearchSmtSpecItem;

import java.util.List;

public interface SmtSpecItemService {
    //通过条件查询配置项信息
    List<SmtSpecItem> selectSpecItems(SearchSmtSpecItem searchSmtSpecItem);

    //新增配置项信息
    int insert(SmtSpecItem smtSpecItem);

    //修改配置项信息
    int updateById(SmtSpecItem smtSpecItem);

    //删除配置项信息
    int deleteByIds(List<String> specIds);

    //通过条件导出配置项信息
    List<SmtSpecItemExcelDTO> exportSpecItems(SearchSmtSpecItem searchSmtSpecItem);

    //通过配置项ID查询配置项信息
    SmtSpecItem selectById(Long specId);
}
