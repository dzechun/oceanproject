package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamMaintainProjectDto;
import com.fantechs.common.base.general.entity.eam.EamMaintainProject;
import com.fantechs.common.base.general.entity.eam.history.EamHtMaintainProject;
import com.fantechs.common.base.general.entity.eam.search.SearchEamMaintainProject;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamMaintainProjectService;
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
 * Created by leifengzhi on 2021/06/25.
 */
@RestController
@Api(tags = "保养项目")
@RequestMapping("/eamMaintainProject")
@Validated
public class EamMaintainProjectController {

    @Resource
    private EamMaintainProjectService eamMaintainProjectService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamMaintainProject eamMaintainProject) {
        return ControllerUtil.returnCRUD(eamMaintainProjectService.save(eamMaintainProject));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamMaintainProjectService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamMaintainProject.update.class) EamMaintainProject eamMaintainProject) {
        return ControllerUtil.returnCRUD(eamMaintainProjectService.update(eamMaintainProject));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamMaintainProject> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamMaintainProject  eamMaintainProject = eamMaintainProjectService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamMaintainProject,StringUtils.isEmpty(eamMaintainProject)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamMaintainProjectDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamMaintainProject searchEamMaintainProject) {
        Page<Object> page = PageHelper.startPage(searchEamMaintainProject.getStartPage(),searchEamMaintainProject.getPageSize());
        List<EamMaintainProjectDto> list = eamMaintainProjectService.findList(ControllerUtil.dynamicConditionByEntity(searchEamMaintainProject));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtMaintainProject>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamMaintainProject searchEamMaintainProject) {
        Page<Object> page = PageHelper.startPage(searchEamMaintainProject.getStartPage(),searchEamMaintainProject.getPageSize());
        List<EamHtMaintainProject> list = eamMaintainProjectService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamMaintainProject));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamMaintainProject searchEamMaintainProject){
    List<EamMaintainProjectDto> list = eamMaintainProjectService.findList(ControllerUtil.dynamicConditionByEntity(searchEamMaintainProject));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "保养项目", EamMaintainProjectDto.class, "保养项目.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
