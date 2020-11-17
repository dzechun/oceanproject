package com.fantechs.provider.electronic.service.Impl;

import com.fantechs.common.base.entity.history.SmtHtElectronicTagStorage;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.electronic.mapper.SmtHtElectronicTagStorageMapper;
import com.fantechs.provider.electronic.service.SmtHtElectronicTagStorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2020/11/17.
 */
@Service
public class SmtHtElectronicTagStorageServiceImpl  extends BaseService<SmtHtElectronicTagStorage> implements SmtHtElectronicTagStorageService {

    @Resource
    public SmtHtElectronicTagStorageMapper smtHtElectronicTagStorageMapper;

    @Override
    public List<SmtHtElectronicTagStorage> findHtList(Map<String, Object> map) {
        return smtHtElectronicTagStorageMapper.findHtList(map);
    }
}
