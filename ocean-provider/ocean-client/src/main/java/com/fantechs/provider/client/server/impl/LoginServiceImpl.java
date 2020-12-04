package com.fantechs.provider.client.server.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.basic.SmtClientManageDto;
import com.fantechs.common.base.electronic.dto.SmtElectronicTagControllerDto;
import com.fantechs.common.base.entity.basic.SmtClientManage;
import com.fantechs.common.base.entity.basic.SmtStorage;
import com.fantechs.common.base.entity.basic.search.SearchSmtClientManage;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.common.base.utils.TokenUtil;
import com.fantechs.provider.api.basic.ClientManageFeignApi;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.client.server.LoginService;
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
    public SmtClientManageDto login(SmtClientManage smtClientManage, HttpServletRequest request) {

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

            //电子标签控制器集合
            List<SmtElectronicTagControllerDto> electronicTagControllerDtos = new ArrayList<>();

            //通过电子标签控制器id列表获取电子标签和储位信息
            String ids = smtClientManageDto.getElectronicTagControllerIdList();
            String[] idsArr = ids.split(",");
            for (String id : idsArr) {
                //查询电子标签信息
                ResponseEntity<SmtElectronicTagControllerDto> responseElectronicTagControllerDto = electronicTagFeignApi.findById(id);
                if (StringUtils.isNotEmpty(responseElectronicTagControllerDto)) {

                    //通过电子标签id查询电子标签信息
                    SmtElectronicTagControllerDto electronicTagControllerDto = responseElectronicTagControllerDto.getData();
                    if (StringUtils.isEmpty(electronicTagControllerDto)){
                        throw new BizErrorException("该电子标签不存在");
                    }
                    electronicTagControllerDtos.add(electronicTagControllerDto);

                    //通过电子标签信息查询储位信息
                    ResponseEntity<List<SmtStorage>> responseSmtStorage = electronicTagFeignApi.findByElectronicTagControllerId(electronicTagControllerDto.getElectronicTagControllerId());
                    List<SmtStorage> smtStorages = responseSmtStorage.getData();
                    if (StringUtils.isNotEmpty(smtStorages)){
                        electronicTagControllerDto.setStorageList(smtStorages);
                    }
                } else {
                    throw new BizErrorException(ErrorCodeEnum.OPT20012003);
                }
            }
            smtClientManageDto.setElectronicTagControllerList(electronicTagControllerDtos);
        }

        return smtClientManageDto;
    }
}
