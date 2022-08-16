package com.fantechs.security.controller;


import com.fantechs.common.base.entity.security.SysAuthRole;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.service.SysAuthRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "更新权限列表", notes = "更新权限列表")
    @PostMapping("/updateBatch")
    public ResponseEntity update(@ApiParam(value = "必传：roleId、menuId", required = true) @RequestBody @Validated @NotNull(message = "sysAuthRoles不能为空") List<SysAuthRole> sysAuthRoles, @ApiParam(value = "必传：menuType", required = true) @RequestParam @Validated @NotNull(message = "menuType不能为空") Byte menuType) {
        return ControllerUtil.returnCRUD(smtAuthRoleService.updateBatch(sysAuthRoles, menuType));
    }

    @ApiOperation(value = "获取授权信息", notes = "获取授权信息")
    @PostMapping("/getSysAuthRole")
    public ResponseEntity<SysAuthRole> getSysAuthRole(@ApiParam(value = "必传：roleId", required = true) @RequestParam @Validated @NotNull(message = "roleId不能为空") Long roleId, @ApiParam(value = "必传：menuId", required = true) @RequestParam @Validated @NotNull(message = "menuId不能为空") Long menuId) {
        SysAuthRole sysAuthRole=smtAuthRoleService.getSysAuthRole(roleId, menuId);
        return  ControllerUtil.returnDataSuccess(sysAuthRole, StringUtils.isEmpty(sysAuthRole)?0:1);
    }
}
