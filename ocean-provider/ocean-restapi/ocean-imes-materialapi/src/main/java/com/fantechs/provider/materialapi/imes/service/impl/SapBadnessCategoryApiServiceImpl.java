package com.fantechs.provider.materialapi.imes.service.impl;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.*;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.materialapi.imes.service.SapBadnessCategoryApiService;
import com.fantechs.provider.materialapi.imes.service.SapMaterialApiService;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;
import com.fantechs.provider.materialapi.imes.utils.ProLineApi.SIMESPROCESSQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.ProLineApi.SIMESPROCESSQUERYOutService;
import com.fantechs.provider.materialapi.imes.utils.badnessCategoryApi.SIMESBADCODEQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.badnessCategoryApi.SIMESBADCODEQUERYOutService;
import com.fantechs.provider.materialapi.imes.utils.materialApi.SIMESMATERIALQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.materialApi.SIMESMATERIALQUERYOutService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@org.springframework.stereotype.Service
public class SapBadnessCategoryApiServiceImpl implements SapBadnessCategoryApiService {

    @Resource
    private BaseFeignApi baseFeignApi;

    private String userName = "MESPIALEUSER"; //雷赛wsdl用户名
    private String password = "1234qwer"; //雷赛wsdl密码

    @Override
    public int getbadnessCategory(SearchSapBadnessCategoryApi searchSapBadnessCategoryApi) {
        Authenticator.setDefault(new BasicAuthenticator(userName, password));
        SIMESBADCODEQUERYOutService service = new SIMESBADCODEQUERYOutService();
        SIMESBADCODEQUERYOut out = service.getHTTPPort();
        DTMESBADCODEQUERYREQ req = new DTMESBADCODEQUERYREQ();
        if(StringUtils.isEmpty(searchSapBadnessCategoryApi.getCatalogue()) || StringUtils.isEmpty(searchSapBadnessCategoryApi.getBadnessCodes()))
            throw new BizErrorException("目录和代码组不能为空");
        req.setKATALOGART(searchSapBadnessCategoryApi.getCatalogue());
        req.setCODEGRUPPE(searchSapBadnessCategoryApi.getBadnessCodes());
        DTMESBADCODEQUERYRES res = out.siMESBADCODEQUERYOut(req);
        if(StringUtils.isNotEmpty(res) && "S".equals(res.getTYPE()) ){
            if(StringUtils.isEmpty(res.getBADCODE())) throw new BizErrorException("请求结果为空");
            Long orgId = getOrId();
            //保存或更新不良代类别
            BaseBadnessCategory baseBadnessCategory = new BaseBadnessCategory();
            baseBadnessCategory.setBadnessCategoryCode(searchSapBadnessCategoryApi.getBadnessCodes());
            baseBadnessCategory.setBadnessCategoryDesc(searchSapBadnessCategoryApi.getCatalogue());
            baseBadnessCategory.setOrgId(orgId);
            baseBadnessCategory.setStatus((byte)1);
            ResponseEntity<BaseBadnessCategory> baseBadnessCategoryResponseEntity = baseFeignApi.addOrUpdate(baseBadnessCategory);

            //保存产品工艺路线
            for(DTMESBADCODE badCode: res.getBADCODE()) {
                BaseBadnessCause baseBadnessCause = new BaseBadnessCause();
                baseBadnessCause.setBadnessCauseCode(badCode.getCODE());
                baseBadnessCause.setBadnessCauseDesc(badCode.getKURZTEXT());
                baseBadnessCause.setBadnessCategoryId(baseBadnessCategoryResponseEntity.getData().getBadnessCategoryId());
                baseBadnessCause.setOrgId(orgId);
                baseFeignApi.addOrUpdate(baseBadnessCause);
            }
            return 1;
        }else{
            throw new BizErrorException("接口请求失败");
        }
    }

    public BaseMaterial getBaseMaterial(String materialCode){
        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialCode(materialCode);
        searchBaseMaterial.setOrganizationId(getOrId());
        ResponseEntity<List<BaseMaterial>> parentMaterialList = baseFeignApi.findSmtMaterialList(searchBaseMaterial);
        if(StringUtils.isEmpty(parentMaterialList.getData()))
            throw new BizErrorException("未查询到对应的物料："+materialCode);
        return parentMaterialList.getData().get(0);
    }

    public Long getOrId() {
        SearchBaseOrganization searchBaseOrganization = new SearchBaseOrganization();
        searchBaseOrganization.setOrganizationName("雷赛");
        ResponseEntity<List<BaseOrganizationDto>> organizationList = baseFeignApi.findOrganizationList(searchBaseOrganization);
        if (StringUtils.isEmpty(organizationList.getData())) throw new BizErrorException("未查询到对应组织");
        return organizationList.getData().get(0).getOrganizationId();
    }
}
