package com.fantechs.provider.materialapi.imes.service.impl;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDto;
import com.fantechs.common.base.general.dto.restapi.*;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProductBom;
import com.fantechs.common.base.general.entity.basic.BaseProductBomDet;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBom;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBomDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.materialapi.imes.service.SapCustomerApiService;
import com.fantechs.provider.materialapi.imes.service.SapProductBomApiService;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;
import com.fantechs.provider.materialapi.imes.utils.customerApi.SIMESCUSTOMERQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.customerApi.SIMESCUSTOMERQUERYOutService;
import com.fantechs.provider.materialapi.imes.utils.productBomApi.SIMESBOMQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.productBomApi.SIMESBOMQUERYOutService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.Authenticator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


@org.springframework.stereotype.Service
public class SapProductBomApiServiceImpl implements SapProductBomApiService {

    @Resource
    private BaseFeignApi baseFeignApi;

    private String userName = "MESPIALEUSER"; //雷赛wsdl用户名
    private String password = "1234qwer"; //雷赛wsdl密码

    @Override
    public int getProductBom(DTMESBOMQUERYREQ dTMESBOMQUERYREQ) {
        Authenticator.setDefault(new BasicAuthenticator(userName, password));
        SIMESBOMQUERYOutService service = new SIMESBOMQUERYOutService();
        SIMESBOMQUERYOut out = service.getHTTPPort();
        DTMESBOMQUERYREQ req = new DTMESBOMQUERYREQ();
        if(StringUtils.isEmpty(dTMESBOMQUERYREQ.getMATNR()))
            throw new BizErrorException("物料号不能为空");
        DTMESBOMQUERYRES res = out.siMESBOMQUERYOut(req);

        if(StringUtils.isNotEmpty(res) && "S".equals(res.getTYPE())){
            if(StringUtils.isEmpty(res.getBOM())) throw new BizErrorException("请求结果为空");

            //区分成品以及半成品
            List<DTMESBOM>  parentList = new ArrayList<DTMESBOM>();
            List<DTMESBOM>  subList = new ArrayList<DTMESBOM>();
            HashSet<String> materialCodes = new HashSet<String>();
            for(DTMESBOM bom: res.getBOM()){
                materialCodes.add(bom.getMATNR());
                if(bom.getMATNR().equals(dTMESBOMQUERYREQ.getMATNR())){
                    parentList.add(bom);
                }else{
                    subList.add(bom);
                }
            }
            //materialCodes.remove(dTMESBOMQUERYREQ.getMATNR());
            System.out.println("------materialCodes-----"+materialCodes);

            //保存bom表以及det表
            BaseProductBom parentBomData =  saveBaseProductBom(dTMESBOMQUERYREQ.getMATNR(),parentList.get(0).getAENNR());
            BaseProductBom bomData =  null;
            for(String code : materialCodes){
                 if(dTMESBOMQUERYREQ.getMATNR().equals(code)){
                     bomData = parentBomData;
                 }else{
                     bomData = saveBaseProductBom(code,"0");

                     BaseProductBomDet subBomDet = new BaseProductBomDet();
                     subBomDet.setProductBomId(parentBomData.getProductBomId());
                     subBomDet.setMaterialId(getMaterialId(code));
                     baseFeignApi.addOrUpdate(subBomDet);
                 }
                if(StringUtils.isEmpty(bomData))  throw new BizErrorException("保存失败");

                for(DTMESBOM bom: res.getBOM()){
                    if(bom.getMATNR().equals(code)) {
                        BaseProductBomDet baseProductBomDet = new BaseProductBomDet();
                        baseProductBomDet.setProductBomId(bomData.getProductBomId());
                        baseProductBomDet.setMaterialId(getMaterialId(bom.getIDNRK()));
                        baseProductBomDet.setUsageQty(new BigDecimal(bom.getMENGE()));
                        baseProductBomDet.setBaseQty(new BigDecimal(bom.getBMENG()));
                        baseProductBomDet.setRemark(bom.getMAKTXZ());
                        baseFeignApi.addOrUpdate(baseProductBomDet);
                    }
                }
            }
            return 1;
        }else{
            throw new BizErrorException("接口请求失败");
        }
    }

    public Long getMaterialId(String materialCode){
        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialCode(materialCode);
        ResponseEntity<List<BaseMaterial>> parentMaterialList = baseFeignApi.findSmtMaterialList(searchBaseMaterial);
        if(StringUtils.isEmpty(parentMaterialList.getData()))
            throw new BizErrorException("未查询到对应的物料");
        return parentMaterialList.getData().get(0).getMaterialId();
    }

    //保存或更新bom表、如果原bom表已经有数据则删除det表
    public BaseProductBom saveBaseProductBom(String materialCode,String productBomVersion){
        SearchBaseProductBom searchBaseProductBom = new SearchBaseProductBom();
        searchBaseProductBom.setMaterialId(getMaterialId(materialCode));
        searchBaseProductBom.setProductBomVersion(productBomVersion);
        ResponseEntity<List<BaseProductBomDto>> productBomList = baseFeignApi.findProductBomList(searchBaseProductBom);
        BaseProductBom productBom = new BaseProductBom();
        ResponseEntity<BaseProductBom> productBoms = null;

        //判断不为空则删除det子集，保存或更新bom表
        if(StringUtils.isNotEmpty(productBomList.getData())){
            SearchBaseProductBomDet SearchBaseProductBomDet = new SearchBaseProductBomDet();
            SearchBaseProductBomDet.setProductBomId(productBomList.getData().get(0).getProductBomId());
            ResponseEntity<List<BaseProductBomDet>> list = baseFeignApi.findList(SearchBaseProductBomDet);
            if(StringUtils.isNotEmpty(list)) baseFeignApi.batchApiDelete(list.getData());
            productBom.setProductBomId(productBomList.getData().get(0).getProductBomId());
        }
        productBom.setProductBomVersion(productBomVersion);
        productBom.setMaterialId(getMaterialId(materialCode));
        productBoms = baseFeignApi.addOrUpdate(productBom);
        return productBoms.getData();
    }

}

