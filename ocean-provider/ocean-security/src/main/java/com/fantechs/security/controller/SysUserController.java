package com.fantechs.security.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.dto.security.SysUserExcelDTO;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.history.SysHtUser;
import com.fantechs.common.base.entity.security.search.SearchSysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.service.SysHtUserService;
import com.fantechs.security.service.SysUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @Auther: wcz
 * @Date: 2020/8/15 9:15
 * @Description:
 * @Version: 1.0
 */
@RestController
@RequestMapping(value = "/sysUser")
@Api(tags = "用户管理")
@Slf4j
@Validated
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysHtUserService sysHtUserService;


    @PostMapping("/findList")
    @ApiOperation(value = "查询用户列表",notes = "根据条件查询用户信息")
    public ResponseEntity<List<SysUser>> selectUsers(
            @ApiParam(value ="输入查询条件",required = false)@RequestBody(required = false) SearchSysUser searchSysUser ) {
        Page<Object> page = PageHelper.startPage(searchSysUser.getStartPage(),searchSysUser.getPageSize());
        List<SysUser> sysUsers = sysUserService.selectUsers(searchSysUser);
        return  ControllerUtil.returnDataSuccess(sysUsers, (int)page.getTotal());
    }


    @GetMapping("/detail")
    @ApiOperation(value = "通过ID获取单个用户信息",notes = "通过ID获取单个用户信息")
    public ResponseEntity<SysUser> selectUserById(@ApiParam(value = "传入主键userId",required = true) @RequestParam @NotNull(message = "id不能为空")Long id) {
        SysUser sysUser = sysUserService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(sysUser,StringUtils.isEmpty(sysUser)?0:1);
    }

    @ApiOperation("增加用户信息")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：account、userCode、userName、password、status",required = true)@RequestBody @Validated SysUser sysUser){
        return ControllerUtil.returnCRUD(sysUserService.save(sysUser));
    }


    @ApiOperation("修改用户信息")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "用户信息对象，用户信息Id必传",required = true)@RequestBody @Validated(value = SysUser.update.class) SysUser sysUser){
        return ControllerUtil.returnCRUD(sysUserService.update(sysUser));
    }

    @PostMapping("/switchOrganization")
    @ApiOperation(value = "切换用户组织",notes = "切换用户组织")
    public ResponseEntity switchOrganization(@ApiParam(value = "组织ID",required = true) @RequestParam Long organizationId) {
        return ControllerUtil.returnCRUD(sysUserService.switchOrganization(organizationId));
    }

    @ApiOperation("修改用户密码")
    @GetMapping("/updatePassword")
    public ResponseEntity updatePassword(@ApiParam(value = "旧密码",required = true)@RequestParam String oldPassword,@ApiParam(value = "新密码",required = true)@RequestParam String newPassword){
        return ControllerUtil.returnCRUD(sysUserService.updatePassword(oldPassword,newPassword));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除用户信息",notes = "删除用户信息")
    public ResponseEntity delUser(@ApiParam(value = "传入主键userId",required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(sysUserService.batchDelete(ids));
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出用户信息excel",notes = "导出用户信息excel",produces = "application/octet-stream")
    public void exportUsers(HttpServletResponse response, @ApiParam(value ="输入查询条件")
    @RequestBody(required = false) SearchSysUser searchSysUser){
        List<SysUser> list = sysUserService.selectUsers(searchSysUser);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "用户信息导出", "用户信息", SysUser.class, "SysUser.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入用户信息",notes = "从excel导入用户信息")
    public ResponseEntity importUsers(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestPart(value="file") MultipartFile file){
        try {
            // 导入操作
            List<SysUserExcelDTO> sysUserExcelDTOS = EasyPoiUtils.importExcel(file, 2, 1, SysUserExcelDTO.class);
            Map<String, Object> resultMap = sysUserService.importUsers(sysUserExcelDTOS);
            return ControllerUtil.returnDataSuccess("操作结果集",resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }

    }

    @PostMapping("/findHtList")
    @ApiOperation(value = "根据条件查询用户信息",notes = "根据条件查询用户信息")
    public ResponseEntity<List<SysHtUser>> selectHtUsers(
            @ApiParam(value ="输入查询条件",required = false)@RequestBody(required = false) SearchSysUser searchSysUser){
        Page<Object> page = PageHelper.startPage(searchSysUser.getStartPage(),searchSysUser.getPageSize());
        List<SysHtUser> sysHtUsers = sysHtUserService.selectHtUsers(searchSysUser);
        return  ControllerUtil.returnDataSuccess(sysHtUsers, (int)page.getTotal());
    }

    @ApiOperation(value = "接口新增或更新",notes = "接口新增或更新")
    @PostMapping("/saveByApi")
    public ResponseEntity<SysUser> saveByApi(@ApiParam(value = "必传：userCode、userName、password、status",required = true)@RequestBody @Validated SysUser sysUser) {
        return ControllerUtil.returnDataSuccess(sysUserService.saveByApi(sysUser),1);
    }

}
