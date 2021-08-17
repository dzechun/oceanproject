package com.fantechs.provider.materialapi.imes.service.impl;


import com.fantechs.common.base.general.dto.restapi.RestapiChkSNRoutingApiDto;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.materialapi.imes.service.ChkSnRoutingService;
import com.fantechs.provider.materialapi.imes.utils.DeviceInterFaceUtils;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.text.ParseException;

/**
 * @author Huangshuijun
 * @create 2021/08/10
 */
@WebService(serviceName = "ChkSnRoutingService", // 与接口中指定的name一致
        targetNamespace = "http://ChkSnRouting.imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.materialapi.imes.service.ChkSnRoutingService"// 接口地址
)
public class ChkSnRoutingServiceImpl implements ChkSnRoutingService {

    @Resource
    private LogsUtils logsUtils;
    @Resource
    private DeviceInterFaceUtils deviceInterFaceUtils;
    @Resource
    private BarcodeUtils barcodeUtils;

    @Override
    public String ChkSnRouting(RestapiChkSNRoutingApiDto restapiChkSNRoutingApiDto) throws ParseException {
        /*
        * 1 验证传参基础信息是否正确
        * 2 检查成品SN、半成品SN状态、流程是否正确
        * 3 检查设备、治具状态及绑定关系是否正确
        * 4 检查设备、治具是否可以在该产品生产
        * 5 检查产前、关键事项是否完成
        */
        String pass="Pass";

        if(StringUtils.isEmpty(restapiChkSNRoutingApiDto)){
            return "Fail 条码流程检查信息为空";
        }

        String check = deviceInterFaceUtils.checkParameter(restapiChkSNRoutingApiDto.getProCode(),restapiChkSNRoutingApiDto.getProcessCode(),
                restapiChkSNRoutingApiDto.getBarcodeCode(),restapiChkSNRoutingApiDto.getPartBarcode(),
                restapiChkSNRoutingApiDto.getEamJigBarCode(),restapiChkSNRoutingApiDto.getEquipmentCode(),
                "","","");
        if (!check.equals("1")) {
            logsUtils.addlog((byte) 0, (byte) 2, (long) 1002, check, restapiChkSNRoutingApiDto.toString());
            return check;
        }
        logsUtils.addlog((byte)1,(byte)2,(long)1002,null,null);
        return pass+" 条码流程检查信息验证通过";
    }


}
