package com.fantechs.provider.materialapi.imes.service.impl;


import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.dto.eam.EamJigReMaterialDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.mes.sfc.UpdateProcessDto;
import com.fantechs.common.base.general.dto.restapi.RestapiChkLogUserInfoApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiSNDataTransferApiDto;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.BaseStation;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.materialapi.imes.service.Chk_LogUserInfoService;
import com.fantechs.provider.materialapi.imes.service.SN_Data_TransferService;
import com.fantechs.provider.materialapi.imes.utils.DeviceInterFaceUtils;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import com.fantechs.provider.mes.sfc.util.BarcodeUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.text.ParseException;
import java.util.List;

/**
 * @author Huangshuijun
 * @create 2021/08/11
 */
@WebService(serviceName = "SN_Data_TransferService", // 与接口中指定的name一致
        targetNamespace = "http://SN_Data_Transfer.imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.materialapi.imes.service.SN_Data_TransferService"// 接口地址
)
public class SN_Data_TransferServiceImpl implements SN_Data_TransferService {

    @Resource
    private LogsUtils logsUtils;
    @Resource
    private DeviceInterFaceUtils deviceInterFaceUtils;
    @Resource
    private BarcodeUtils barcodeUtils;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    @LcnTransaction
    public String SN_Data_Transfer(RestapiSNDataTransferApiDto restapiSNDataTransferApiDto) throws Exception {
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
        if (!check.equals("1")) {
            logsUtils.addlog((byte) 0, (byte) 2, (long) 1002, check, restapiSNDataTransferApiDto.toString());
            return check;
        }

        //返写治具编号使用次数 暂时默认治具使用次数为 1
        if(StringUtils.isNotEmpty(restapiSNDataTransferApiDto.getEamJigBarCode())){
            String[] jigBarCodeArr=restapiSNDataTransferApiDto.getEamJigBarCode().split(",");
            for (String item : jigBarCodeArr) {
                if(StringUtils.isNotEmpty(item)) {
                    ResponseEntity<List<EamJigBarcodeDto>> eamJigBarcodeDtoList = deviceInterFaceUtils.getJigBarCode(item);
                    EamJigBarcodeDto eamJigBarcodeDto=eamJigBarcodeDtoList.getData().get(0);
                    baseFeignApi.plusCurrentUsageTime(eamJigBarcodeDto.getJigBarcodeId(),1);
                }
            }
        }

        //过站
        BaseOrganizationDto baseOrganizationDto=deviceInterFaceUtils.getOrId().getData().get(0);
        Long orgId=baseOrganizationDto.getOrganizationId();
        SysUser user=deviceInterFaceUtils.getSysUser(restapiSNDataTransferApiDto.getUserCode(),orgId).getData().get(0);
        BaseProLine baseProLine=deviceInterFaceUtils.getProLine(restapiSNDataTransferApiDto.getProCode(),orgId).getData().get(0);
        BaseProcess baseProcess=deviceInterFaceUtils.getProcess(restapiSNDataTransferApiDto.getProcessCode(),orgId).getData().get(0);
        MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto=deviceInterFaceUtils.getWorkOrderBarcode(restapiSNDataTransferApiDto.getBarCode()).getData().get(0);
        Long workOrderId=mesSfcWorkOrderBarcodeDto.getWorkOrderId();
        MesPmWorkOrderDto mesPmWorkOrderDto=deviceInterFaceUtils.getWorkOrder(workOrderId).getData().get(0);
        BaseStation baseStation=deviceInterFaceUtils.getStation(restapiSNDataTransferApiDto.getStationCode(),orgId).getData().get(0);

        UpdateProcessDto updateProcessDto = UpdateProcessDto.builder()
                .badnessPhenotypeCode("N/A")
                .barCode(restapiSNDataTransferApiDto.getBarCode())
                .equipmentId("N/A")
                .operatorUserId(user.getUserId())
                .proLineId(baseProLine.getProLineId())
                .routeId(mesPmWorkOrderDto.getRouteId())
                .nowProcessId(baseProcess.getProcessId())
                .nowStationId(baseStation.getStationId())
                .workOrderId(mesPmWorkOrderDto.getWorkOrderId())
                .passCode("")
                .passCodeType((byte) 1)
                .build();

        barcodeUtils.updateProcess(updateProcessDto);

        //不良现象未处理

        logsUtils.addlog((byte)1,(byte)2,(long)1002,null,null);
        return pass+" 过站成功";
    }


}
