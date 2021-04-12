package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtKeyMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtKeyMaterialMapper;
import com.fantechs.provider.base.service.BaseHtKeyMaterialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/11/24.
 */
@Service
public class BaseHtKeyMaterialServiceImpl extends BaseService<BaseHtKeyMaterial> implements BaseHtKeyMaterialService {

    @Resource
    private BaseHtKeyMaterialMapper baseHtKeyMaterialMapper;

    @Override
    public List<BaseHtKeyMaterial> findHtList(Map<String, Object> map) {
        return baseHtKeyMaterialMapper.findHtList(map);
    }
}
