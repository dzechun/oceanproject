package com.fantechs.provider.base.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductBom;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBom;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProductBomMapper;
import com.fantechs.provider.base.service.BaseHtProductBomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/12.
 */
@Service
public class BaseHtProductBomServiceImpl extends BaseService<BaseHtProductBom> implements BaseHtProductBomService {

         @Resource
         private BaseHtProductBomMapper baseHtProductBomMapper;


        @Override
        public List<BaseHtProductBom> findList(Map<String, Object> map) {
            SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
            map.put("orgId", user.getOrganizationId());
            return baseHtProductBomMapper.findList(map);
        }



}
