package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.entity.basic.SmtSupplierAddress;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtSupplierAddressMapper;
import com.fantechs.provider.imes.basic.service.SmtSupplierAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/05.
 */
@Service
public class SmtSupplierAddressServiceImpl extends BaseService<SmtSupplierAddress> implements SmtSupplierAddressService {

    @Resource
    private SmtSupplierAddressMapper smtSupplierAddressMapper;


    @Override
    public List<SmtSupplierAddress> findList(Map<String, Object> map) {
        return smtSupplierAddressMapper.findList(map);
    }
}
