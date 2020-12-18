package com.fantechs.provider.bcm.service;

import com.fantechs.common.base.general.dto.bcm.BcmLabelMaterialDto;
import com.fantechs.common.base.general.entity.bcm.BcmLabelMaterial;
import com.fantechs.common.base.general.entity.bcm.history.BcmHtLabelMaterial;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabelMaterial;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
public interface BcmHtLabelMaterialService extends IService<BcmHtLabelMaterial> {
    List<BcmLabelMaterialDto> findList(SearchBcmLabelMaterial searchBcmLabelMaterial);
}
