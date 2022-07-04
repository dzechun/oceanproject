package com.fantechs.provider.materialapi.imes.service.impl;


import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.Resbase;
import com.fantechs.common.base.general.dto.restapi.SapPushMessageApi;
import com.fantechs.common.base.general.dto.restapi.Textmsginput;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.materialapi.imes.service.SapPushMessageApiService;
import com.fantechs.provider.materialapi.imes.utils.BaseUtils;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.materialapi.imes.utils.pushMessage.SendMsg;
import com.fantechs.provider.materialapi.imes.utils.pushMessage.SendMsgService;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
public class SapPushMessageApiServiceImpl implements SapPushMessageApiService {
    protected static final Logger logger = LoggerFactory.getLogger(SapPushMessageApiServiceImpl.class);
    @Resource
    private LogsUtils logsUtils;
    @Resource
    private BaseUtils baseUtils;
    private String appid = "1000035";

    @Override
    @GlobalTransactional
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
        req.setUSERID("");
        Resbase res = msg.sendMessage(req);
        if(StringUtils.isNotEmpty(res)&&"0".equals(res.getRETCODE())){
            logsUtils.addlog((byte)1,(byte)1,orgId,req.getMSG(),res.getRETMSG());
            return 1;
        }else{
            logsUtils.addlog((byte)0,(byte)1,orgId,req.getMSG(),res.getRETMSG());
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
