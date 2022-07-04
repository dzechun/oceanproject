package com.fantechs.provider.materialapi.imes.service.impl;


import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.*;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.materialapi.imes.utils.BaseUtils;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.materialapi.imes.utils.reportWorkApi.SIMESWORKORDERREPORTSAVEOut;
import com.fantechs.provider.materialapi.imes.utils.reportWorkApi.SIMESWORKORDERREPORTSAVEOutService;
import com.fantechs.provider.materialapi.imes.service.SapReportWorkApiService;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;
import io.seata.spring.annotation.GlobalTransactional;

import javax.annotation.Resource;
import java.net.Authenticator;
import java.text.ParseException;
import java.util.List;


@org.springframework.stereotype.Service
public class SapReportWorkApiServiceImpl implements SapReportWorkApiService {

    @Resource
    private LogsUtils logsUtils;
    @Resource
    private BaseUtils baseUtils;

    private String userName = "MESPIALEUSER"; //雷赛wsdl用户名
    private String password = "1234qwer"; //雷赛wsdl密码

    @Override
    @GlobalTransactional
    public int sendReportWork(SearchSapReportWorkApi searchSapReportWorkApi) throws ParseException {
        check(searchSapReportWorkApi);
        Authenticator.setDefault(new BasicAuthenticator(userName, password));
        SIMESWORKORDERREPORTSAVEOutService service = new SIMESWORKORDERREPORTSAVEOutService();
        SIMESWORKORDERREPORTSAVEOut out = service.getHTTPPort();
        DTMESWORKORDERREPORTSAVEREQ req = new DTMESWORKORDERREPORTSAVEREQ();
        req.setAUFNR(searchSapReportWorkApi.getWorkOrderCode());
        req.setVORNR(searchSapReportWorkApi.getProcessCode());
        req.setLMNGA(searchSapReportWorkApi.getProductionQty());
        req.setRMNGA(searchSapReportWorkApi.getReWorkQty());
        req.setXMNGA(searchSapReportWorkApi.getScrapQty());
        DTMESWORKORDERREPORTSAVERES res = out.siMESWORKORDERREPORTSAVEOut(req);
        List<BaseOrganizationDto> orgIdList = baseUtils.getOrId();
        if(StringUtils.isEmpty(orgIdList)) throw new BizErrorException("未查询到对应组织");
        Long orgId = orgIdList.get(0).getOrganizationId();

        if(StringUtils.isNotEmpty(res) && "S".equals(res.getTYPE())){
            logsUtils.addlog((byte)1,(byte)1,orgId,null,req.toString());
            return 1;
        }else{
            logsUtils.addlog((byte)0,(byte)1,orgId,res.toString(),req.toString());
            throw new BizErrorException("接口请求失败,失败原因："+res.getMESSAGE());
        }
    }


    public void check(SearchSapReportWorkApi searchSapReportWorkApi){
        if(StringUtils.isEmpty(searchSapReportWorkApi))
            throw new BizErrorException("请求失败,参数不能为空");
        if(StringUtils.isEmpty(searchSapReportWorkApi.getWorkOrderCode()))
            throw new BizErrorException("工单号不能为空");
        if(StringUtils.isEmpty(searchSapReportWorkApi.getProcessCode()))
            throw new BizErrorException("工序号不能为空");
    }

}
