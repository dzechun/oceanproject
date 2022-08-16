package com.fantechs.security.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.security.SysDefaultCustomFormDto;
import com.fantechs.common.base.general.entity.security.SysDefaultCustomForm;
import com.fantechs.common.base.general.entity.security.search.SearchSysDefaultCustomForm;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.service.SysDefaultCustomFormService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */
@RestController
@Api(tags = "默认自定义表单控制器")
@RequestMapping("/sysDefaultCustomForm")
@Validated
public class SysDefaultCustomFormController {

    @Resource
    private SysDefaultCustomFormService sysDefaultCustomFormService;

    @ApiOperation(value = "同步默认自定义表单的数据到自定义表单",notes = "同步默认自定义表单的数据到自定义表单")
    @PostMapping("/syncDefaultData")
    public ResponseEntity syncDefaultData(@ApiParam(value = "组织id",required = true)@RequestParam  @NotNull(message="组织id不能为空") Long orgId) {
        return ControllerUtil.returnCRUD(sysDefaultCustomFormService.syncDefaultData(orgId));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SysDefaultCustomForm sysDefaultCustomForm) {
        return ControllerUtil.returnCRUD(sysDefaultCustomFormService.save(sysDefaultCustomForm));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(sysDefaultCustomFormService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SysDefaultCustomForm.update.class) SysDefaultCustomForm sysDefaultCustomForm) {
        return ControllerUtil.returnCRUD(sysDefaultCustomFormService.update(sysDefaultCustomForm));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SysDefaultCustomForm> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SysDefaultCustomForm  sysDefaultCustomForm = sysDefaultCustomFormService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(sysDefaultCustomForm, StringUtils.isEmpty(sysDefaultCustomForm)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SysDefaultCustomFormDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSysDefaultCustomForm searchSysDefaultCustomForm) {
        Page<Object> page = PageHelper.startPage(searchSysDefaultCustomForm.getStartPage(),searchSysDefaultCustomForm.getPageSize());
        List<SysDefaultCustomFormDto> list = sysDefaultCustomFormService.findList(ControllerUtil.dynamicConditionByEntity(searchSysDefaultCustomForm));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSysDefaultCustomForm searchSysDefaultCustomForm){
    List<SysDefaultCustomFormDto> list = sysDefaultCustomFormService.findList(ControllerUtil.dynamicConditionByEntity(searchSysDefaultCustomForm));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SysDefaultCustomForm信息", SysDefaultCustomFormDto.class, "SysDefaultCustomForm.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
