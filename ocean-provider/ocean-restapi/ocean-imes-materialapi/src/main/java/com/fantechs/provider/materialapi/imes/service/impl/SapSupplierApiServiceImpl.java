package com.fantechs.provider.materialapi.imes.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseFactoryDto;
import com.fantechs.common.base.general.dto.restapi.*;
import com.fantechs.common.base.general.entity.basic.BaseFactory;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseFactory;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.materialapi.imes.service.SapSupplierApiService;
import com.fantechs.provider.materialapi.imes.utils.BaseUtils;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.materialapi.imes.utils.supplierApi.SIMESSUPPLIERQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.supplierApi.SIMESSUPPLIERQUERYOutService;

import javax.annotation.Resource;
import java.net.Authenticator;
import java.text.ParseException;
import java.util.List;


@org.springframework.stereotype.Service
public class SapSupplierApiServiceImpl implements SapSupplierApiService {

    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private LogsUtils logsUtils;
    @Resource
    private BaseUtils baseUtils;

    private String userName = "MESPIALEUSER"; //雷赛wsdl用户名
    private String password = "1234qwer"; //雷赛wsdl密码

    @Override
    @LcnTransaction
    public int getSupplier(SearchSapSupplierApi searchSapSupplierApi) throws ParseException {
        Authenticator.setDefault(new BasicAuthenticator(userName, password));
        SIMESSUPPLIERQUERYOutService service = new SIMESSUPPLIERQUERYOutService();
        SIMESSUPPLIERQUERYOut out = service.getHTTPPort();
        DTMESSUPPLIERQUERYREQ req = new DTMESSUPPLIERQUERYREQ();
        if(StringUtils.isEmpty(searchSapSupplierApi.getWerks()))
            throw new BizErrorException("工厂号不能为空");
        Long orgId = baseUtils.getOrId();
        req.setWERKS(searchSapSupplierApi.getWerks());
        DTMESSUPPLIERQUERYRES res = out.siMESSUPPLIERQUERYOut(req);
        if(StringUtils.isNotEmpty(res) && "S".equals(res.getTYPE())){
            if(StringUtils.isEmpty(res.getSUPPLIER())) throw new BizErrorException("请求结果为空");
            for(DTMESSUPPLIER supplier: res.getSUPPLIER()){
                if(StringUtils.isEmpty(supplier.getLIFNR())) throw new BizErrorException("新增或更新失败，物料编码为空");
                BaseSupplier baseSupplier = new BaseSupplier();
                baseSupplier.setSupplierName(supplier.getNAME1());
                baseSupplier.setSupplierCode(baseUtils.removeZero(supplier.getLIFNR()));
                baseSupplier.setSupplierDesc(supplier.getNAME1());
                baseSupplier.setStatus((byte)1);
                baseSupplier.setSupplierType((byte)1);
                baseSupplier.setIsDelete((byte)1);
                baseFeignApi.saveByApi(baseSupplier);
            }
            logsUtils.addlog((byte)1,(byte)1,orgId,null,req.toString());
            return 1;
        }else{
            logsUtils.addlog((byte)0,(byte)1,orgId,res.toString(),req.toString());
            throw new BizErrorException("接口请求失败");
        }
    }

    @Override
    public int getSuppliers(){
        SearchBaseFactory searchBaseFactory = new SearchBaseFactory();
        searchBaseFactory.setOrgId(baseUtils.getOrId());
        ResponseEntity<List<BaseFactoryDto>> factoryList = baseFeignApi.findFactoryList(searchBaseFactory);
        int i = 0;
        if(StringUtils.isNotEmpty(factoryList.getData())){
            for(BaseFactory factory : factoryList.getData()){
                SearchSapSupplierApi searchSapSupplierApi = new SearchSapSupplierApi();
                searchSapSupplierApi.setWerks(factory.getFactoryCode());
                try {
                    i = this.getSupplier(searchSapSupplierApi);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return i;
    }
}

