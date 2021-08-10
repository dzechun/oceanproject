package com.fantechs.provider.materialapi.imes.service.impl;


import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.dto.eam.EamJigBarcodeDto;
import com.fantechs.common.base.general.dto.eam.EamJigReMaterialDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcWorkOrderBarcodeDto;
import com.fantechs.common.base.general.dto.restapi.RestapiChkSNRoutingApiDto;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
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
/**
 * @author Huangshuijun
 * @create 2021/08/10
 */
@WebService(serviceName = "Chk_SNRoutingService", // 与接口中指定的name一致
        targetNamespace = "http://workOrder.imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
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
        String check = check(restapiChkSNRoutingApiDto);
        if (!check.equals("1")) {
            logsUtils.addlog((byte) 0, (byte) 2, (long) 1002, check, restapiChkSNRoutingApiDto.toString());
            return check;
        }
        logsUtils.addlog((byte)1,(byte)2,(long)1002,null,null);
        return pass+" 登录信息验证通过";
    }


    public String check(RestapiChkSNRoutingApiDto restapiChkSNRoutingApiDto) {
        String check = "1";
        String fail="Fail";
        Long orgId=null;
        Long MaterialId=null;
        ResponseEntity<List<BaseOrganizationDto>> baseOrganizationDtoList=deviceInterFaceUtils.getOrId();
        if(StringUtils.isEmpty(baseOrganizationDtoList.getData())){
            check = fail+" 请求失败,未查询到对应组织";
        }
        //获取组织ID
        orgId=baseOrganizationDtoList.getData().get(0).getOrganizationId();

        if(StringUtils.isEmpty(restapiChkSNRoutingApiDto))
            check = fail+" 请求失败,参数为空";
        if(StringUtils.isEmpty(restapiChkSNRoutingApiDto.getProCode()))
            check = fail+" 请求失败,产线编码不能为空";
        else{
            ResponseEntity<List<BaseProLine>> baseProLinelist=deviceInterFaceUtils.getProLine(restapiChkSNRoutingApiDto.getProCode(),orgId);
            if(StringUtils.isEmpty(baseProLinelist.getData())){
                check = fail+" 请求失败,产线编码不存在";
            }
        }
        if(StringUtils.isEmpty(restapiChkSNRoutingApiDto.getProcessCode()))
            check = fail+" 请求失败,工序编码不能为空";
        else {
            ResponseEntity<List<BaseProcess>> baseProcesslist=deviceInterFaceUtils.getProcess(restapiChkSNRoutingApiDto.getProcessCode(),orgId);
            if(StringUtils.isEmpty(baseProcesslist.getData())){
                check = fail+" 请求失败,工序编码不存在";
            }
        }
        if(StringUtils.isEmpty(restapiChkSNRoutingApiDto.getBarcodeCode()))
            check = fail+" 请求失败,成品SN不能为空";
        else{
            ResponseEntity<List<MesSfcWorkOrderBarcodeDto>> mesSfcWorkOrderBarcodeDtoList= deviceInterFaceUtils.getWorkOrderBarcode(restapiChkSNRoutingApiDto.getBarcodeCode());
            if(StringUtils.isEmpty(mesSfcWorkOrderBarcodeDtoList.getData())){
                check = fail+" 请求失败,成品SN不存在";
            }

            //检查条码状态
            if (mesSfcWorkOrderBarcodeDtoList.getData().size() > 1) {
                check = fail+" 请求失败,成品SN重复";
            }
            MesSfcWorkOrderBarcodeDto mesSfcWorkOrderBarcodeDto = mesSfcWorkOrderBarcodeDtoList.getData().get(0);
            if (mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 2 || mesSfcWorkOrderBarcodeDto.getBarcodeStatus() == 3) {
                check = fail+" 请求失败,成品SN条码状态不正确";
            }

            //检查工单
            Long workOrderId=mesSfcWorkOrderBarcodeDto.getWorkOrderId();
            ResponseEntity<List<MesPmWorkOrderDto>> mesPmWorkOrderDtoList= deviceInterFaceUtils.getWorkOrder(workOrderId);
            if(StringUtils.isEmpty(mesPmWorkOrderDtoList.getData())){
                check = fail+" 请求失败,生产工单不存在";
            }
            MesPmWorkOrderDto mesPmWorkOrderDto=mesPmWorkOrderDtoList.getData().get(0);

            if(StringUtils.isEmpty(mesPmWorkOrderDto.getRouteId())){
                check = fail+" 请求失败,工单未设置工艺流程";
            }
            if (4 == mesPmWorkOrderDto.getWorkOrderStatus() || 5 == mesPmWorkOrderDto.getWorkOrderStatus()) {
                check = fail+" 请求失败,工单状态已完成或已挂起";
            }
            if (mesPmWorkOrderDto.getProductionQty().compareTo(mesPmWorkOrderDto.getWorkOrderQty()) > -1) {
                check = fail+" 请求失败,工单投产数量大于等于工单数";
            }

            MaterialId=mesPmWorkOrderDto.getMaterialId();

        }
        //设备编码判断
        if(StringUtils.isNotEmpty(restapiChkSNRoutingApiDto.getEquipmentCode())){
            ResponseEntity<List<EamEquipmentDto>> eamEquipmentDtoList = deviceInterFaceUtils.getEamEquipment(restapiChkSNRoutingApiDto.getEquipmentCode());
            if (StringUtils.isEmpty(eamEquipmentDtoList.getData())) {
                check = fail + " 请求失败,设备编码不存在";
            }
        }
        //治具SN判断
        if(StringUtils.isNotEmpty(restapiChkSNRoutingApiDto.getEamJigBarCode())){
            String jigBarCode=restapiChkSNRoutingApiDto.getEamJigBarCode();
            String[] jigBarCodeA=jigBarCode.split(",");
            for (String item : jigBarCodeA) {
                if(StringUtils.isNotEmpty(item)) {
                    ResponseEntity<List<EamJigBarcodeDto>> eamJigBarcodeDtoList = deviceInterFaceUtils.getJigBarCode(item);
                    if (StringUtils.isEmpty(eamJigBarcodeDtoList.getData())) {
                        check = fail + " 请求失败,治具SN不存在";
                        break;
                    }
                    else {
                        //判断治具状态
                        EamJigBarcodeDto eamJigBarcodeDto=eamJigBarcodeDtoList.getData().get(0);
                        if (4 == eamJigBarcodeDto.getUsageStatus() || 5 == eamJigBarcodeDto.getUsageStatus()) {
                            check = fail+" 请求失败,治具SN状态处于维修或报废";
                            break;
                        }
                        //判断治具编码与产品绑定关系
                        Long JidID=eamJigBarcodeDto.getJigId();
                        ResponseEntity<List<EamJigReMaterialDto>> eamJigReMaterialDtoList = deviceInterFaceUtils.getEamJigReMaterial(MaterialId,JidID);
                        if (StringUtils.isEmpty(eamJigReMaterialDtoList.getData())) {
                            check = fail + " 请求失败,治具SN与产品SN没有绑定关系";
                            break;
                        }
                    }
                }
            }
        }
        //产前关键事项是否完成判断

        return check;
    }



}
