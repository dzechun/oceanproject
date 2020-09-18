package com.fantechs.provider.imes.basic.service.impl;


import com.fantechs.common.base.entity.basic.history.SmtHtDept;
import com.fantechs.common.base.entity.basic.search.SearchSmtDept;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtDeptMapper;
import com.fantechs.provider.imes.basic.service.SmtHtDeptService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SmtHtDeptServiceImpl extends BaseService<SmtHtDept> implements SmtHtDeptService {

    @Resource
    private SmtHtDeptMapper smtHtDeptMapper;

    @Override
    public List<SmtHtDept> selectHtDepts(SearchSmtDept searchSmtDept) {
        return smtHtDeptMapper.selectHtDepts(searchSmtDept);
    }
}
