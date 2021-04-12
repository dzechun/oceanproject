package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.BaseSupplierAddress;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseSupplierAddressMapper;
import com.fantechs.provider.base.service.BaseSupplierAddressService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/01/05.
 */
@Service
public class BaseSupplierAddressServiceImpl extends BaseService<BaseSupplierAddress> implements BaseSupplierAddressService {

    @Resource
    private BaseSupplierAddressMapper baseSupplierAddressMapper;


    @Override
    public List<BaseSupplierAddress> findList(Map<String, Object> map) {
        return baseSupplierAddressMapper.findList(map);
    }
}
