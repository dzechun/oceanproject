package com.fantechs.provider.materialapi.imes.service.impl;


import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.RestapiChkLogUserInfoApiDto;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcess;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.materialapi.imes.service.Chk_LogUserInfoService;
import com.fantechs.provider.materialapi.imes.utils.DeviceInterFaceUtils;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebService(serviceName = "Chk_LogUserInfoService", // 与接口中指定的name一致
        targetNamespace = "http://workOrder.imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.materialapi.imes.service.Chk_LogUserInfoService"// 接口地址
)
public class Chk_LogUserInfoServiceImpl implements Chk_LogUserInfoService {

    @Resource
    private LogsUtils logsUtils;
    @Resource
    private DeviceInterFaceUtils deviceInterFaceUtils;

    @Override
    public String Chk_LogUserInfo(RestapiChkLogUserInfoApiDto restapiChkLogUserInfoApiDto) throws ParseException {
        String pass="Pass";
        String check = check(restapiChkLogUserInfoApiDto);
        if (!check.equals("1")) {
            logsUtils.addlog((byte) 0, (byte) 2, (long) 1002, check, restapiChkLogUserInfoApiDto.toString());
            return check;
        }
        logsUtils.addlog((byte)1,(byte)2,(long)1002,null,null);
        return pass+" 登录信息验证通过";
    }


    public String check(RestapiChkLogUserInfoApiDto restapiChkLogUserInfoApiDto) {
        String check = "1";
        String fail="Fail";
        Long orgId=null;
        ResponseEntity<List<BaseOrganizationDto>> baseOrganizationDtoList=deviceInterFaceUtils.getOrId();
        if(StringUtils.isEmpty(baseOrganizationDtoList.getData())){
            check = fail+" 请求失败,未查询到对应组织";
        }
        //获取组织ID
        orgId=baseOrganizationDtoList.getData().get(0).getOrganizationId();

        if(StringUtils.isEmpty(restapiChkLogUserInfoApiDto))
            check = fail+" 请求失败,参数为空";
        if(StringUtils.isEmpty(restapiChkLogUserInfoApiDto.getProCode()))
            check = fail+" 请求失败,产线编码不能为空";
        else{
            ResponseEntity<List<BaseProLine>> baseProLinelist=deviceInterFaceUtils.getProLine(restapiChkLogUserInfoApiDto.getProCode(),orgId);
            if(StringUtils.isEmpty(baseProLinelist.getData())){
                check = fail+" 请求失败,产线编码不存在";
            }

        }
        if(StringUtils.isEmpty(restapiChkLogUserInfoApiDto.getProcessCode()))
            check = fail+" 请求失败,工序编码不能为空";
        else {
            ResponseEntity<List<BaseProcess>> baseProcesslist=deviceInterFaceUtils.getProcess(restapiChkLogUserInfoApiDto.getProcessCode(),orgId);
            if(StringUtils.isEmpty(baseProcesslist.getData())){
                check = fail+" 请求失败,工序编码不存在";
            }
        }
        if(StringUtils.isEmpty(restapiChkLogUserInfoApiDto.getUserCode()))
            check = fail+" 请求失败,登录用户帐号不能为空";

        if(StringUtils.isEmpty(restapiChkLogUserInfoApiDto.getPassword()))
            check = fail+" 请求失败,登录用户密码不能为空";

        //验证用户账号和密码是否正确
        ResponseEntity<List<SysUser>> sysUserlist=deviceInterFaceUtils.getSysUser(restapiChkLogUserInfoApiDto.getUserCode(),orgId);
        if(StringUtils.isEmpty(sysUserlist.getData())){
            check = fail+" 请求失败,登录用户帐号不存在";
        }
        else{
            SysUser sysUser=sysUserlist.getData().get(0);
            String passWord=new BCryptPasswordEncoder().encode(restapiChkLogUserInfoApiDto.getPassword());
            if(!sysUser.getPassword().equals(passWord))
                check = fail+" 请求失败,登录用户密码不正确";
        }

        return check;
    }


}
