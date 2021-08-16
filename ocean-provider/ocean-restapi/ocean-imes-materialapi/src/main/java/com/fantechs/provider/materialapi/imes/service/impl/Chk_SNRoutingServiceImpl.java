package com.fantechs.provider.materialapi.imes.service.impl;


import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.dto.eam.EamJigReMaterialDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.restapi.RestapiChkSNRoutingApiDto;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.general.entity.mes.pm.MesPmProductionKeyIssuesOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.materialapi.imes.service.Chk_SNRoutingService;
import com.fantechs.provider.materialapi.imes.utils.DeviceInterFaceUtils;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

/**
 * @author Huangshuijun
 * @create 2021/08/10
 */
@WebService(serviceName = "Chk_SNRoutingService", // 与接口中指定的name一致
        targetNamespace = "http://Chk_SNRouting.imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.materialapi.imes.service.Chk_SNRoutingService"// 接口地址
)
public class Chk_SNRoutingServiceImpl implements Chk_SNRoutingService {

    @Resource
    private LogsUtils logsUtils;
    @Resource
    private DeviceInterFaceUtils deviceInterFaceUtils;
    @Resource
    private BarcodeUtils barcodeUtils;

    @Override
    public String Chk_SNRouting(RestapiChkSNRoutingApiDto restapiChkSNRoutingApiDto) throws ParseException {
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
