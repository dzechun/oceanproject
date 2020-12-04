package com.fantechs.provider.client.controller;

import com.fantechs.common.base.dto.basic.SmtClientManageDto;
import com.fantechs.common.base.entity.basic.SmtClientManage;
import com.fantechs.common.base.entity.basic.search.SearchSmtClientManage;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.client.server.impl.LoginServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by lfz on 2020/11/30.
 */
@RestController
public class LoginController {

    @Resource
    private LoginServiceImpl loginServiceImpl;

    @PostMapping(value = "/login")
    public ResponseEntity<SmtClientManageDto> sendElectronicTagStorage(@RequestBody SmtClientManage smtClientManage, HttpServletRequest request) {

        SmtClientManageDto smtClientManageDto = loginServiceImpl.login(smtClientManage, request);
        if (smtClientManageDto == null){
            return ControllerUtil.returnFail("验证失败", 100);
        }
        return ControllerUtil.returnSuccess("验证成功",smtClientManageDto);
    }
}
