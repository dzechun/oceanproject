package com.fantechs.provider.client.controller;

import com.fantechs.common.base.electronic.dto.PtlEquipmentDto;
import com.fantechs.common.base.electronic.entity.PtlClientManage;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.client.server.impl.LoginServiceImpl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by lfz on 2020/11/30.
 */
@RestController
public class LoginController {

    @Resource
    private LoginServiceImpl loginServiceImpl;

    @PostMapping(value = "/login")
    public ResponseEntity<List<PtlEquipmentDto>> login(@RequestBody PtlClientManage ptlClientManage, HttpServletRequest request) {
        List<PtlEquipmentDto> ptlEquipmentDtos = loginServiceImpl.login(ptlClientManage, request);
        if (StringUtils.isEmpty(ptlEquipmentDtos)){
            return ControllerUtil.returnFail("验证失败", 100);
        }
        return ControllerUtil.returnSuccess("验证成功", ptlEquipmentDtos);
    }
}
