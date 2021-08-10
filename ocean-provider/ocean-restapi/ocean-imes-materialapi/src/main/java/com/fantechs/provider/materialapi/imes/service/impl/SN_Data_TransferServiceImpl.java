package com.fantechs.provider.materialapi.imes.service.impl;


import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.RestapiChkLogUserInfoApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiSNDataTransferApiDto;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.materialapi.imes.service.Chk_LogUserInfoService;
import com.fantechs.provider.materialapi.imes.service.SN_Data_TransferService;
import com.fantechs.provider.materialapi.imes.utils.DeviceInterFaceUtils;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.text.ParseException;
import java.util.List;

@WebService(serviceName = "SN_Data_TransferService", // 与接口中指定的name一致
        targetNamespace = "http://workOrder.imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.materialapi.imes.service.SN_Data_TransferService"// 接口地址
)
public class SN_Data_TransferServiceImpl implements SN_Data_TransferService {

    @Resource
    private LogsUtils logsUtils;
    @Resource
    private DeviceInterFaceUtils deviceInterFaceUtils;

    @Override
    public String SN_Data_Transfer(RestapiSNDataTransferApiDto restapiSNDataTransferApiDto) throws ParseException {
        String pass="Pass";
        String check = check(restapiSNDataTransferApiDto);
        if (!check.equals("1")) {
            logsUtils.addlog((byte) 0, (byte) 2, (long) 1002, check, restapiSNDataTransferApiDto.toString());
            return check;
        }
        logsUtils.addlog((byte)1,(byte)2,(long)1002,null,null);
        return pass+" 登录信息验证通过";
    }


    public String check(RestapiSNDataTransferApiDto restapiSNDataTransferApiDto) {
        String check = "1";
        String fail="Fail";
        Long orgId=null;
        ResponseEntity<List<BaseOrganizationDto>> baseOrganizationDtoList=deviceInterFaceUtils.getOrId();
        if(StringUtils.isEmpty(baseOrganizationDtoList.getData())){
            check = fail+" 请求失败,未查询到对应组织";
        }
        //获取组织ID
        orgId=baseOrganizationDtoList.getData().get(0).getOrganizationId();

        if(StringUtils.isEmpty(restapiSNDataTransferApiDto))
            check = fail+" 请求失败,参数为空";
        if(StringUtils.isEmpty(restapiSNDataTransferApiDto.getProCode()))
            check = fail+" 请求失败,产线编码不能为空";
        else{
            ResponseEntity<List<BaseProLine>> baseProLinelist=deviceInterFaceUtils.getProLine(restapiSNDataTransferApiDto.getProCode(),orgId);
            if(StringUtils.isEmpty(baseProLinelist.getData())){
                check = fail+" 请求失败,产线编码不存在";
            }

        }
        if(StringUtils.isEmpty(restapiSNDataTransferApiDto.getProcessCode()))
            check = fail+" 请求失败,工序编码不能为空";
        else {
            ResponseEntity<List<BaseProcess>> baseProcesslist=deviceInterFaceUtils.getProcess(restapiSNDataTransferApiDto.getProcessCode(),orgId);
            if(StringUtils.isEmpty(baseProcesslist.getData())){
                check = fail+" 请求失败,工序编码不存在";
            }
        }

        return check;
    }


}
