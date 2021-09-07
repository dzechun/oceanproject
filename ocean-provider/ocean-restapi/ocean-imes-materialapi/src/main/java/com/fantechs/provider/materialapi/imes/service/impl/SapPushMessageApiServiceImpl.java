package com.fantechs.provider.materialapi.imes.service.impl;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.*;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.materialapi.imes.service.SapPushMessageApiService;
import com.fantechs.provider.materialapi.imes.service.SapReportWorkApiService;
import com.fantechs.provider.materialapi.imes.utils.BaseUtils;
import com.fantechs.provider.materialapi.imes.utils.BasicAuthenticator;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.materialapi.imes.utils.pushMessage.SendMsg;
import com.fantechs.provider.materialapi.imes.utils.pushMessage.SendMsgService;
import com.fantechs.provider.materialapi.imes.utils.reportWorkApi.SIMESWORKORDERREPORTSAVEOut;
import com.fantechs.provider.materialapi.imes.utils.reportWorkApi.SIMESWORKORDERREPORTSAVEOutService;

import javax.annotation.Resource;
import java.net.Authenticator;
import java.text.ParseException;
import java.util.List;


@org.springframework.stereotype.Service
public class SapPushMessageApiServiceImpl implements SapPushMessageApiService {

    @Resource
    private LogsUtils logsUtils;
    @Resource
    private BaseUtils baseUtils;
    private String appid = "1000035";

    @Override
    @LcnTransaction
    public int  sendPushMessage(SapPushMessageApi sapPushMessageApi){
        check(sapPushMessageApi);
        List<BaseOrganizationDto> orgIdList = baseUtils.getOrId();
        if(StringUtils.isEmpty(orgIdList)) throw new BizErrorException("未查询到对应组织");
        Long orgId = orgIdList.get(0).getOrganizationId();

        SendMsgService service = new SendMsgService();
        SendMsg msg = service.getSendMsgPort();
        Textmsginput req = new Textmsginput();
        req.setAPPID(appid);
        req.setMSG(sapPushMessageApi.getMsg());
        req.setUSERNAME(sapPushMessageApi.getUserName());

        Resbase res = msg.sendMessage(req);
        if(StringUtils.isNotEmpty(res)){
            logsUtils.addlog((byte)1,(byte)1,orgId,res.toString(),req.toString());
            return 1;
        }else{
            logsUtils.addlog((byte)0,(byte)1,orgId,res.toString(),req.toString());
            throw new BizErrorException("接口请求失败,失败原因："+res.getRETMSG());
        }
    }


    public void check(SapPushMessageApi sapPushMessageApi){
        if(StringUtils.isEmpty(sapPushMessageApi.getMsg()))
            throw new BizErrorException("请求失败,消息参数不能为空");
        if(StringUtils.isEmpty(sapPushMessageApi.getUserName()))
            throw new BizErrorException("请求失败,发送人员不能为空");
    }

}
