package com.fantechs.security.controller;


import com.fantechs.common.base.entity.security.SysAuthRole;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.security.service.SysAuthRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by lfz on 2020/8/21.
 */
@RestController
@Api(tags = "权限控制器")
@RequestMapping("/authRole")
public class SysAuthRoleController {

    @Autowired
    private SysAuthRoleService smtAuthRoleService;

    @ApiOperation(value = "更新权限列表",notes = "更新权限列表")
    @PostMapping("/updateBatch")
    public ResponseEntity update(@ApiParam(value = "必传：roleId、menuId",required = true)@RequestBody @Validated @NotNull(message = "sysAuthRoles不能为空") List<SysAuthRole> sysAuthRoles){
        return ControllerUtil.returnCRUD(smtAuthRoleService.updateBatch(sysAuthRoles));
    }
}
