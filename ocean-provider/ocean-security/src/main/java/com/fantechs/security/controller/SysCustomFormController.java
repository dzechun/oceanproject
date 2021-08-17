package com.fantechs.security.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.security.SysCustomFormDto;
import com.fantechs.common.base.general.entity.security.SysCustomForm;
import com.fantechs.common.base.general.entity.security.search.SearchSysCustomForm;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.service.SysCustomFormService;
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
@Api(tags = "自定义表单控制器")
@RequestMapping("/sysCustomForm")
@Validated
public class SysCustomFormController {

    @Autowired
    private SysCustomFormService sysCustomFormService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SysCustomForm sysCustomForm) {
        // 获取登录用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        sysCustomForm.setOrgId(user.getOrganizationId());
        return ControllerUtil.returnCRUD(sysCustomFormService.save(sysCustomForm));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(sysCustomFormService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SysCustomForm.update.class) SysCustomForm sysCustomForm) {
        return ControllerUtil.returnCRUD(sysCustomFormService.update(sysCustomForm));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SysCustomForm> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SysCustomForm  sysCustomForm = sysCustomFormService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(sysCustomForm,StringUtils.isEmpty(sysCustomForm)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SysCustomFormDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSysCustomForm searchSysCustomForm) {
        Page<Object> page = PageHelper.startPage(searchSysCustomForm.getStartPage(),searchSysCustomForm.getPageSize());
        List<SysCustomFormDto> list = sysCustomFormService.findList(ControllerUtil.dynamicConditionByEntity(searchSysCustomForm));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSysCustomForm searchSysCustomForm){
        List<SysCustomFormDto> list = sysCustomFormService.findList(ControllerUtil.dynamicConditionByEntity(searchSysCustomForm));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SysCustomForm信息", SysCustomForm.class, "SysCustomForm.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
