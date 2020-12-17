package com.fantechs.provider.bcm.service.impl;

import com.fantechs.common.base.general.dto.bcm.BcmLabelMaterialDto;
import com.fantechs.common.base.general.entity.bcm.history.BcmHtLabelMaterial;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabelMaterial;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.bcm.mapper.BcmHtLabelMaterialMapper;
import com.fantechs.provider.bcm.service.BcmHtLabelMaterialService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@Service
public class BcmHtLabelMaterialServiceImpl  extends BaseService<BcmHtLabelMaterial> implements BcmHtLabelMaterialService {

         @Resource
         private BcmHtLabelMaterialMapper bcmHtLabelMaterialMapper;

    @Override
    public List<BcmLabelMaterialDto> findList(SearchBcmLabelMaterial searchBcmLabelMaterial) {
        return bcmHtLabelMaterialMapper.findList(searchBcmLabelMaterial);
    }
}
