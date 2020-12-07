package com.fantechs.provider.client.server.impl;

import com.fantechs.common.base.dto.basic.SmtClientManageDto;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagStorageDto;
import com.fantechs.common.base.electronic.dto.SmtEquipmentDto;
import com.fantechs.common.base.electronic.entity.search.SearchSmtElectronicTagStorage;
import com.fantechs.common.base.electronic.entity.search.SearchSmtEquipment;
import com.fantechs.common.base.entity.basic.SmtClientManage;
import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.basic.search.SearchSmtClientManage;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.TokenUtil;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.api.imes.basic.ClientManageFeignApi;
import com.fantechs.provider.client.server.LoginService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private ClientManageFeignApi clientManageFeignApi;
    @Resource
    private ElectronicTagFeignApi electronicTagFeignApi;

    @Override
    public List<SmtEquipmentDto> login(SmtClientManage smtClientManage, HttpServletRequest request) {

        //通过登录密钥查询客户端信息
        SearchSmtClientManage searchSmtClientManage = new SearchSmtClientManage();
        searchSmtClientManage.setSecretKey(smtClientManage.getSecretKey());
        ResponseEntity<List<SmtClientManageDto>> clientManageList = clientManageFeignApi.findList(searchSmtClientManage);
        SmtClientManageDto smtClientManageDto = clientManageList.getData().get(0);

        if (StringUtils.isNotEmpty(smtClientManageDto)) {
            //登录成功更新客户端的登录状态和登录时间
            smtClientManageDto.setLoginIp(TokenUtil.getIpAddress(request));
            smtClientManageDto.setLoginTime(new Date());
            smtClientManageDto.setOnlineStatus("1");
            smtClientManageDto.setLoginTag((byte) 1);
            clientManageFeignApi.update(smtClientManageDto);


            //根据客户端id查询电子标签信息
            SearchSmtEquipment searchSmtEquipment = new SearchSmtEquipment();
            ResponseEntity<List<SmtEquipmentDto>> responseEntity = electronicTagFeignApi.findList(searchSmtEquipment);
            List<SmtEquipmentDto> equipmentDtos = responseEntity.getData();

            //根据电子标签信息查询储位信息
            if (StringUtils.isNotEmpty(equipmentDtos)){
                List<SmtStorage> smtStorages = new ArrayList<>();
                SearchSmtElectronicTagStorage searchSmtElectronicTagStorage = new SearchSmtElectronicTagStorage();
                for (SmtEquipmentDto equipmentDto : equipmentDtos) {
                    searchSmtElectronicTagStorage.setEquipmentId(equipmentDto.getEquipmentId());
                    ResponseEntity<List<SmtElectronicTagStorageDto>> responseEntity1 = electronicTagFeignApi.findList(searchSmtElectronicTagStorage);
                    List<SmtElectronicTagStorageDto> electronicTagStorageDtos = responseEntity1.getData();
                    for (SmtElectronicTagStorageDto electronicTagStorageDto : electronicTagStorageDtos) {
                        SmtStorage smtStorage = new SmtStorage();
                        BeanUtils.copyProperties(electronicTagStorageDto,smtStorage);
                        smtStorages.add(smtStorage);
                    }
                    equipmentDto.setStorageList(smtStorages);
                }
            }
            return equipmentDtos;
        }
        throw new BizErrorException("该客户端未绑定设备");
    }
}
