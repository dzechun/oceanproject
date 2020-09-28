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
import com.fantechs.common.base.utils.TemplateFileUtil;
import com.fantechs.security.service.SysHtUserService;
import com.fantechs.security.service.SysUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    public ResponseEntity<SysUser> selectUserById(@ApiParam(value = "传入主键userId",required = true) @RequestParam Long id) {
        SysUser sysUser = sysUserService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(sysUser,StringUtils.isEmpty(sysUser)?0:1);
    }

    @ApiOperation("增加用户信息")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：account、userCode、userName、password、status",required = true)@RequestBody SysUser sysUser){
        if(StringUtils.isEmpty(
                sysUser.getUserCode(),
                sysUser.getUserName(),
                sysUser.getPassword(),
                sysUser.getStatus())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(sysUserService.save(sysUser));
    }


    @ApiOperation("修改用户信息")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "用户信息对象，用户信息Id必传",required = true)@RequestBody SysUser sysUser){
        if(StringUtils.isEmpty(sysUser.getUserId())){
            return ControllerUtil.returnFailByParameError();
        }
        return ControllerUtil.returnCRUD(sysUserService.update(sysUser));
    }


    @PostMapping("/delete")
    @ApiOperation(value = "删除用户信息",notes = "删除用户信息")
    public ResponseEntity delUser(@ApiParam(value = "传入主键userId",required = true) @RequestBody String ids) {
        if(StringUtils.isEmpty(ids)){
            return ControllerUtil.returnFail("缺少必需参数", ErrorCodeEnum.OPT20012002.getCode());
        }
        return ControllerUtil.returnCRUD(sysUserService.batchDelete(ids));
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出用户信息excel",notes = "导出用户信息excel")
    public void exportUsers(HttpServletResponse response, @ApiParam(value ="输入查询条件")
    @RequestBody(required = false) SearchSysUser searchSysUser){
        List<SysUserExcelDTO> list = sysUserService.selectUsersExcelDto(searchSysUser);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "用户信息导出", "用户信息", SysUserExcelDTO.class, "用户信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }

    @GetMapping("/downloadTemplate")
    @ApiOperation(value = "下载模板", notes = "下载模板")
    public void downloadTemplate(HttpServletResponse response){
        String fileName ="SysUser.xlsx";
        TemplateFileUtil.downloadTemplate(TemplateFileUtil.OCEAN_SECURITY_EXCEL, fileName, response);
    }

    /**
     * 从excel导入数据
     * @return
     * @throws
     */
    @PostMapping(value = "/import")
    @ApiOperation(value = "从excel导入用户信息",notes = "从excel导入用户信息")
    public ResponseEntity importUsers(@ApiParam(value ="输入excel文件",required = true)
                                      @RequestBody(required = true) MultipartFile file){
        int i=0;
        try {
            // 导入操作
            List<SysUser> sysUsers = EasyPoiUtils.importExcel(file,SysUser.class);
            i= sysUserService.importUsers(sysUsers);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return ControllerUtil.returnFail(e.getMessage(), ErrorCodeEnum.OPT20012002.getCode());
        }
        return ControllerUtil.returnCRUD(i);
    }

    @PostMapping("/findHtList")
    @ApiOperation(value = "根据条件查询用户信息",notes = "根据条件查询用户信息")
    public ResponseEntity<List<SysHtUser>> selectHtUsers(
            @ApiParam(value ="输入查询条件",required = false)@RequestBody(required = false) SearchSysUser searchSysUser){
        Page<Object> page = PageHelper.startPage(searchSysUser.getStartPage(),searchSysUser.getPageSize());
        List<SysHtUser> sysHtUsers = sysHtUserService.selectHtUsers(searchSysUser);
        return  ControllerUtil.returnDataSuccess(sysHtUsers, (int)page.getTotal());
    }

}
