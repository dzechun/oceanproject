package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.general.entity.basic.history.BaseHtProductBomDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBomDet;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtProductBomDetMapper;
import com.fantechs.provider.base.service.BaseHtProductBomDetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@Service
public class BaseHtProductBomDetServiceImpl extends BaseService<BaseHtProductBomDet> implements BaseHtProductBomDetService {

         @Resource
         private BaseHtProductBomDetMapper baseHtProductBomDetMapper;

        @Override
        public List<BaseHtProductBomDet> findList(SearchBaseProductBomDet searchBaseProductBomDet) {
            return baseHtProductBomDetMapper.findList(searchBaseProductBomDet);
        }
}
