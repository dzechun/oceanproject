package com.fantechs.provider.electronic.service.Impl;

import com.fantechs.common.base.entity.history.SmtHtElectronicTagController;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.electronic.mapper.SmtHtElectronicTagControllerMapper;
import com.fantechs.provider.electronic.service.SmtHtElectronicTagControllerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/11/16.
 */
@Service
public class SmtHtElectronicTagControllerServiceImpl extends BaseService<SmtHtElectronicTagController> implements SmtHtElectronicTagControllerService {

    @Resource
    private SmtHtElectronicTagControllerMapper smtHtElectronicTagControllerMapper;

    @Override
    public List<SmtHtElectronicTagController> findHtList(Map<String,Object> map) {
        return smtHtElectronicTagControllerMapper.findHtList(map);
    }
}
