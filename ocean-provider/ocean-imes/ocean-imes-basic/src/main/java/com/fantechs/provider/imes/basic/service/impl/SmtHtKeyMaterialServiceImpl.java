package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.entity.basic.history.SmtHtKeyMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtKeyMaterialMapper;
import com.fantechs.provider.imes.basic.service.SmtHtKeyMaterialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * Created by leifengzhi on 2020/11/24.
 */
@Service
public class SmtHtKeyMaterialServiceImpl extends BaseService<SmtHtKeyMaterial> implements SmtHtKeyMaterialService {

    @Resource
    private SmtHtKeyMaterialMapper smtHtKeyMaterialMapper;

    @Override
    public List<SmtHtKeyMaterial> findHtList(Map<String, Object> map) {
        return smtHtKeyMaterialMapper.findHtList(map);
    }
}
