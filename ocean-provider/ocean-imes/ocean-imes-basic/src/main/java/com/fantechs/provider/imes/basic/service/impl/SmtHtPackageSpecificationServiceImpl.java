package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.entity.basic.history.SmtHtPackageSpecification;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtPackageSpecificationMapper;
import com.fantechs.provider.imes.basic.service.SmtHtPackageSpecificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/11/04.
 */
@Service
public class SmtHtPackageSpecificationServiceImpl extends BaseService<SmtHtPackageSpecification> implements SmtHtPackageSpecificationService {

    @Resource
    private SmtHtPackageSpecificationMapper smtHtPackageSpecificationMapper;

    @Override
    public List<SmtHtPackageSpecification> findHtList(Map<String, Object> map) {
        return smtHtPackageSpecificationMapper.findHtList(map);
    }
}
