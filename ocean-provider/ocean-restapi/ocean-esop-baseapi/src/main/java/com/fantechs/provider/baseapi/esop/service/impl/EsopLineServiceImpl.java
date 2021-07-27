package com.fantechs.provider.baseapi.esop.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.general.dto.basic.BaseWorkShopDto;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkShop;
import com.fantechs.common.base.general.entity.restapi.esop.EsopLine;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.baseapi.esop.mapper.EsopLineMapper;
import com.fantechs.provider.baseapi.esop.service.EsopLineService;
import com.fantechs.provider.baseapi.esop.service.EsopWorkshopService;
import com.fantechs.provider.baseapi.esop.util.LogsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class EsopLineServiceImpl extends BaseService<EsopLine> implements EsopLineService {
    private static final Logger log = LoggerFactory.getLogger(EsopLineServiceImpl.class);

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
        List<BaseProLine> baseLines = new ArrayList<BaseProLine>();
        if(StringUtils.isNotEmpty(list)){
            for(EsopLine esopLine :list){
                baseLines.add(getEsopLine(esopLine));
            }
        }
        log.info("----------baseLines---------"+baseLines.size());
        log.info("----------baseLines---------"+baseLines.get(0));
        ResponseEntity<List<BaseProLine>> baseProLineList = baseFeignApi.batchAddLine(baseLines);
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
        searchBaseWorkShop.setOrgId((long)1002);
        ResponseEntity<List<BaseWorkShopDto>> workShopList = baseFeignApi.findWorkShopList(searchBaseWorkShop);

        if(StringUtils.isNotEmpty(workShopList.getData())) {
            baseProLine.setWorkShopId(workShopList.getData().get(0).getWorkShopId());
            baseProLine.setFactoryId(workShopList.getData().get(0).getFactoryId());
        }
        /*else{
            //数据库中没查到则通过code去esop库中同步
            Map<String, Object> map = new HashMap();
            map.put("workShopCode",esopLine.getWorkshopCode());
            List<BaseWorkShop> baseWorkShops = esopWorkshopService.addWorkshop(map);
            if(StringUtils.isNotEmpty(baseWorkShops)) {
                baseProLine.setWorkShopId(baseWorkShops.get(0).getWorkShopId());
                baseProLine.setFactoryId(baseWorkShops.get(0).getFactoryId());
            }
        }*/

        baseProLine.setCreateTime(esopLine.getCreatedTime());
        baseProLine.setModifiedTime(esopLine.getModifyTime());
        baseProLine.setIsDelete(esopLine.getIsDeleted());
        baseProLine.setOrganizationId((long)1002);
        return baseProLine;
    }
}
