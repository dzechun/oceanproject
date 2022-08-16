package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.history.BaseHtProductProcessRoute;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductProcessRoute;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.mapper.BaseHtProductProcessRouteMapper;
import com.fantechs.provider.base.service.BaseHtProductProcessRouteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by wcz on 2020/09/30.
 */
@Service
public class BaseHtProductProcessRouteServiceImpl extends BaseService<BaseHtProductProcessRoute> implements BaseHtProductProcessRouteService {

    @Resource
    private BaseHtProductProcessRouteMapper baseHtProductProcessRouteMapper;

    @Override
    public List<BaseHtProductProcessRoute> findList(Map<String, Object> map) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        map.put("orgId", user.getOrganizationId());
        List<BaseHtProductProcessRoute> list = baseHtProductProcessRouteMapper.findList(map);
        for (BaseHtProductProcessRoute baseHtProductProcessRoute : list) {
            Integer productType = baseHtProductProcessRoute.getProductType();
            if(productType==0){
                baseHtProductProcessRoute.setProductName("*");
            }else if (productType==1){
                baseHtProductProcessRoute.setProductName(baseHtProductProcessRoute.getProName());
            }else if (productType==2){
                baseHtProductProcessRoute.setProductName(baseHtProductProcessRoute.getProductModelCode());
            }else if (productType==3){
                baseHtProductProcessRoute.setProductName(baseHtProductProcessRoute.getMaterialCode());
            }
        }
        return list;
    }


}
