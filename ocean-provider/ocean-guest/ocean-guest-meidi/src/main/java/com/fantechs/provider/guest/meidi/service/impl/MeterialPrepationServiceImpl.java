package com.fantechs.provider.guest.meidi.service.impl;

import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.*;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.guest.meidi.entity.MeterialPrepation;
import com.fantechs.provider.guest.meidi.entity.MeterialPrepationRequest;
import com.fantechs.provider.guest.meidi.service.MeterialPrepationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/26.
 */
@Service
public class MeterialPrepationServiceImpl implements MeterialPrepationService {
    protected static final Logger logger = LoggerFactory.getLogger(MeterialPrepationServiceImpl.class);
    @Resource
    private SecurityFeignApi securityFeignApi;

    private String completedUrl= "http://127.0.0.1:9600/FCS/PDA/MeterialPrepationCompleted";
    private String canceUrl= "http://127.0.0.1:9600/FCS/PDA/MeterialPrepationCance";
 //   private String completedUrl1= "http://127.0.0.1:9023/meidiApi/get";
    @Override
    public int send(MeterialPrepation meterialPrepation) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        logger.info("---------调用接口-------------");
        if(StringUtils.isEmpty(meterialPrepation.getStorageCode()) || StringUtils.isEmpty())
            throw  new BizErrorException("库位编码或接口请求类型不能为空");
        MeterialPrepationRequest req = new MeterialPrepationRequest();
        Map<String,Object> map = new HashMap<>();
        String result = null;
    //    req.setRequestCode();
        String data = "{\"locationCode\":\""+meterialPrepation.getStorageCode()+"\",\""+user.getNickName()+"\":\""+user.getNickName()+"\"}";
        req.setRequestData(data);
        //请求头
        Map<String,String> header = new HashMap<String, String>();
        //username:password--->访问的用户名，密码,并使用base64进行加密，将加密的字节信息转化为string类型，encoding--->token
        String encoding = null;
        try {
            encoding = DatatypeConverter.printBase64Binary("kermit:kermit".getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        header.put("Authorization", "Basic " + encoding);

        if("1".equals(meterialPrepation.getType())){
            req.setRequsetName("MeterialPreparationCompleted");
            req.setRequestCode(UUIDUtils.getUUID());
            logger.info("---------请求参数-------------"+req);
            result = HTTPUtils.sendHttp(completedUrl,ControllerUtil.dynamicConditionByEntity(req), header);
            if(StringUtils.isNotEmpty(result)){
                map =  JsonUtils.jsonToMap(result);
                logger.info("---------请求结果map1-------------"+map);
                if("success".equals(map.get("responseMessage"))){
                    return 0;
                }else{
                    throw new BizErrorException("接口请求错误，错误信息为："+map.get("responseData")
                    +"错误编码为："+map.get("requestCode"));
                }
            }
        }else if("2".equals(meterialPrepation.getType())){
            req.setRequsetName("MeterialPreparationCancel");
            req.setRequestCode(UUIDUtils.getUUID());
            logger.info("---------请求参数-------------"+req);
            result = HTTPUtils.sendHttp(canceUrl,ControllerUtil.dynamicConditionByEntity(req), header);
            if(StringUtils.isNotEmpty(result)){
                map = JsonUtils.jsonToMap(result);
                logger.info("---------请求结果map2------------"+map);
                if("success".equals(map.get("responseMessage"))){
                    return 0;
                }else{
                    throw new BizErrorException("接口请求错误，错误信息为："+map.get("responseData")
                            +"错误编码为："+map.get("requestCode"));
                }
            }
        }else{
            throw  new BizErrorException("请求接口类型错误");
        }

        //保存日志
        if("success".equals(map.get("responseMessag"))){
            addlog((byte)1,user.getOrganizationId(),ControllerUtil.dynamicConditionByEntity(req).toString(),result);
        }else{
            addlog((byte)2,user.getOrganizationId(),ControllerUtil.dynamicConditionByEntity(req).toString(),result);
        }
        return 1;
    }


    /**
     * 日志
     * @param result
     * @param orgId
     * @param responseData
     * @param requestParameter
     */
    public void  addlog(Byte result,Long orgId,String requestParameter,String responseData){
        SysApiLog sysApiLog = new SysApiLog();
        sysApiLog.setThirdpartySysName("美的");
        sysApiLog.setCallResult(result);
        sysApiLog.setCallType((byte)1);
        sysApiLog.setApiModule("ocean-guest-meidi");
        sysApiLog.setOrgId(orgId);
        sysApiLog.setRequestTime(new Date());
        sysApiLog.setResponseTime(new Date());
        sysApiLog.setResponseData(responseData);
        sysApiLog.setRequestParameter(requestParameter);
        securityFeignApi.add(sysApiLog);
    }
}
