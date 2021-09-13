package com.fantechs.provider.materialapi.imes.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDto;
import com.fantechs.common.base.general.dto.restapi.*;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProductBom;
import com.fantechs.common.base.general.entity.basic.BaseProductBomDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBom;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.materialapi.imes.utils.BaseUtils;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.materialapi.imes.utils.productBomApi.SIMESBOMQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.productBomApi.SIMESBOMQUERYOutService;
import com.fantechs.provider.materialapi.imes.service.SapProductBomApiService;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.Authenticator;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@org.springframework.stereotype.Service
public class SapProductBomApiServiceImpl implements SapProductBomApiService {

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
    public int getProductBom(SearchSapProductBomApi searchSapProductBomApi) throws ParseException {
        Authenticator.setDefault(new BasicAuthenticator(userName, password));
        SIMESBOMQUERYOutService service = new SIMESBOMQUERYOutService();
        SIMESBOMQUERYOut out = service.getHTTPPort();
        DTMESBOMQUERYREQ req = new DTMESBOMQUERYREQ();
        if(StringUtils.isEmpty(searchSapProductBomApi.getMaterialCode()))
            throw new BizErrorException("物料号不能为空");
        req.setMATNR(searchSapProductBomApi.getMaterialCode());
        req.setOITXT(searchSapProductBomApi.getOitxt());
        DTMESBOMQUERYRES res = out.siMESBOMQUERYOut(req);
        List<BaseOrganizationDto> orgIdList = baseUtils.getOrId();
        if(StringUtils.isEmpty(orgIdList)) throw new BizErrorException("未查询到对应组织");
        Long orgId = orgIdList.get(0).getOrganizationId();
        if(StringUtils.isNotEmpty(res) && "S".equals(res.getTYPE())){
            if(StringUtils.isEmpty(res.getBOM())) throw new BizErrorException("请求结果为空");

            //区分成品以及半成品
            List<DTMESBOM>  parentList = new ArrayList<DTMESBOM>();
            HashSet<String> materialCodes = new HashSet<String>();
            for(DTMESBOM bom: res.getBOM()){
                materialCodes.add(baseUtils.removeZero(bom.getMATNR()));
                if(bom.getMATNR().equals(searchSapProductBomApi.getMaterialCode())){
                    if(StringUtils.isNotEmpty(baseUtils.removeZero(bom.getAENNR())))
                        parentList.add(bom);
                }
            }

            //保存bom表以及det表
            List<BaseMaterial> baseMaterial1 = baseUtils.getBaseMaterial(searchSapProductBomApi.getMaterialCode(),orgId);
            if(StringUtils.isEmpty(baseMaterial1)) throw new BizErrorException("未查询到对应物料编码");

            BaseProductBom parentBomData =  saveBaseProductBom(baseMaterial1.get(0),parentList.get(0).getAENNR());
            BaseProductBom bomData =  null;
            for(String code : materialCodes){
                List<BaseMaterial> baseMaterials = baseUtils.getBaseMaterial(code,orgId);
                if(StringUtils.isEmpty(baseMaterials)) throw new BizErrorException("未查询到对应物料编码");
                 if(searchSapProductBomApi.getMaterialCode().equals(code)){
                     bomData = parentBomData;
                 }else{
                     bomData = saveBaseProductBom(baseMaterials.get(0),"0");
                     BaseProductBomDet subBomDet = new BaseProductBomDet();
                     subBomDet.setProductBomId(parentBomData.getProductBomId());
                     subBomDet.setMaterialId(baseMaterials.get(0).getMaterialId());
                     subBomDet.setIfHaveLowerLevel((byte)1);
                     subBomDet.setIsDelete((byte)1);
                     subBomDet.setStatus((byte)1);
                     baseFeignApi.addOrUpdate(subBomDet);
                 }
                if(StringUtils.isEmpty(bomData))  throw new BizErrorException("保存失败");

                for(DTMESBOM bom: res.getBOM()){
                    if(bom.getMATNR().equals(code)) {
                        BaseProductBomDet baseProductBomDet = new BaseProductBomDet();
                        baseProductBomDet.setProductBomId(bomData.getProductBomId());
                        List<BaseMaterial> bomMaterial = baseUtils.getBaseMaterial(bom.getIDNRK(),orgId);
                        if(StringUtils.isEmpty(bomMaterial)) throw new BizErrorException("未查询到对应物料编码");
                        baseProductBomDet.setMaterialId(bomMaterial.get(0).getMaterialId());
                        baseProductBomDet.setUsageQty(new BigDecimal(bom.getMENGE().trim()));
                        baseProductBomDet.setBaseQty(new BigDecimal(bom.getBMENG().trim()));
                        baseProductBomDet.setRemark(bom.getMAKTXZ());
                        baseProductBomDet.setIfHaveLowerLevel((byte)0);
                        baseProductBomDet.setIsDelete((byte)1);
                        baseProductBomDet.setStatus((byte)1);
                        baseProductBomDet.setOrgId(orgId);
                        baseFeignApi.addOrUpdate(baseProductBomDet);
                    }
                }
            }
            logsUtils.addlog((byte)1,(byte)1,orgId,null,req.toString());
            return 1;
        }else{
            logsUtils.addlog((byte)0,(byte)1,orgId,res.toString(),req.toString());
            throw new BizErrorException("接口请求失败");
        }
    }

/*    public Long getMaterialId(String materialCode){
        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialCode(materialCode);
        ResponseEntity<List<BaseMaterial>> parentMaterialList = baseFeignApi.findSmtMaterialList(searchBaseMaterial);
        if(StringUtils.isEmpty(parentMaterialList.getData()))
            throw new BizErrorException("未查询到对应的物料"+materialCode);
        return parentMaterialList.getData().get(0).getMaterialId();
    }*/

    //保存或更新bom表、如果原bom表已经有数据则删除det表
    public BaseProductBom saveBaseProductBom( BaseMaterial baseMaterial,String productBomVersion){

        SearchBaseProductBom searchBaseProductBom = new SearchBaseProductBom();
        searchBaseProductBom.setMaterialId(baseMaterial.getMaterialId());
        searchBaseProductBom.setProductBomVersion(productBomVersion);
        ResponseEntity<List<BaseProductBomDto>> productBomList = baseFeignApi.findProductBomList(searchBaseProductBom);
        BaseProductBom productBom = new BaseProductBom();
        ResponseEntity<BaseProductBom> pproductBom = null;

        //判断不为空则删除det子集，保存或更新bom表
        if(StringUtils.isNotEmpty(productBomList.getData())){
            baseFeignApi.batchApiDelete(productBomList.getData().get(0).getProductBomId());
        }
        productBom.setProductBomId(productBomList.getData().get(0).getProductBomId());
        productBom.setProductBomVersion(productBomVersion);
        productBom.setMaterialId(baseMaterial.getMaterialId());
        pproductBom = baseFeignApi.addOrUpdate(productBom);
        return pproductBom.getData();
    }

}

