package com.fantechs.security.controller;

import com.fantechs.common.base.entity.security.SysImportTemplate;
import com.fantechs.common.base.entity.security.search.SearchSysImportTemplate;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.security.service.SysImportTemplateService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/10/13.
 */
@RestController
@Api(tags = "导入模板文件信息")
@RequestMapping("/sysImportTemplate")
@Validated
public class SysImportTemplateController {

    @Resource
    private SysImportTemplateService sysImportTemplateService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SysImportTemplate sysImportTemplate) {
        return ControllerUtil.returnCRUD(sysImportTemplateService.save(sysImportTemplate));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(sysImportTemplateService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SysImportTemplate.update.class) SysImportTemplate sysImportTemplate) {
        return ControllerUtil.returnCRUD(sysImportTemplateService.update(sysImportTemplate));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SysImportTemplate> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SysImportTemplate  sysImportTemplate = sysImportTemplateService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(sysImportTemplate,StringUtils.isEmpty(sysImportTemplate)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SysImportTemplate>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSysImportTemplate searchSysImportTemplate) {
        Page<Object> page = PageHelper.startPage(searchSysImportTemplate.getStartPage(),searchSysImportTemplate.getPageSize());
        List<SysImportTemplate> list = sysImportTemplateService.findList(ControllerUtil.dynamicConditionByEntity(searchSysImportTemplate));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<SysImportTemplate>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchSysImportTemplate searchSysImportTemplate) {
        List<SysImportTemplate> list = sysImportTemplateService.findList(ControllerUtil.dynamicConditionByEntity(searchSysImportTemplate));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSysImportTemplate searchSysImportTemplate){
    List<SysImportTemplate> list = sysImportTemplateService.findList(ControllerUtil.dynamicConditionByEntity(searchSysImportTemplate));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "导入模板文件信息", SysImportTemplate.class, "导入模板文件信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
