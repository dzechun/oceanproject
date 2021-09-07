package com.fantechs.provider.materialapi.imes.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseFactoryDto;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.*;
import com.fantechs.common.base.general.entity.basic.BaseFactory;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseFactory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWorkShop;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.materialapi.imes.utils.BaseUtils;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.materialapi.imes.utils.customerApi.SIMESCUSTOMERQUERYOut;
import com.fantechs.provider.materialapi.imes.service.SapCustomerApiService;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;
import com.fantechs.provider.materialapi.imes.utils.customerApi.SIMESCUSTOMERQUERYOutService;


import javax.annotation.Resource;
import java.net.Authenticator;
import java.text.ParseException;
import java.util.List;


@org.springframework.stereotype.Service
public class SapCustomerApiServiceImpl implements SapCustomerApiService {

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
    public int getCustomer(SearchSapSupplierApi searchSapSupplierApi) throws ParseException {
        Authenticator.setDefault(new BasicAuthenticator(userName, password));
        SIMESCUSTOMERQUERYOutService service = new SIMESCUSTOMERQUERYOutService();
        SIMESCUSTOMERQUERYOut out = service.getHTTPPort();
        DTMESCUSTOMERQUERYREQ req = new DTMESCUSTOMERQUERYREQ();
        if(StringUtils.isEmpty(searchSapSupplierApi.getWerks()))
            throw new BizErrorException("工厂号不能为空");
        req.setWERKS(searchSapSupplierApi.getWerks());
        DTMESCUSTOMERQUERYRES res = out.siMESCUSTOMERQUERYOut(req);

        List<BaseOrganizationDto> orgIdList = baseUtils.getOrId();
        if(StringUtils.isEmpty(orgIdList)) throw new BizErrorException("未查询到对应组织");
        Long orgId = orgIdList.get(0).getOrganizationId();

        if(StringUtils.isNotEmpty(res) && "S".equals(res.getTYPE())){
            if(StringUtils.isEmpty(res.getCUSTOMER())) throw new BizErrorException("请求结果为空");

            for(DTMESCUSTOMER customer: res.getCUSTOMER()){
                if(StringUtils.isEmpty(customer.getKUNNR())) throw new BizErrorException("新增或更新失败，物料编码为空");
                BaseSupplier baseSupplier = new BaseSupplier();
                baseSupplier.setSupplierName(customer.getNAME1());
                baseSupplier.setSupplierCode(baseUtils.removeZero(customer.getKUNNR()));
                baseSupplier.setSupplierDesc(customer.getNAME1());
                baseSupplier.setStatus((byte)1);
                baseSupplier.setSupplierType((byte)2);
                baseSupplier.setOrganizationId(orgId);
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
    public int getCustomers(){
        SearchBaseFactory searchBaseFactory = new SearchBaseFactory();
        List<BaseOrganizationDto> orgIdList = baseUtils.getOrId();
        if(StringUtils.isEmpty(orgIdList)) throw new BizErrorException("未查询到对应组织");
        Long orgId = orgIdList.get(0).getOrganizationId();
        searchBaseFactory.setOrgId(orgId);
        ResponseEntity<List<BaseFactoryDto>> factoryList = baseFeignApi.findFactoryList(searchBaseFactory);
        int i = 0;
        if(StringUtils.isNotEmpty(factoryList.getData())){
            for(BaseFactory factory : factoryList.getData()){
                SearchSapSupplierApi searchSapSupplierApi = new SearchSapSupplierApi();
                searchSapSupplierApi.setWerks(factory.getFactoryCode());
                try {
                    i = this.getCustomer(searchSapSupplierApi);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        return i;
    }
}

