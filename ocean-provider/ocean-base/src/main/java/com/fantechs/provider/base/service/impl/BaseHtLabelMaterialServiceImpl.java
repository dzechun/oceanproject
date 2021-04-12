package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtLabelMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtLabelMaterialMapper;
import com.fantechs.provider.base.service.BaseHtLabelMaterialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@Service
public class BaseHtLabelMaterialServiceImpl extends BaseService<BaseHtLabelMaterial> implements BaseHtLabelMaterialService {

         @Resource
         private BaseHtLabelMaterialMapper baseHtLabelMaterialMapper;

    @Override
    public List<BaseHtLabelMaterial> findList(SearchBaseLabelMaterial searchBaseLabelMaterial) {
        return baseHtLabelMaterialMapper.findList(searchBaseLabelMaterial);
    }
}
