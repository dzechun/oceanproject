package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigMaintainProjectDto;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainProject;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainProject;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigMaintainProject;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamHtJigMaintainProjectService;
import com.fantechs.provider.eam.service.EamJigMaintainProjectService;
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
 * Created by leifengzhi on 2021/08/12.
 */
@RestController
@Api(tags = "治具保养项目")
@RequestMapping("/eamJigMaintainProject")
@Validated
public class EamJigMaintainProjectController {

    @Resource
    private EamJigMaintainProjectService eamJigMaintainProjectService;
    @Resource
    private EamHtJigMaintainProjectService eamHtJigMaintainProjectService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamJigMaintainProject eamJigMaintainProject) {
        return ControllerUtil.returnCRUD(eamJigMaintainProjectService.save(eamJigMaintainProject));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamJigMaintainProjectService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamJigMaintainProject.update.class) EamJigMaintainProject eamJigMaintainProject) {
        return ControllerUtil.returnCRUD(eamJigMaintainProjectService.update(eamJigMaintainProject));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamJigMaintainProject> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamJigMaintainProject  eamJigMaintainProject = eamJigMaintainProjectService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamJigMaintainProject,StringUtils.isEmpty(eamJigMaintainProject)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamJigMaintainProjectDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigMaintainProject searchEamJigMaintainProject) {
        Page<Object> page = PageHelper.startPage(searchEamJigMaintainProject.getStartPage(),searchEamJigMaintainProject.getPageSize());
        List<EamJigMaintainProjectDto> list = eamJigMaintainProjectService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaintainProject));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtJigMaintainProject>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigMaintainProject searchEamJigMaintainProject) {
        Page<Object> page = PageHelper.startPage(searchEamJigMaintainProject.getStartPage(),searchEamJigMaintainProject.getPageSize());
        List<EamHtJigMaintainProject> list = eamHtJigMaintainProjectService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaintainProject));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamJigMaintainProject searchEamJigMaintainProject){
    List<EamJigMaintainProjectDto> list = eamJigMaintainProjectService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaintainProject));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "治具保养项目", EamJigMaintainProjectDto.class, "治具保养项目.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}