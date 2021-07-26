package com.fantechs.provider.materialapi.imes.service.impl;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.DTMESMATERIAL;
import com.fantechs.common.base.general.dto.restapi.DTMESMATERIALQUERYREQ;
import com.fantechs.common.base.general.dto.restapi.DTMESMATERIALQUERYRES;
import com.fantechs.common.base.general.dto.restapi.SearchSapMaterialApi;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.materialapi.imes.utils.materialApi.SIMESMATERIALQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.materialApi.SIMESMATERIALQUERYOutService;
import com.fantechs.provider.materialapi.imes.service.SapMaterialApiService;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;

import javax.annotation.Resource;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@org.springframework.stereotype.Service
public class SapMaterialApiServiceImpl implements SapMaterialApiService {

    @Resource
    private BaseFeignApi baseFeignApi;

    private String userName = "MESPIALEUSER"; //雷赛wsdl用户名
    private String password = "1234qwer"; //雷赛wsdl密码

    @Override
    public int getMaterial(SearchSapMaterialApi searchSapMaterialApi) {
        Authenticator.setDefault(new BasicAuthenticator(userName, password));
        SIMESMATERIALQUERYOutService service = new SIMESMATERIALQUERYOutService();
        SIMESMATERIALQUERYOut out = service.getHTTPPort();
        DTMESMATERIALQUERYREQ req = new DTMESMATERIALQUERYREQ();
        if(StringUtils.isEmpty(searchSapMaterialApi.getStartTime()) || StringUtils.isEmpty(searchSapMaterialApi.getEndTime()))
            throw new BizErrorException("开始和结束时间不能为空");
        req.setERSDA(searchSapMaterialApi.getStartTime());
        req.setERSDAEND(searchSapMaterialApi.getEndTime());
        DTMESMATERIALQUERYRES res = out.siMESMATERIALQUERYOut(req);
        if(StringUtils.isNotEmpty(res) && "S".equals(res.getTYPE())){
            if(StringUtils.isEmpty(res.getMATS())) throw new BizErrorException("请求结果为空");
            List<BaseMaterial> addList = new ArrayList<BaseMaterial>();
            List<BaseMaterial> updateList = new ArrayList<BaseMaterial>();
            for(DTMESMATERIAL material: res.getMATS()){
                if(StringUtils.isEmpty(material.getMATNR())) throw new BizErrorException("新增或更新失败，物料编码为空");
                //判断物料信息为新增或者修改，并填充
                saveAndUpdate(material,addList,updateList);
            }
            baseFeignApi.addList(addList);
            baseFeignApi.batchUpdateByCode(updateList);
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

}
