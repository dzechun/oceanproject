package com.fantechs.provider.bcm.service.impl;

import com.fantechs.common.base.general.dto.bcm.BcmLabelDto;
import com.fantechs.common.base.general.entity.bcm.history.BcmHtLabel;
import com.fantechs.common.base.general.entity.bcm.search.SearchBcmLabel;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.bcm.mapper.BcmHtLabelMapper;
import com.fantechs.provider.bcm.service.BcmHtLabelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@Service
public class BcmHtLabelServiceImpl  extends BaseService<BcmHtLabel> implements BcmHtLabelService {

    @Resource
    private BcmHtLabelMapper bcmHtLabelMapper;

    @Override
    public List<BcmLabelDto> findList(SearchBcmLabel searchBcmLabel) {
        return bcmHtLabelMapper.findList(searchBcmLabel);
    }
}
