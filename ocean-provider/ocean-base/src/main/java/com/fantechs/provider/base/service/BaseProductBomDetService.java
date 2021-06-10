package com.fantechs.provider.base.service;

import com.fantechs.common.base.general.entity.basic.BaseProductBom;
import com.fantechs.common.base.general.entity.basic.BaseProductBomDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBomDet;
import com.fantechs.common.base.support.IService;

import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */

public interface BaseProductBomDetService extends IService<BaseProductBomDet> {

    List<BaseProductBomDet> findList(SearchBaseProductBomDet searchBaseProductBomDet);

    List<BaseProductBomDet> findNextLevelProductBomDet(Long productBomDetId);

    BaseProductBomDet addOrUpdate (BaseProductBomDet baseProductBomDet);

    int batchApiDelete(List<BaseProductBomDet> bseProductBomDets);
}
