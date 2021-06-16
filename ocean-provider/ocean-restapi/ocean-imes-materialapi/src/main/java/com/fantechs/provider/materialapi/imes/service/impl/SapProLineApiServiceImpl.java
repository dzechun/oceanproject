package com.fantechs.provider.materialapi.imes.service.impl;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.*;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseWorkshopSection;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.materialapi.imes.service.SapProLineApiService;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;
import com.fantechs.provider.materialapi.imes.utils.ProLineApi.SIMESPROCESSQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.ProLineApi.SIMESPROCESSQUERYOutService;

import javax.annotation.Resource;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@org.springframework.stereotype.Service
public class SapProLineApiServiceImpl implements SapProLineApiService {

    @Resource
    private BaseFeignApi baseFeignApi;

    private String userName = "MESPIALEUSER"; //雷赛wsdl用户名
    private String password = "1234qwer"; //雷赛wsdl密码

    @Override
    public int getProLine(SearchSapProLineApi searchSapProLineApi) {
        Authenticator.setDefault(new BasicAuthenticator(userName, password));
        SIMESPROCESSQUERYOutService service = new SIMESPROCESSQUERYOutService();
        SIMESPROCESSQUERYOut out = service.getHTTPPort();
        DTMESPROCESSQUERYREQ req = new DTMESPROCESSQUERYREQ();
        if(StringUtils.isEmpty(searchSapProLineApi.getMaterialCode()) || StringUtils.isEmpty(searchSapProLineApi.getWerks()))
            throw new BizErrorException("物料编码和工厂不能为空");
        req.setMATNR(searchSapProLineApi.getMaterialCode());
        req.setWERKS(searchSapProLineApi.getWerks());
        DTMESPROCESSQUERYRES res = out.siMESPROCESSQUERYOut(req);
        if(StringUtils.isNotEmpty(res) && "S".equals(res.getTYPE())){
            if(StringUtils.isEmpty(res.getPROCESS())) throw new BizErrorException("请求结果为空");
            //保存或更新工艺路线
            BaseMaterial baseMaterial = getBaseMaterial(searchSapProLineApi.getMaterialCode());
            BaseProLine proLine = new BaseProLine();
            proLine.setProName(baseMaterial.getMaterialName());
            proLine.setProDesc(baseMaterial.getMaterialName());
            proLine.setProCode(baseMaterial.getMaterialCode());
            proLine.setStatus(1);
            proLine.setOrganizationId(getOrId());
            baseFeignApi.addOrUpdate(proLine);

            BaseWorkshopSection baseWorkshopSection = new BaseWorkshopSection();
            baseWorkshopSection.setSectionCode(baseMaterial.getMaterialCode());
            baseWorkshopSection.setSectionName(baseMaterial.getMaterialName());
            baseWorkshopSection.setSectionDesc(baseMaterial.getMaterialName());
            baseWorkshopSection.setStatus((byte)1);
            baseWorkshopSection.setOrganizationId(getOrId());
            baseFeignApi.addOrUpdate(baseWorkshopSection);


           /* List<BaseMaterial> addList = new ArrayList<BaseMaterial>();
            List<BaseMaterial> updateList = new ArrayList<BaseMaterial>();
            for(DTMESPROCESS proLine: res.getPROCESS()){
                *//*if(StringUtils.isEmpty(material.getMATNR())) throw new BizErrorException("新增或更新失败，物料编码为空");
                //判断物料信息为新增或者修改，并填充
                saveAndUpdate(material,addList,updateList);*//*
            }*/
            return 1;
        }else{
            throw new BizErrorException("接口请求失败");
        }
    }

    public void saveAndUpdate(DTMESMATERIAL material, List<BaseMaterial> addList, List<BaseMaterial> updateList ){
        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialCode(material.getMATNR());
        ResponseEntity<List<BaseMaterial>> list = baseFeignApi.findList(searchBaseMaterial);
        BaseMaterial baseMaterial = new BaseMaterial();

        SearchBaseOrganization searchBaseOrganization = new SearchBaseOrganization();
        searchBaseOrganization.setOrganizationName("雷赛");
        ResponseEntity<List<BaseOrganizationDto>> organizationList = baseFeignApi.findOrganizationList(searchBaseOrganization);
        if(StringUtils.isEmpty(organizationList.getData()))  throw new BizErrorException("未查询到对应组织");
        baseMaterial.setOrganizationId((organizationList.getData().get(0).getOrganizationId()));

        if(StringUtils.isEmpty(list.getData())){
            baseMaterial.setMaterialName(material.getMAKTX());
            baseMaterial.setMaterialDesc(material.getMAKTX());
            baseMaterial.setMaterialCode(material.getMATNR());
            baseMaterial.setCreateTime(new Date());
            baseMaterial.setModifiedTime(new Date());
            baseMaterial.setStatus((byte)1);
            baseMaterial.setSystemSource("ERP");
            addList.add(baseMaterial);
        }else{
            baseMaterial = list.getData().get(0);
            baseMaterial.setMaterialName(material.getMAKTX());
            baseMaterial.setMaterialDesc(material.getMAKTX());
            baseMaterial.setSystemSource("ERP");
            updateList.add(baseMaterial);
        }

    }

    public BaseMaterial getBaseMaterial(String materialCode){
        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialCode(materialCode);
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
