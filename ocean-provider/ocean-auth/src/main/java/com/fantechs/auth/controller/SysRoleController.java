package com.fantechs.auth.controller;

import com.fantechs.common.base.dto.security.SysRoleDto;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.SysUserRole;
import com.fantechs.common.base.entity.security.history.SysHtRole;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.auth.service.SysHtRoleService;
import com.fantechs.auth.service.SysRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Auther: wcz
 * @Date: 2020/8/18 15:15
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/sysRole")
@Api(tags = "角色管理")
@Slf4j
@Validated
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysHtRoleService sysHtRoleService;

    @ApiOperation("查询角色信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SysRoleDto>> selectRoles(@ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchSysRole searchSysRole){
        Page<Object> page = PageHelper.startPage(searchSysRole.getStartPage(),searchSysRole.getPageSize());
        List<SysRoleDto> sysRoles = sysRoleService.selectRoles(searchSysRole);
        return ControllerUtil.returnDataSuccess(sysRoles,(int)page.getTotal());
    }

    @ApiOperation("增加角色信息")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：roleCode、roleName",required = true)@RequestBody @Validated SysRole sysRole){
        return ControllerUtil.returnCRUD(sysRoleService.save(sysRole));
    }

    @ApiOperation("修改角色信息")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "角色信息对象，角色信息Id必传",required = true)@RequestBody @Validated(value = SysRole.update.class) SysRole sysRole){
        return ControllerUtil.returnCRUD(sysRoleService.update(sysRole));
    }

    @ApiOperation("角色详情")
    @PostMapping("/detail")
    public ResponseEntity<SysRole> getMenuDetail(@ApiParam(value = "角色ID",required = true)@RequestParam @NotNull(message = "id不能为空") Long id){
        SysRole sysRole = sysRoleService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(sysRole,StringUtils.isEmpty(sysRole)?0:1);
    }

    @ApiOperation("删除角色信息")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "角色对象ID",required = true)@RequestParam  @NotBlank(message = "ids不能为空")String ids){
        return ControllerUtil.returnCRUD(sysRoleService.batchDelete(ids));
    }

    @ApiOperation("绑定用户")
    @PostMapping("/addUser")
    public ResponseEntity addUser(
            @ApiParam(value = "角色Id",required = true)@RequestParam @NotNull(message = "角色Id不能为空") Long roleId,
            @ApiParam(value = "用户Id",required = true)@RequestBody @NotNull(message = "userIds不能为空") List<Long> userIds){
        return ControllerUtil.returnCRUD(sysRoleService.addUser(roleId,userIds));
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出角色信息excel",notes = "导出角色信息excel",produces = "application/octet-stream")
    public void exportRoles(HttpServletResponse response, @ApiParam(value ="输入查询条件",required = false)
    @RequestBody(required = false) SearchSysRole searchSysRole){
        List<SysRoleDto> list = sysRoleService.selectRoles(searchSysRole);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "角色信息导出", "角色信息", SysRoleDto.class, "角色信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }


    @PostMapping("/findHtList")
    @ApiOperation(value = "根据条件查询角色履历信息",notes = "根据条件查询角色履历信息")
    public ResponseEntity<List<SysHtRole>> selectHtRoles(
            @ApiParam(value ="输入查询条件",required = false)@RequestBody(required = false) SearchSysRole searchSysRole)
    {
        Page<Object> page = PageHelper.startPage(searchSysRole.getStartPage(),searchSysRole.getPageSize());
        List<SysHtRole> sysHtRoles = sysHtRoleService.selectHtRoles(searchSysRole);
        return  ControllerUtil.returnDataSuccess(sysHtRoles, (int)page.getTotal());
    }

    @ApiOperation(value = "获取用户角色信息", notes = "获取用户角色信息")
    @PostMapping("/findUserRoleList")
    public ResponseEntity<List<SysUserRole>> findUserRoleList(@ApiParam(value = "必传：userId", required = true) @RequestParam @Validated @NotNull(message = "userId不能为空") Long userId) {
        List<SysUserRole> sysUserRoleList=sysRoleService.findUserRoleList(userId);
        return  ControllerUtil.returnDataSuccess(sysUserRoleList, StringUtils.isEmpty(sysUserRoleList)?0:sysUserRoleList.size());
    }

    @ApiOperation("用户绑定角色")
    @PostMapping("/addUserRole")
    public ResponseEntity addUserRole(
            @ApiParam(value = "用户Id",required = true)@RequestParam @NotNull(message = "角色Id不能为空") Long userId,
            @ApiParam(value = "角色id",required = true)@RequestBody @NotNull(message = "userIds不能为空") List<Long> roleIds){
        return ControllerUtil.returnCRUD(sysRoleService.addUserRole(userId,roleIds));
    }
}
