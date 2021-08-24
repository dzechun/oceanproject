package com.fantechs.provider.materialapi.imes.service.impl;


import com.fantechs.common.base.general.dto.basic.BaseExecuteResultDto;
import com.fantechs.common.base.general.dto.restapi.RestapiChkLogUserInfoApiDto;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.materialapi.imes.service.ChkLogUserInfoService;
import com.fantechs.provider.materialapi.imes.utils.DeviceInterFaceUtils;

import javax.annotation.Resource;
import javax.jws.WebService;

@WebService(serviceName = "ChkLogUserInfoService", // 与接口中指定的name一致
        targetNamespace = "http://ChkLogUserInfo.imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.materialapi.imes.service.ChkLogUserInfoService"// 接口地址
)
public class ChkLogUserInfoServiceImpl implements ChkLogUserInfoService {

    @Resource
    private DeviceInterFaceUtils deviceInterFaceUtils;
    @Resource
    private SFCFeignApi sfcFeignApi;


    @Override
    public String ChkLogUserInfo(RestapiChkLogUserInfoApiDto restapiChkLogUserInfoApiDto) throws Exception {
        String executeResult="";
        BaseExecuteResultDto baseExecuteResultDto= sfcFeignApi.chkLogUserInfo(restapiChkLogUserInfoApiDto).getData();
        executeResult= JsonUtils.objectToJson(baseExecuteResultDto);
        return  executeResult;
    }

}
