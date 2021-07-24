package com.fantechs.provider.baseapi.esop.service.impl;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.*;
import com.fantechs.common.base.general.entity.basic.*;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.baseapi.esop.service.SapRouteApiService;
import com.fantechs.provider.baseapi.esop.utils.BasicAuthenticator;
import com.fantechs.provider.baseapi.esop.utils.ProLineApi.SIMESPROCESSQUERYOut;
import com.fantechs.provider.baseapi.esop.utils.ProLineApi.SIMESPROCESSQUERYOutService;

import javax.annotation.Resource;
import java.net.Authenticator;
import java.util.List;


@org.springframework.stereotype.Service
public class SapRouteApiServiceImpl implements SapRouteApiService {

    @Resource
    private BaseFeignApi baseFeignApi;

    private String userName = "MESPIALEUSER"; //雷赛wsdl用户名
    private String password = "1234qwer"; //雷赛wsdl密码

    @Override
    public int getRoute(SearchSapRouteApi searchSapRouteApi) {
        Authenticator.setDefault(new BasicAuthenticator(userName, password));
        SIMESPROCESSQUERYOutService service = new SIMESPROCESSQUERYOutService();
        SIMESPROCESSQUERYOut out = service.getHTTPPort();
        DTMESPROCESSQUERYREQ req = new DTMESPROCESSQUERYREQ();
        if(StringUtils.isEmpty(searchSapRouteApi.getMaterialCode()) || StringUtils.isEmpty(searchSapRouteApi.getWerks()))
            throw new BizErrorException("物料编码和工厂不能为空");
        req.setMATNR(searchSapRouteApi.getMaterialCode());
        req.setWERKS(searchSapRouteApi.getWerks());
        DTMESPROCESSQUERYRES res = out.siMESPROCESSQUERYOut(req);
        if(StringUtils.isNotEmpty(res) && "S".equals(res.getTYPE()) ){
            if(StringUtils.isEmpty(res.getPROCESS())) throw new BizErrorException("请求结果为空");
            //保存或更新工艺路线
            BaseMaterial baseMaterial = getBaseMaterial(searchSapRouteApi.getMaterialCode());
            if(StringUtils.isEmpty(baseMaterial)) throw new BizErrorException("未查询到对应的物料信息");
            Long orgId = getOrId();
            //保存工艺路线
            BaseRoute baseRoute = new BaseRoute();
            baseRoute.setRouteName(res.getPROCESS().get(0).getKTEXT());
            baseRoute.setRouteDesc(res.getPROCESS().get(0).getKTEXT());
            baseRoute.setRouteCode(baseMaterial.getMaterialCode());
            baseRoute.setRouteType(1);
            baseRoute.setStatus(1);
            baseRoute.setOrganizationId(orgId);
            baseFeignApi.addOrUpdate(baseRoute);

            //保存产品工艺路线
            BaseProductProcessRoute  baseProductProcessRoute = new BaseProductProcessRoute();
            baseProductProcessRoute.setProductType(0);
            baseProductProcessRoute.setMaterialId(getBaseMaterial(searchSapRouteApi.getMaterialCode()).getMaterialId());
            baseProductProcessRoute.setRouteId(baseRoute.getRouteId());
            baseProductProcessRoute.setStatus(1);
            baseProductProcessRoute.setOrganizationId(orgId);
            baseFeignApi.addOrUpdate(baseProductProcessRoute);


            //保存工段
            BaseWorkshopSection baseWorkshopSection = new BaseWorkshopSection();
            baseWorkshopSection.setSectionCode(baseMaterial.getMaterialCode());
            baseWorkshopSection.setSectionName(res.getPROCESS().get(0).getKTEXT());
            baseWorkshopSection.setSectionDesc(res.getPROCESS().get(0).getKTEXT());
            baseWorkshopSection.setStatus((byte)1);
            baseWorkshopSection.setOrganizationId(orgId);
            ResponseEntity<BaseWorkshopSection> baseWorkshopSectionResponseEntity = baseFeignApi.addOrUpdate(baseWorkshopSection);

            //保存工序类别
            BaseProcessCategory baseProcessCategory = new BaseProcessCategory();
            baseProcessCategory.setProcessCategoryName(res.getPROCESS().get(0).getKTEXT());
            baseProcessCategory.setProcessCategoryDesc(res.getPROCESS().get(0).getKTEXT());
            baseProcessCategory.setProcessCategoryCode(res.getPROCESS().get(0).getKTEXT());
            baseProcessCategory.setStatus((byte)1);
            baseProcessCategory.setOrganizationId(orgId);
            ResponseEntity<BaseProcessCategory> baseProcessCategoryResponseEntity = baseFeignApi.addOrUpdate(baseProcessCategory);

            System.out.println("----baseWorkshopSection----"+baseWorkshopSection);
            //保存工序
            for(DTMESPROCESS process: res.getPROCESS()){
                BaseProcess  baseProcess = new BaseProcess();
                baseProcess.setProcessName(process.getLTXA1());
                baseProcess.setProcessDesc(process.getLTXA1());
                baseProcess.setProcessCode(process.getVORNR());
                baseProcess.setSectionId(baseWorkshopSectionResponseEntity.getData().getSectionId());
                baseProcess.setSectionCode(baseWorkshopSectionResponseEntity.getData().getSectionCode());
                baseProcess.setOrganizationId(orgId);
                baseProcess.setProcessCategoryId(baseProcessCategoryResponseEntity.getData().getProcessCategoryId());
                baseFeignApi.addOrUpdate(baseProcess);
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
