package com.fantechs.provider.baseapi.esop.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.common.base.general.dto.basic.BaseWorkShopDto;
import com.fantechs.common.base.general.entity.basic.BaseDept;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseWorkShop;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkShop;
import com.fantechs.common.base.general.entity.restapi.esop.EsopDept;
import com.fantechs.common.base.general.entity.restapi.esop.EsopLine;
import com.fantechs.common.base.general.entity.restapi.esop.EsopWorkshop;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.baseapi.esop.mapper.EsopLineMapper;
import com.fantechs.provider.baseapi.esop.service.EsopLineService;
import com.fantechs.provider.baseapi.esop.service.EsopWorkshopService;
import com.fantechs.provider.baseapi.esop.util.LogsUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 *
 * Created by leifengzhi on 2021/07/21.
 */
@Service
public class EsopLineServiceImpl extends BaseService<EsopLine> implements EsopLineService {

    @Resource
    private EsopLineMapper esopLineMapper;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private LogsUtils logsUtils;
    @Resource
    private EsopWorkshopService esopWorkshopService;


    @Override
    @LcnTransaction
    public List<BaseProLine> addLine(Map<String, Object> map) throws ParseException {
        List<EsopLine> list = esopLineMapper.findList(map);
        List<BaseProLine> esopLines = new ArrayList<BaseProLine>();
        if(StringUtils.isNotEmpty(list)){
            for(EsopLine esopLine :list){
                esopLines.add(getEsopLine(esopLine));
            }
        }
        ResponseEntity<List<BaseProLine>> baseProLineList = baseFeignApi.batchAddLine(esopLines);
        logsUtils.addlog((byte)1,(byte)1,(long)1002,null,null);
        return baseProLineList.getData();
    }

    public BaseProLine getEsopLine(EsopLine esopLine) throws ParseException {
        BaseProLine baseProLine = new BaseProLine();
        baseProLine.setProCode(esopLine.getCode());
        baseProLine.setProName(esopLine.getName());
        baseProLine.setProDesc(esopLine.getShortName());

        SearchBaseWorkShop searchBaseWorkShop = new SearchBaseWorkShop();
        searchBaseWorkShop.setWorkShopCode(esopLine.getWorkshopCode());
        ResponseEntity<List<BaseWorkShopDto>> workShopList = baseFeignApi.findWorkShopList(searchBaseWorkShop);
        if(StringUtils.isNotEmpty(workShopList.getData())) {
            baseProLine.setWorkShopId(workShopList.getData().get(0).getWorkShopId());
            baseProLine.setFactoryId(workShopList.getData().get(0).getFactoryId());
        }else{
            //数据库中没查到则通过code去esop库中同步
            Map<String, Object> map = new HashMap();
            map.put("workShopCode",esopLine.getWorkshopCode());
            List<BaseWorkShop> baseWorkShops = esopWorkshopService.addWorkshop(map);
            baseProLine.setWorkShopId(baseWorkShops.get(0).getWorkShopId());
            baseProLine.setFactoryId(baseWorkShops.get(0).getFactoryId());
        }

        baseProLine.setCreateTime(esopLine.getCreatedTime());
        baseProLine.setModifiedTime(esopLine.getModifyTime());
        baseProLine.setIsDelete(esopLine.getIsDeleted());
        baseProLine.setOrganizationId((long)1002);
        return baseProLine;
    }
}
