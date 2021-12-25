package com.fantechs.provider.materialapi.imes.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.basic.BaseProductBomDto;
import com.fantechs.common.base.general.dto.restapi.DTMESBOM;
import com.fantechs.common.base.general.dto.restapi.DTMESBOMQUERYREQ;
import com.fantechs.common.base.general.dto.restapi.DTMESBOMQUERYRES;
import com.fantechs.common.base.general.dto.restapi.SearchSapProductBomApi;
import com.fantechs.common.base.general.entity.basic.BaseProductBom;
import com.fantechs.common.base.general.entity.basic.BaseProductBomDet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductBom;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.materialapi.imes.service.SapProductBomApiService;
import com.fantechs.provider.materialapi.imes.utils.BaseUtils;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.materialapi.imes.utils.productBomApi.SIMESBOMQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.productBomApi.SIMESBOMQUERYOutService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.net.Authenticator;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;


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
    ReentrantLock lock = new ReentrantLock(true);

    @Override
    @LcnTransaction
    public int getProductBom(SearchSapProductBomApi searchSapProductBomApi) throws ParseException {
        if(lock.tryLock()) {
            try {
                Authenticator.setDefault(new BasicAuthenticator(userName, password));
                SIMESBOMQUERYOutService service = new SIMESBOMQUERYOutService();
                SIMESBOMQUERYOut out = service.getHTTPPort();
                DTMESBOMQUERYREQ req = new DTMESBOMQUERYREQ();
                if (StringUtils.isEmpty(searchSapProductBomApi.getMaterialCode()))
                    throw new BizErrorException("物料号不能为空");
                req.setMATNR(searchSapProductBomApi.getMaterialCode());
                req.setOITXT(searchSapProductBomApi.getOitxt());
                DTMESBOMQUERYRES res = out.siMESBOMQUERYOut(req);
                List<BaseOrganizationDto> orgIdList = baseUtils.getOrId();
                if (StringUtils.isEmpty(orgIdList)) throw new BizErrorException("未查询到对应组织");
                Long orgId = orgIdList.get(0).getOrganizationId();
                if (StringUtils.isNotEmpty(res) && "S".equals(res.getTYPE())) {
                    if (StringUtils.isEmpty(res.getBOM())) throw new BizErrorException("请求结果为空");
                    //区分表头和det的物料编码
                    List<DTMESBOM> parentList = new ArrayList<DTMESBOM>();
                    HashSet<String> materialCodes = new HashSet<String>();
                    HashSet<String> allMaterialCode = new HashSet<String>();
                    for (DTMESBOM bom : res.getBOM()) {
                        String materialCode = baseUtils.removeZero(bom.getMATNR());
                        materialCodes.add(materialCode);
                        allMaterialCode.add(materialCode);
                        if(StringUtils.isNotEmpty(bom.getIDNRK()))
                            allMaterialCode.add(baseUtils.removeZero(bom.getIDNRK()));
                        if (materialCode.equals(searchSapProductBomApi.getMaterialCode())) {
                            if (StringUtils.isNotEmpty(bom.getAENNR()))
                                parentList.add(bom);
                        }
                    }
                    List<String> str =new ArrayList(allMaterialCode);
                    Map<String, Long> data = baseFeignApi.findIdByCode(str).getData();
                    Map<String,Long> productBomIds = new HashMap<>();
                    //保存第一层bom表以及det表
                    if (StringUtils.isEmpty(data.get(searchSapProductBomApi.getMaterialCode()))) {
                         throw new BizErrorException("未查询到对应物料编码");
                    }
                    if(StringUtils.isNotEmpty(parentList)) {
                        BaseProductBom parentBomData = saveBaseProductBom(data.get(searchSapProductBomApi.getMaterialCode()), baseUtils.removeZero(parentList.get(0).getAENNR()), orgId);
                        BaseProductBom bomData = null;

                        List<BaseProductBomDet> detList = new ArrayList<>();
                        for (String code : materialCodes) {
                            if (searchSapProductBomApi.getMaterialCode().equals(code)) {
                                bomData = parentBomData;
                            } else {
                                Long materialId = data.get(code);
                                if(StringUtils.isEmpty(materialId)) throw new BizErrorException("未查询到物料信息，物料编码为："+code);
                                bomData = saveBaseProductBom(materialId, "0", orgId);

                                BaseProductBomDet subBomDet = new BaseProductBomDet();
                                if (StringUtils.isNotEmpty(parentBomData))
                                    subBomDet.setProductBomId(parentBomData.getProductBomId());
                                subBomDet.setMaterialId(materialId);
                                subBomDet.setIfHaveLowerLevel((byte) 1);
                                subBomDet.setIsDelete((byte) 1);
                                subBomDet.setStatus((byte) 1);
                                detList.add(subBomDet);
                            }

                            if (StringUtils.isEmpty(bomData)) throw new BizErrorException("保存失败");

                            productBomIds.put(code, bomData.getProductBomId());
                            for (DTMESBOM bom : res.getBOM()) {
                                if (baseUtils.removeZero(bom.getMATNR()).equals(code) && StringUtils.isNotEmpty(bom.getIDNRK())) {
                                    String detCode = baseUtils.removeZero(bom.getIDNRK());
                                    Long materialId = data.get(detCode);
                                    if(StringUtils.isEmpty(materialId)) throw new BizErrorException("未查询到物料信息，物料编码为："+bom.getIDNRK());
                                    BaseProductBomDet baseProductBomDet = new BaseProductBomDet();
                                    baseProductBomDet.setProductBomId(bomData.getProductBomId());
                                    baseProductBomDet.setPosition(bom.getZWH());
                                    baseProductBomDet.setMaterialId(materialId);
                                    baseProductBomDet.setUsageQty(new BigDecimal(bom.getMENGE().trim()));
                                    baseProductBomDet.setBaseQty(new BigDecimal(bom.getBMENG().trim()));
                                    baseProductBomDet.setRemark(bom.getMAKTXZ());
                                    baseProductBomDet.setIfHaveLowerLevel((byte) 0);
                                    baseProductBomDet.setIsDelete((byte) 1);
                                    baseProductBomDet.setStatus((byte) 1);
                                    baseProductBomDet.setOrgId(orgId);
                                    baseProductBomDet.setCreateTime(new Date());
                                    baseProductBomDet.setCreateUserId((long)99);
                                    detList.add(baseProductBomDet);
                                }
                            }
                        }
                        baseFeignApi.addOrUpdate(detList);
                    }
                    logsUtils.addlog((byte) 1, (byte) 1, orgId, null, req.getMATNR());
                    return 1;
                } else {
                    logsUtils.addlog((byte) 0, (byte) 1, orgId, res.getTYPE() + "--" + res.getMESSAGE(), req.getMATNR());
                    throw new BizErrorException("接口请求失败,错误信息为：" + res.getMESSAGE());
                }
            }finally {
                lock.unlock();
            }
        }
        throw new BizErrorException("正在同步产品BOM，请勿重新操作");
    }


    //保存或更新bom表、如果原bom表已经有数据则删除det表
    public BaseProductBom saveBaseProductBom( Long id,String productBomVersion,Long orgId){

        SearchBaseProductBom searchBaseProductBom = new SearchBaseProductBom();
        searchBaseProductBom.setMaterialId(id);
        searchBaseProductBom.setProductBomVersion(productBomVersion);
        searchBaseProductBom.setOrgId(orgId);
        ResponseEntity<List<BaseProductBomDto>> productBomList = baseFeignApi.findProductBomList(searchBaseProductBom);
        BaseProductBom productBom = new BaseProductBom();
        ResponseEntity<BaseProductBom> pproductBom = null;

        //判断不为空则删除det子集，保存或更新bom表
        if(StringUtils.isNotEmpty(productBomList.getData())){
            baseFeignApi.batchApiDelete(productBomList.getData().get(0).getProductBomId());
        }
        //productBom.setProductBomId(productBomList.getData().get(0).getProductBomId());
        productBom.setProductBomVersion(productBomVersion);
        productBom.setMaterialId(id);
        productBom.setOrgId(orgId);
        productBom.setCreateTime(new Date());
        productBom.setCreateUserId((long)99);
        productBom.setIsDelete((byte) 1);
        productBom.setStatus((byte) 1);
        pproductBom = baseFeignApi.addOrUpdate(productBom);
        return pproductBom.getData();
    }

}

