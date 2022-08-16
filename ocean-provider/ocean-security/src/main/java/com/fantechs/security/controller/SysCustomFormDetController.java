package com.fantechs.security.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.security.SysCustomFormDetDto;
import com.fantechs.common.base.general.entity.security.SysCustomFormDet;
import com.fantechs.common.base.general.entity.security.search.SearchSysCustomFormDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.service.SysCustomFormDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/01/08.
 */
@RestController
@Api(tags = "自定义表单明细控制器")
@RequestMapping("/sysCustomFormDet")
@Validated
public class SysCustomFormDetController {

    @Autowired
    private SysCustomFormDetService sysCustomFormDetService;

    @ApiOperation(value = "全组织新增",notes = "新增")
    @PostMapping("/saveInAllOrg")
    public ResponseEntity saveInAllOrg(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SysCustomFormDet sysCustomFormDet) {
        return ControllerUtil.returnCRUD(sysCustomFormDetService.saveInAllOrg(sysCustomFormDet));
    }

    @ApiOperation("全组织删除")
    @PostMapping("/batchDeleteInAllOrg")
    public ResponseEntity batchDeleteInAllOrg(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(sysCustomFormDetService.batchDeleteInAllOrg(ids));
    }

    @ApiOperation("全组织修改")
    @PostMapping("/updateInAllOrg")
    public ResponseEntity updateInAllOrg(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SysCustomFormDet.update.class) SysCustomFormDet sysCustomFormDet) {
        return ControllerUtil.returnCRUD(sysCustomFormDetService.updateInAllOrg(sysCustomFormDet));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SysCustomFormDet sysCustomFormDet) {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        sysCustomFormDet.setOrgId(user.getOrganizationId());
        return ControllerUtil.returnCRUD(sysCustomFormDetService.save(sysCustomFormDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(sysCustomFormDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SysCustomFormDet.update.class) SysCustomFormDet sysCustomFormDet) {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        sysCustomFormDet.setOrgId(user.getOrganizationId());
        return ControllerUtil.returnCRUD(sysCustomFormDetService.update(sysCustomFormDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SysCustomFormDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SysCustomFormDet  sysCustomFormDet = sysCustomFormDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(sysCustomFormDet,StringUtils.isEmpty(sysCustomFormDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SysCustomFormDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSysCustomFormDet searchSysCustomFormDet) {
        Page<Object> page = PageHelper.startPage(searchSysCustomFormDet.getStartPage(),searchSysCustomFormDet.getPageSize());
        List<SysCustomFormDetDto> list = sysCustomFormDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSysCustomFormDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSysCustomFormDet searchSysCustomFormDet){
        List<SysCustomFormDetDto> list = sysCustomFormDetService.findList(ControllerUtil.dynamicConditionByEntity(searchSysCustomFormDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SysCustomFormDet信息", SysCustomFormDet.class, "SysCustomFormDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
