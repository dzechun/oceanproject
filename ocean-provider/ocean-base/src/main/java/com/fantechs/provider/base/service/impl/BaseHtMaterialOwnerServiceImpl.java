package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtMaterialOwner;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtMaterialOwnerMapper;
import com.fantechs.provider.base.service.BaseHtMaterialOwnerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/04/23.
 */
@Service
public class BaseHtMaterialOwnerServiceImpl extends BaseService<BaseHtMaterialOwner> implements BaseHtMaterialOwnerService {

    @Resource
    private BaseHtMaterialOwnerMapper baseHtMaterialOwnerMapper;

    @Override
    public List<BaseHtMaterialOwner> findHtList(Map<String, Object> map) {
        return baseHtMaterialOwnerMapper.findHtList(map);
    }
}
