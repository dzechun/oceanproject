package com.fantechs.provider.materialapi.imes.service.impl;


import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.basic.BaseExecuteResultDto;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.UpdateProcessDto;
import com.fantechs.common.base.general.dto.restapi.RestapiSNDataTransferApiDto;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseStation;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.eam.EamFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.materialapi.imes.service.SnDataTransferService;
import com.fantechs.provider.materialapi.imes.utils.DeviceInterFaceUtils;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.util.List;

/**
 * @author Huangshuijun
 * @create 2021/08/11
 */
@WebService(serviceName = "SnDataTransferService", // 与接口中指定的name一致
        targetNamespace = "http://SnDataTransfer.imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.materialapi.imes.service.SnDataTransferService"// 接口地址
)
public class SnDataTransferServiceImpl implements SnDataTransferService {

    @Resource
    private DeviceInterFaceUtils deviceInterFaceUtils;
    @Resource
    private EamFeignApi eamFeignApi;
    @Resource
    private SFCFeignApi sfcFeignApi;

    @Override
    @LcnTransaction
    public String SnDataTransfer(RestapiSNDataTransferApiDto restapiSNDataTransferApiDto) throws Exception {
        /*
         * 1 验证传参基础信息是否正确
         * 2 检查成品SN、半成品SN状态、流程是否正确
         * 3 检查设备、治具状态及绑定关系是否正确
         * 4 检查设备、治具是否可以在该产品生产
         * 5 检查产前、关键事项是否完成
         * 前5项判断在checkParameter方法中完成
         * 6 返写治具编号使用次数
         * 7 记录条码过站时间、结果
         */
        String pass="Pass";
        if(StringUtils.isEmpty(restapiSNDataTransferApiDto)){
            return "Fail 过站信息为空";
        }

        String check = deviceInterFaceUtils.checkParameter(restapiSNDataTransferApiDto.getProCode(),restapiSNDataTransferApiDto.getProcessCode(),
                restapiSNDataTransferApiDto.getBarCode(),restapiSNDataTransferApiDto.getPartBarcode(),
                restapiSNDataTransferApiDto.getEamJigBarCode(),restapiSNDataTransferApiDto.getEquipmentCode(),
                restapiSNDataTransferApiDto.getSectionCode(),restapiSNDataTransferApiDto.getUserCode(),
                restapiSNDataTransferApiDto.getBadnessPhenotypeCode());

        String executeResult="";
        BaseExecuteResultDto baseExecuteResultDto= sfcFeignApi.snDataTransfer(restapiSNDataTransferApiDto).getData();
        executeResult= JsonUtils.objectToJson(baseExecuteResultDto);
        return  executeResult;
    }


}
