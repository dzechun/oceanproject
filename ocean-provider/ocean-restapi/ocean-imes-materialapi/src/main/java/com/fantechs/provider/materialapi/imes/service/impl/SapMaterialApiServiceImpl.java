package com.fantechs.provider.materialapi.imes.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
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
import com.fantechs.provider.materialapi.imes.utils.BaseUtils;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.materialapi.imes.utils.materialApi.SIMESMATERIALQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.materialApi.SIMESMATERIALQUERYOutService;
import com.fantechs.provider.materialapi.imes.service.SapMaterialApiService;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;

import javax.annotation.Resource;
import java.net.Authenticator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@org.springframework.stereotype.Service
public class SapMaterialApiServiceImpl implements SapMaterialApiService {

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
    public int getMaterial(SearchSapMaterialApi searchSapMaterialApi) throws ParseException {
        Authenticator.setDefault(new BasicAuthenticator(userName, password));
        SIMESMATERIALQUERYOutService service = new SIMESMATERIALQUERYOutService();
        SIMESMATERIALQUERYOut out = service.getHTTPPort();
        DTMESMATERIALQUERYREQ req = new DTMESMATERIALQUERYREQ();
        Long orgId =baseUtils.getOrId();
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
            logsUtils.addlog((byte)1,(byte)1,orgId,null,req.toString());
            return 1;
        }else{
            logsUtils.addlog((byte)0,(byte)1,orgId,res.toString(),req.toString());
            throw new BizErrorException("接口请求失败");
        }
    }

    public void saveAndUpdate(DTMESMATERIAL material, List<BaseMaterial> addList, List<BaseMaterial> updateList){
        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        String materialCode =baseUtils.removeZero(material.getMATNR());
        Long orgId = baseUtils.getOrId();
        searchBaseMaterial.setMaterialCode(materialCode);
        searchBaseMaterial.setOrganizationId(orgId);
        ResponseEntity<List<BaseMaterial>> list = baseFeignApi.findList(searchBaseMaterial);
        BaseMaterial baseMaterial = new BaseMaterial();
        baseMaterial.setOrganizationId(orgId);

        if(StringUtils.isEmpty(list.getData())){
            baseMaterial.setMaterialName(material.getMAKTX());
            baseMaterial.setMaterialDesc(material.getMAKTX());
            baseMaterial.setMaterialCode(materialCode);
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
