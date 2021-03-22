package com.fantechs.provider.log.service.impl;

import com.fantechs.common.base.general.dto.log.SmtEmpOperationLogDto;
import com.fantechs.common.base.general.entity.log.SmtEmpOperationLog;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.log.mapper.SmtEmpOperationLogMapper;
import com.fantechs.provider.log.service.SmtEmpOperationLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2021/03/12.
 */
@Service
public class SmtEmpOperationLogServiceImpl extends BaseService<SmtEmpOperationLog> implements SmtEmpOperationLogService {

    @Resource
    private SmtEmpOperationLogMapper smtEmpOperationLogMapper;

    @Override
    public List<SmtEmpOperationLogDto> findList(Map<String, Object> map) {
        return smtEmpOperationLogMapper.findList(map);
    }
}
