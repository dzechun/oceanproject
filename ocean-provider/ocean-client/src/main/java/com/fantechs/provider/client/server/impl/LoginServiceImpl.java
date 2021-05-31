package com.fantechs.provider.client.server.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlClientManageDto;
import com.fantechs.common.base.electronic.dto.PtlEquipmentDto;
import com.fantechs.common.base.electronic.entity.PtlClientManage;
import com.fantechs.common.base.electronic.entity.search.SearchPtlClientManage;
import com.fantechs.common.base.electronic.entity.search.SearchPtlEquipment;
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
    public List<PtlEquipmentDto> login(@RequestBody PtlClientManage ptlClientManage, HttpServletRequest request) {

        //通过登录密钥查询客户端信息
        SearchPtlClientManage searchPtlClientManage = new SearchPtlClientManage();
        searchPtlClientManage.setSecretKey(ptlClientManage.getSecretKey());
        ResponseEntity<List<PtlClientManageDto>> clientManageList = electronicTagFeignApi.findClientManageList(searchPtlClientManage);
        if(StringUtils.isEmpty(clientManageList)){
            throw  new BizErrorException(ErrorCodeEnum.GL99990100);
        }

        PtlClientManageDto ptlClientManageDto = clientManageList.getData().get(0);
        List<PtlEquipmentDto> equipmentDtoList=new LinkedList<>();

        if (StringUtils.isNotEmpty(ptlClientManageDto)) {
            //登录成功更新客户端的登录状态和登录时间
            ptlClientManageDto.setLoginIp(TokenUtil.getIpAddress(request));
            ptlClientManageDto.setLoginTime(new Date());
            ptlClientManageDto.setOnlineStatus("1");
            ptlClientManageDto.setLoginTag((byte) 1);
            electronicTagFeignApi.updateClientManage(ptlClientManageDto);


            //根据客户端id查询电子标签信息
            SearchPtlEquipment searchPtlEquipment = new SearchPtlEquipment();
            searchPtlEquipment.setEquipmentType((byte) 0);
            searchPtlEquipment.setClientId(ptlClientManageDto.getClientId());
            equipmentDtoList = electronicTagFeignApi.findEquipmentList(searchPtlEquipment).getData();
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
