package com.fantechs.provider.baseapi.esop.service;

import com.fantechs.common.base.general.entity.basic.BaseWorkShop;
import com.fantechs.common.base.general.entity.restapi.esop.EsopWorkshop;
import com.fantechs.common.base.support.IService;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/21.
 */

public interface EsopWorkshopService extends IService<EsopWorkshop> {
     List<BaseWorkShop> addWorkshop(Map<String, Object> map) throws ParseException;
}
