package com.fantechs.provider.client.server.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.SmtClientManageDto;
import com.fantechs.common.base.electronic.dto.SmtEquipmentDto;
import com.fantechs.common.base.electronic.entity.SmtClientManage;
import com.fantechs.common.base.electronic.entity.search.SearchSmtClientManage;
import com.fantechs.common.base.electronic.entity.search.SearchSmtEquipment;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.TokenUtil;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.client.server.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private ElectronicTagFeignApi electronicTagFeignApi;

    @Override
    public List<SmtEquipmentDto> login(@RequestBody SmtClientManage smtClientManage, HttpServletRequest request) {

        //通过登录密钥查询客户端信息
        SearchSmtClientManage searchSmtClientManage = new SearchSmtClientManage();
        searchSmtClientManage.setSecretKey(smtClientManage.getSecretKey());
        ResponseEntity<List<SmtClientManageDto>> clientManageList = electronicTagFeignApi.findClientManageList(searchSmtClientManage);
        if(StringUtils.isEmpty(clientManageList)){
            throw  new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        SmtClientManageDto smtClientManageDto = clientManageList.getData().get(0);
        List<SmtEquipmentDto> equipmentDtoList=new LinkedList<>();

        if (StringUtils.isNotEmpty(smtClientManageDto)) {
            //登录成功更新客户端的登录状态和登录时间
            smtClientManageDto.setLoginIp(TokenUtil.getIpAddress(request));
            smtClientManageDto.setLoginTime(new Date());
            smtClientManageDto.setOnlineStatus("1");
            smtClientManageDto.setLoginTag((byte) 1);
            electronicTagFeignApi.updateClientManage(smtClientManageDto);


            //根据客户端id查询电子标签信息
            SearchSmtEquipment searchSmtEquipment = new SearchSmtEquipment();
            searchSmtEquipment.setEquipmentType((byte) 0);
            searchSmtEquipment.setClientId(smtClientManageDto.getClientId());
            equipmentDtoList = electronicTagFeignApi.findEquipmentList(searchSmtEquipment).getData();
            if(StringUtils.isEmpty(equipmentDtoList)){
                throw new BizErrorException("该客户端未绑定设备");
            }
//            //根据电子标签信息查询储位信息
//            if (StringUtils.isNotEmpty(equipmentDtoList)){
//                SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
//                for (SmtEquipmentDto equipmentDto : equipmentDtoList) {
//                    searchSmtElectronicTagStorage.setEquipmentId(equipmentDto.getEquipmentId());
//                    ResponseEntity<List<SmtElectronicTagStorageDto>> responseEntity1 = electronicTagFeignApi.findElectronicTagStorageList(searchSmtElectronicTagStorage);
//                    equipmentDto.setElectronicTagStorageList(responseEntity1.getData());
//                }
//            }

        }
        return equipmentDtoList;
    }
}
