package com.fantechs.provider.materialapi.imes.service.impl;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.restapi.*;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.materialapi.imes.service.SapSupplierApiService;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;
import com.fantechs.provider.materialapi.imes.utils.supplierApi.SIMESSUPPLIERQUERYOut;
import com.fantechs.provider.materialapi.imes.utils.supplierApi.SIMESSUPPLIERQUERYOutService;

import javax.annotation.Resource;
import java.net.Authenticator;


@org.springframework.stereotype.Service
public class SapSupplierApiServiceImpl implements SapSupplierApiService {

    @Resource
    private BaseFeignApi baseFeignApi;

    private String userName = "MESPIALEUSER"; //雷赛wsdl用户名
    private String password = "1234qwer"; //雷赛wsdl密码

    @Override
    public int getSupplier(SearchSapSupplierApi searchSapSupplierApi) {
        Authenticator.setDefault(new BasicAuthenticator(userName, password));
        SIMESSUPPLIERQUERYOutService service = new SIMESSUPPLIERQUERYOutService();
        SIMESSUPPLIERQUERYOut out = service.getHTTPPort();
        DTMESSUPPLIERQUERYREQ req = new DTMESSUPPLIERQUERYREQ();
        if(StringUtils.isEmpty(searchSapSupplierApi.getWerks()))
            throw new BizErrorException("工厂号不能为空");

        req.setWERKS(searchSapSupplierApi.getWerks());
        DTMESSUPPLIERQUERYRES res = out.siMESSUPPLIERQUERYOut(req);
        if(StringUtils.isNotEmpty(res) && "S".equals(res.getTYPE())){
            if(StringUtils.isEmpty(res.getSUPPLIER())) throw new BizErrorException("请求结果为空");
            for(DTMESSUPPLIER supplier: res.getSUPPLIER()){
                if(StringUtils.isEmpty(supplier.getLIFNR())) throw new BizErrorException("新增或更新失败，物料编码为空");
                BaseSupplier baseSupplier = new BaseSupplier();
                baseSupplier.setSupplierName(supplier.getNAME1());
                baseSupplier.setSupplierCode(supplier.getLIFNR());
                baseSupplier.setSupplierDesc(supplier.getNAME1());
                baseSupplier.setStatus((byte)1);
                baseSupplier.setSupplierType((byte)1);
                baseFeignApi.addOrUpdate(baseSupplier);
            }
            return 1;
        }else{
            throw new BizErrorException("接口请求失败");
        }
    }
}

