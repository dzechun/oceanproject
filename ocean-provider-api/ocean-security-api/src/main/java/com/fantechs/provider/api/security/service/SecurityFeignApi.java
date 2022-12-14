package com.fantechs.provider.api.security.service;


import com.fantechs.common.base.dto.security.SysRoleDto;
import com.fantechs.common.base.entity.security.*;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.response.ResponseEntity;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;
import java.util.List;


/**
 * Created by lfz on 2020/8/6.
 */
@FeignClient(value = "ocean-security")
public interface SecurityFeignApi {

    @PostMapping(value = "/login")
    ResponseEntity login(@RequestParam(value = "username") String username, @RequestParam(value = "password") String password,
                         @RequestParam(value = "orgId") Long orgId, @RequestParam(value = "type") String type  , @RequestParam(value = "browserKernel") String browserKernel);


    @GetMapping(value = "/logout")
    ResponseEntity logout();

    @ApiOperation("增加用户信息")
    @PostMapping("/sysUser/add")
    ResponseEntity add(@ApiParam(value = "必传：account、userCode、userName、password、status",required = true)@RequestBody @Validated SysUser sysUser);

    @ApiOperation("修改用户信息")
    @PostMapping("/sysUser/update")
    ResponseEntity update(@ApiParam(value = "用户信息对象，用户信息Id必传",required = true)@RequestBody @Validated(value = SysUser.update.class) SysUser sysUser);

    @GetMapping("/sysUser/detail")
    @ApiOperation(value = "通过ID获取单个用户信息", notes = "通过ID获取单个用户信息")
    ResponseEntity<SysUser> selectUserById(@ApiParam(value = "传入主键userId", required = true) @RequestParam(value = "id") Long id);

    @ApiOperation("根据条件查询程序配置项列表")
    @PostMapping("/sysSpecItem/findList")
    ResponseEntity<List<SysSpecItem>> findSpecItemList(@ApiParam(value = "查询条件，请参考Model说明") @RequestBody(required = false) SearchSysSpecItem searchSysSpecItem);

    @PostMapping("/sysUser/findList")
    @ApiOperation(value = "查询用户列表",notes = "根据条件查询用户信息")
    ResponseEntity<List<SysUser>> selectUsers(
            @ApiParam(value ="输入查询条件",required = false)@RequestBody(required = false) SearchSysUser searchSysUser );

    @ApiOperation(value = "新增接口日志",notes = "新增接口日志(组织、创建人、创建时间已有)")
    @PostMapping("/sysApiLog/add")
    ResponseEntity add(@ApiParam(value = "必传",required = true) @RequestBody  SysApiLog sysApiLog);

    @ApiOperation(value = "批量新增接口日志",notes = "新增接口日志(组织、创建人、创建时间已有)")
    @PostMapping("/sysApiLog/batchAdd")
    ResponseEntity batchAdd(@ApiParam(value = "必传",required = true) @RequestBody List<SysApiLog> logList);

    @ApiOperation("获取用户角色信息")
    @PostMapping("/sysRole/findUserRoleList")
    ResponseEntity<List<SysUserRole>> findUserRoleList(@ApiParam(value = "必传：userId", required = true) @RequestParam @Validated @NotNull(message = "userId不能为空") Long userId);

    @ApiOperation("获取授权信息")
    @PostMapping("/authRole/getSysAuthRole")
    ResponseEntity<SysAuthRole> getSysAuthRole(@ApiParam(value = "必传：roleId", required = true) @RequestParam @Validated @NotNull(message = "roleId不能为空") Long roleId, @ApiParam(value = "必传：menuId", required = true) @RequestParam @Validated @NotNull(message = "menuId不能为空") Long menuId);


    @ApiOperation(value = "同步默认自定义表单的数据到自定义表单",notes = "同步默认自定义表单的数据到自定义表单")
    @PostMapping("/sysDefaultCustomForm/syncDefaultData")
    ResponseEntity syncDefaultData(@ApiParam(value = "组织id",required = true)@RequestParam  @NotNull(message="组织id不能为空") Long orgId);

    @ApiOperation("接口新增或修改供应商用户信息")
    @PostMapping("/sysUser/saveByApi")
    ResponseEntity<SysUser> saveByApi(@ApiParam(value = "必传：userCode、userName、password、status",required = true)@RequestBody @Validated SysUser sysUser);

    @ApiOperation("查询角色信息")
    @PostMapping("/sysRole/findList")
    ResponseEntity<List<SysRoleDto>> selectRoles(@ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchSysRole searchSysRole);


}
