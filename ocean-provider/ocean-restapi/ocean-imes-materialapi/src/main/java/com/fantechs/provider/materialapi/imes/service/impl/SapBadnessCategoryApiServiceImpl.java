package com.fantechs.provider.materialapi.imes.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.*;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.materialapi.imes.utils.BaseUtils;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.materialapi.imes.utils.badnessCategoryApi.SIMESBADCODEQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.badnessCategoryApi.SIMESBADCODEQUERYOutService;
import com.fantechs.provider.materialapi.imes.service.SapBadnessCategoryApiService;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.net.Authenticator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@org.springframework.stereotype.Service
public class SapBadnessCategoryApiServiceImpl implements SapBadnessCategoryApiService {
    private static final Logger logger = LoggerFactory.getLogger(SapBadnessCategoryApiServiceImpl.class);

    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private BaseUtils baseUtils;
    @Resource
    private LogsUtils logsUtils;

    private String userName = "MESPIALEUSER"; //雷赛wsdl用户名
    private String password = "1234qwer"; //雷赛wsdl密码

    @Override
    @LcnTransaction
    public int getbadnessCategory(SearchSapBadnessCategoryApi searchSapBadnessCategoryApi) throws ParseException {
        Authenticator.setDefault(new BasicAuthenticator(userName, password));
        SIMESBADCODEQUERYOutService service = new SIMESBADCODEQUERYOutService();
        SIMESBADCODEQUERYOut out = service.getHTTPPort();
        DTMESBADCODEQUERYREQ req = new DTMESBADCODEQUERYREQ();
        if(StringUtils.isEmpty(searchSapBadnessCategoryApi.getCatalogue()) || StringUtils.isEmpty(searchSapBadnessCategoryApi.getBadnessCodes()))
            throw new BizErrorException("目录和代码组不能为空");
        req.setKATALOGART(searchSapBadnessCategoryApi.getCatalogue());
        req.setCODEGRUPPE(searchSapBadnessCategoryApi.getBadnessCodes());
        DTMESBADCODEQUERYRES res = out.siMESBADCODEQUERYOut(req);
        Long orgId = baseUtils.getOrId();
        if(StringUtils.isNotEmpty(res) && "S".equals(res.getTYPE()) ){
            if(StringUtils.isEmpty(res.getBADCODE())) throw new BizErrorException("请求结果为空");
            //保存或更新不良代类别
            BaseBadnessCategory baseBadnessCategory = new BaseBadnessCategory();
            baseBadnessCategory.setBadnessCategoryCode(baseUtils.removeZero(searchSapBadnessCategoryApi.getBadnessCodes()));
            baseBadnessCategory.setBadnessCategoryDesc(searchSapBadnessCategoryApi.getCatalogue());
            baseBadnessCategory.setOrgId(orgId);
            baseBadnessCategory.setStatus((byte)1);
            ResponseEntity<BaseBadnessCategory> baseBadnessCategoryResponseEntity = baseFeignApi.saveByApi(baseBadnessCategory);

            //保存产品工艺路线
            for(DTMESBADCODE badCode: res.getBADCODE()) {
                BaseBadnessCause baseBadnessCause = new BaseBadnessCause();
                baseBadnessCause.setBadnessCauseCode(baseUtils.removeZero(badCode.getCODE()));
                baseBadnessCause.setBadnessCauseDesc(badCode.getKURZTEXT());
                baseBadnessCause.setBadnessCategoryId(baseBadnessCategoryResponseEntity.getData().getBadnessCategoryId());
                baseBadnessCause.setOrgId(orgId);
                baseFeignApi.saveByApi(baseBadnessCause);
            }
            logsUtils.addlog((byte)1,(byte)1,orgId,null,req.toString());
            return 1;
        }else{
            logsUtils.addlog((byte)0,(byte)1,orgId,res.toString(),req.toString());
            throw new BizErrorException("接口请求失败");
        }
    }

}
