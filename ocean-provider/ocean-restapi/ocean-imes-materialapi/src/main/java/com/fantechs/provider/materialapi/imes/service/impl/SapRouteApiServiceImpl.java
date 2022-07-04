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
import com.fantechs.provider.materialapi.imes.service.SapRouteApiService;
import com.fantechs.provider.materialapi.imes.utils.BaseUtils;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.materialapi.imes.utils.ProLineApi.SIMESPROCESSQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.ProLineApi.SIMESPROCESSQUERYOutService;
import io.seata.spring.annotation.GlobalTransactional;

import javax.annotation.Resource;
import java.net.Authenticator;
import java.text.ParseException;
import java.util.List;


@org.springframework.stereotype.Service
public class SapRouteApiServiceImpl implements SapRouteApiService {

    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private LogsUtils logsUtils;
    @Resource
    private BaseUtils baseUtils;

    private String userName = "MESPIALEUSER"; //雷赛wsdl用户名
    private String password = "1234qwer"; //雷赛wsdl密码

    @Override
    @GlobalTransactional
    public int getRoute(SearchSapRouteApi searchSapRouteApi) throws ParseException {
        Authenticator.setDefault(new BasicAuthenticator(userName, password));
        SIMESPROCESSQUERYOutService service = new SIMESPROCESSQUERYOutService();
        SIMESPROCESSQUERYOut out = service.getHTTPPort();
        DTMESPROCESSQUERYREQ req = new DTMESPROCESSQUERYREQ();
        List<BaseOrganizationDto> orgIdList = baseUtils.getOrId();
        if(StringUtils.isEmpty(orgIdList)) throw new BizErrorException("未查询到对应组织");
        Long orgId = orgIdList.get(0).getOrganizationId();
        if(StringUtils.isEmpty(searchSapRouteApi.getMaterialCode()) || StringUtils.isEmpty(searchSapRouteApi.getWerks()))
            throw new BizErrorException("物料编码和工厂不能为空");
        req.setMATNR(searchSapRouteApi.getMaterialCode());
        req.setWERKS(searchSapRouteApi.getWerks());
        DTMESPROCESSQUERYRES res = out.siMESPROCESSQUERYOut(req);
        if(StringUtils.isNotEmpty(res) && "S".equals(res.getTYPE()) ){
            if(StringUtils.isEmpty(res.getPROCESS())) throw new BizErrorException("请求结果为空");
            //保存或更新工艺路线
           /* BaseMaterial baseMaterial = baseUtils.getBaseMaterial(baseUtils.removeZero(searchSapRouteApi.getMaterialCode()));
            if(StringUtils.isEmpty(baseMaterial)) throw new BizErrorException("未查询到对应的物料信息");

            //保存工艺路线
            BaseRoute baseRoute = new BaseRoute();
            baseRoute.setRouteName(res.getPROCESS().get(0).getKTEXT());
            baseRoute.setRouteDesc(res.getPROCESS().get(0).getKTEXT());
            baseRoute.setRouteCode(baseMaterial.getMaterialCode());
            baseRoute.setRouteType(1);
            baseRoute.setStatus(1);
            baseRoute.setIsDelete((byte)1);
            baseRoute.setOrganizationId(orgId);
            baseFeignApi.addOrUpdate(baseRoute);

            //保存产品工艺路线
            BaseProductProcessRoute  baseProductProcessRoute = new BaseProductProcessRoute();
            baseProductProcessRoute.setProductType(0);
            baseProductProcessRoute.setMaterialId(baseUtils.getBaseMaterial(searchSapRouteApi.getMaterialCode()).getMaterialId());
            baseProductProcessRoute.setRouteId(baseRoute.getRouteId());
            baseProductProcessRoute.setStatus(1);
            baseProductProcessRoute.setOrganizationId(orgId);
            baseProductProcessRoute.setIsDelete((byte)1);
            baseFeignApi.addOrUpdate(baseProductProcessRoute);*/


            //保存工段
            for(DTMESPROCESS process: res.getPROCESS()){
                BaseWorkshopSection baseWorkshopSection = new BaseWorkshopSection();
                baseWorkshopSection.setSectionCode(process.getARBPL());
                baseWorkshopSection.setSectionName(process.getLTXA1());
                baseWorkshopSection.setSectionDesc(process.getLTXA1());
                baseWorkshopSection.setStatus((byte)1);
                baseWorkshopSection.setOrganizationId(orgId);
                baseWorkshopSection.setIsDelete((byte)1);
                baseFeignApi.addOrUpdate(baseWorkshopSection);
            }
            //保存工序类别
       /*     BaseProcessCategory baseProcessCategory = new BaseProcessCategory();
            baseProcessCategory.setProcessCategoryName(res.getPROCESS().get(0).getKTEXT());
            baseProcessCategory.setProcessCategoryDesc(res.getPROCESS().get(0).getKTEXT());
            baseProcessCategory.setProcessCategoryCode(res.getPROCESS().get(0).getKTEXT());
            baseProcessCategory.setStatus((byte)1);
            baseProcessCategory.setOrganizationId(orgId);
            baseProcessCategory.setIsDelete((byte)1);
            ResponseEntity<BaseProcessCategory> baseProcessCategoryResponseEntity = baseFeignApi.addOrUpdate(baseProcessCategory);

            //保存工序
            for(DTMESPROCESS process: res.getPROCESS()){
                BaseProcess  baseProcess = new BaseProcess();
                baseProcess.setProcessName(process.getLTXA1());
                baseProcess.setProcessDesc(process.getLTXA1());
                baseProcess.setProcessCode(baseUtils.removeZero(process.getVORNR()));
                baseProcess.setSectionId(baseWorkshopSectionResponseEntity.getData().getSectionId());
                baseProcess.setSectionCode(baseWorkshopSectionResponseEntity.getData().getSectionCode());
                baseProcess.setOrganizationId(orgId);
                baseProcess.setProcessCategoryId(baseProcessCategoryResponseEntity.getData().getProcessCategoryId());
                baseProcess.setStatus(1);
                baseProcess.setIsDelete((byte)1);
                baseFeignApi.addOrUpdate(baseProcess);
            }*/
            logsUtils.addlog((byte)1,(byte)1,orgId,null,req.toString());
            return 1;
        }else{
            logsUtils.addlog((byte)0,(byte)1,orgId,res.getMESSAGE(),req.toString());
            throw new BizErrorException("接口请求失败,错误信息为："+res.getMESSAGE());
        }
    }

}
