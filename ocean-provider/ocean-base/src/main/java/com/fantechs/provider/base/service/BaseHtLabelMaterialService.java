package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.history.BaseHtLabelMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
public interface BaseHtLabelMaterialService extends IService<BaseHtLabelMaterial> {
    List<BaseHtLabelMaterial> findList(SearchBaseLabelMaterial searchBaseLabelMaterial);
}
