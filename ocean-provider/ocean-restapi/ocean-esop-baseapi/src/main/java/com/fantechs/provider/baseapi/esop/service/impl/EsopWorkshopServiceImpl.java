package com.fantechs.provider.baseapi.esop.service.impl;


import com.fantechs.common.base.general.dto.basic.BaseFactoryDto;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.entity.basic.BaseWorkShop;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseFactory;
import com.fantechs.common.base.general.entity.restapi.esop.EsopWorkshop;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.baseapi.esop.mapper.EsopWorkshopMapper;
import com.fantechs.provider.baseapi.esop.service.EsopWorkshopService;
import com.fantechs.provider.baseapi.esop.util.BaseUtils;
import com.fantechs.provider.baseapi.esop.util.LogsUtils;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/07/21.
 */
@Service
public class EsopWorkshopServiceImpl extends BaseService<EsopWorkshop> implements EsopWorkshopService {

    @Resource
    private EsopWorkshopMapper esopWorkshopMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private LogsUtils logsUtils;
    @Resource
    private BaseUtils baseUtils;

  @Override
  @GlobalTransactional
    public List<BaseWorkShop> addWorkshop(Map<String, Object> map) throws ParseException {
      List<EsopWorkshop> list = esopWorkshopMapper.findList(map);
      List<BaseWorkShop> baseWorkShops = new ArrayList<BaseWorkShop>();
      List<BaseOrganizationDto> baseOrganizationDtos = baseUtils.getOrId();
      if(StringUtils.isNotEmpty(list) && StringUtils.isNotEmpty(baseOrganizationDtos)){
          for(BaseOrganizationDto dto : baseOrganizationDtos) {
              for (EsopWorkshop esopWorkshop : list) {
                  baseWorkShops.add(getBaseWorkshop(esopWorkshop, dto.getOrganizationId()));
              }
          }
      }
      ResponseEntity<List<BaseWorkShop>> baseWorkShopList = baseFeignApi.batchAddWorkshop(baseWorkShops);
  //    logsUtils.addlog((byte)1,(byte)1,orgId,null,null);
      return baseWorkShopList.getData();
  }

    public BaseWorkShop getBaseWorkshop(EsopWorkshop esopWorkshop ,Long orgId){
        BaseWorkShop baseWorkShop = new BaseWorkShop();
        baseWorkShop.setWorkShopCode(esopWorkshop.getCode());
        baseWorkShop.setWorkShopName(esopWorkshop.getName());
        baseWorkShop.setWorkShopDesc(esopWorkshop.getLongName());
        //新宝未使用工厂，默认工厂关联部门与车间
        SearchBaseFactory searchBaseFactory = new SearchBaseFactory();
        searchBaseFactory.setOrgId(orgId);
        ResponseEntity<List<BaseFactoryDto>> factoryList = baseFeignApi.findFactoryList(searchBaseFactory);
        if(StringUtils.isNotEmpty(factoryList.getData()))
            baseWorkShop.setFactoryId(factoryList.getData().get(0).getFactoryId());
        baseWorkShop.setCreateTime(esopWorkshop.getCreatedTime());
        baseWorkShop.setModifiedTime(esopWorkshop.getModifyTime());
        baseWorkShop.setIsDelete(esopWorkshop.getIsDeleted());
        baseWorkShop.setOrganizationId(orgId);
        baseWorkShop.setIsDelete((byte)1);
        baseWorkShop.setStatus(1);
        return baseWorkShop;
    }
}
