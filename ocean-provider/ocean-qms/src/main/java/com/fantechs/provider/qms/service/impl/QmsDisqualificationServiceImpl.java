package com.fantechs.provider.qms.service.impl;

import com.fantechs.common.base.general.entity.qms.QmsDisqualification;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.qms.mapper.QmsDisqualificationMapper;
import com.fantechs.provider.qms.service.QmsDisqualificationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * Created by leifengzhi on 2021/01/06.
 */
@Service
public class QmsDisqualificationServiceImpl extends BaseService<QmsDisqualification> implements QmsDisqualificationService {

    @Resource
    private QmsDisqualificationMapper qmsDisqualificationMapper;

}
