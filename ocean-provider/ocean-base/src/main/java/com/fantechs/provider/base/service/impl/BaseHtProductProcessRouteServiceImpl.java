package com.fantechs.provider.base.service.impl;

import com.fantechs.common.base.general.entity.basic.history.BaseHtProductProcessRoute;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductProcessRoute;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.base.mapper.BaseHtProductProcessRouteMapper;
import com.fantechs.provider.base.service.BaseHtProductProcessRouteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/30.
 */
@Service
public class BaseHtProductProcessRouteServiceImpl extends BaseService<BaseHtProductProcessRoute> implements BaseHtProductProcessRouteService {

    @Resource
    private BaseHtProductProcessRouteMapper baseHtProductProcessRouteMapper;

    @Override
    public List<BaseHtProductProcessRoute> findList(SearchBaseProductProcessRoute searchBaseProductProcessRoute) {
        List<BaseHtProductProcessRoute> list = baseHtProductProcessRouteMapper.findList(searchBaseProductProcessRoute);
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
