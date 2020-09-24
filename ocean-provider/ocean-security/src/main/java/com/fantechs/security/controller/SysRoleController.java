package com.fantechs.security.controller;

import com.fantechs.common.base.dto.security.SysRoleExcelDTO;
import com.fantechs.common.base.entity.security.SysRole;
import com.fantechs.common.base.entity.security.history.SysHtRole;
import com.fantechs.common.base.entity.security.search.SearchSysRole;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.service.SysHtRoleService;
import com.fantechs.security.service.SysRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
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
public class SysRoleController {
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysHtRoleService sysHtRoleService;

    @ApiOperation("查询角色信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SysRole>> selectRoles(
            @ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchSysRole searchSysRole){
        Page<Object> page = PageHelper.startPage(searchSysRole.getStartPage(),searchSysRole.getPageSize());
        List<SysRole> sysRoles = sysRoleService.selectRoles(searchSysRole);
        return ControllerUtil.returnDataSuccess(sysRoles,(int)page.getTotal());
    }

    @ApiOperation("增加角色信息")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：roleCode、roleName",required = true)@RequestBody SysRole sysRole){
        if(StringUtils.isEmpty(
                sysRole.getRoleCode(),
                sysRole.getRoleName())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(sysRoleService.insert(sysRole));
    }

    @ApiOperation("修改角色信息")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "角色信息对象，角色信息Id必传",required = true)@RequestBody SysRole sysRole){
        if(StringUtils.isEmpty(sysRole.getRoleId())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(sysRoleService.updateById(sysRole));
    }

    @ApiOperation("角色详情")
    @PostMapping("/detail")
    public ResponseEntity<SysRole> getMenuDetail(@ApiParam(value = "角色ID",required = true)@RequestParam Long roleId){
        if(StringUtils.isEmpty(roleId)){
            return ControllerUtil.returnFailByParameError();
        }
        SysRole sysRole = sysRoleService.selectByKey(roleId);
        return  ControllerUtil.returnDataSuccess(sysRole,StringUtils.isEmpty(sysRole)?0:1);
    }

    @ApiOperation("删除角色信息")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "角色对象ID",required = true)@RequestBody List<Long> roleIds){
        if(StringUtils.isEmpty(roleIds)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(sysRoleService.deleteByIds(roleIds));
    }

    @ApiOperation("绑定用户")
    @PostMapping("/addUser")
    public ResponseEntity addUser(
            @ApiParam(value = "角色ID",required = true)@RequestParam Long roleId,
            @ApiParam(value = "用户ID",required = false)@RequestBody List<Long> userIds){
        if(StringUtils.isEmpty(roleId)){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(sysRoleService.addUser(roleId,userIds));
    }

    /**
     * 导出数据
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出角色信息excel",notes = "导出角色信息excel")
    public void exportRoles(HttpServletResponse response, @ApiParam(value ="输入查询条件",required = false)
    @RequestBody(required = false) SearchSysRole searchSysRole){
        List<SysRole> list = sysRoleService.selectRoles(searchSysRole);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "角色信息导出", "角色信息", SysRoleExcelDTO.class, "角色信息.xls", response);
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
}
