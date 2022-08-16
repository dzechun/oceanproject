package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtBarcodeRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtBarcodeRuleMapper;
import com.fantechs.provider.base.service.BaseHtBarcodeRuleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/10/26.
 */
@Service
public class BaseHtBarcodeRuleServiceImpl extends BaseService<BaseHtBarcodeRule> implements BaseHtBarcodeRuleService {

     @Resource
     private BaseHtBarcodeRuleMapper baseHtBarcodeRuleMapper;

     @Override
     public List<BaseHtBarcodeRule> findList(Map<String, Object> map) {
          SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
          map.put("orgId", user.getOrganizationId());
        return baseHtBarcodeRuleMapper.findList(map);
     }
}
