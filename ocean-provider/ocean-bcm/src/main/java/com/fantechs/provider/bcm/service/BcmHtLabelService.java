package com.fantechs.provider.bcm.service;

import com.fantechs.common.base.general.dto.bcm.BcmLabelDto;
import com.fantechs.common.base.general.entity.bcm.history.BcmHtLabel;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabel;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
public interface BcmHtLabelService extends IService<BcmHtLabel> {
    List<BcmLabelDto> findList(SearchBcmLabel searchBcmLabel);
}
