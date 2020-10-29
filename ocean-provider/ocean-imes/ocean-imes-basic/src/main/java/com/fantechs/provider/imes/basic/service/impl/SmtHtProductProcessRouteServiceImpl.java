package com.fantechs.provider.imes.basic.service.impl;

import com.fantechs.common.base.entity.basic.history.SmtHtProductProcessRoute;
import com.fantechs.common.base.entity.basic.search.SearchSmtProductProcessRoute;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.provider.imes.basic.mapper.SmtHtProductProcessRouteMapper;
import com.fantechs.provider.imes.basic.service.SmtHtProductProcessRouteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Created by wcz on 2020/09/30.
 */
@Service
public class SmtHtProductProcessRouteServiceImpl extends BaseService<SmtHtProductProcessRoute> implements SmtHtProductProcessRouteService {

    @Resource
    private SmtHtProductProcessRouteMapper smtHtProductProcessRouteMapper;

    @Override
    public List<SmtHtProductProcessRoute> findList(SearchSmtProductProcessRoute searchSmtProductProcessRoute) {
        List<SmtHtProductProcessRoute> list = smtHtProductProcessRouteMapper.findList(searchSmtProductProcessRoute);
        for (SmtHtProductProcessRoute smtHtProductProcessRoute : list) {
            Integer productType = smtHtProductProcessRoute.getProductType();
            if(productType==0){
                smtHtProductProcessRoute.setProductName("*");
            }else if (productType==1){
                smtHtProductProcessRoute.setProductName(smtHtProductProcessRoute.getProName());
            }else if (productType==2){
                smtHtProductProcessRoute.setProductName(smtHtProductProcessRoute.getProductModelCode());
            }else if (productType==3){
                smtHtProductProcessRoute.setProductName(smtHtProductProcessRoute.getMaterialCode());
            }
        }
        return list;
    }
}
