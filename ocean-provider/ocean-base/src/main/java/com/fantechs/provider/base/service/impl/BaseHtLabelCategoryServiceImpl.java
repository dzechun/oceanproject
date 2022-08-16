package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtLabelCategory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabelCategory;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtLabelCategoryMapper;
import com.fantechs.provider.base.service.BaseHtLabelCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
* @author Mr.Lei
* @create 2020/12/17.
*/
@Service
public class BaseHtLabelCategoryServiceImpl extends BaseService<BaseHtLabelCategory> implements BaseHtLabelCategoryService {

         @Resource
         private BaseHtLabelCategoryMapper baseHtLabelCategoryMapper;

    @Override
    public List<BaseHtLabelCategory> findList(Map<String, Object> map) {
        return baseHtLabelCategoryMapper.findList(map);
    }
}
