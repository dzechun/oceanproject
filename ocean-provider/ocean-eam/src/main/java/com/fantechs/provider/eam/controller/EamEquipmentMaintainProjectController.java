package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainProjectDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainProject;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentMaintainProject;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamEquipmentMaintainProjectService;
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
 * Created by leifengzhi on 2021/08/21.
 */
@RestController
@Api(tags = "设备保养项目")
@RequestMapping("/eamEquipmentMaintainProject")
@Validated
public class EamEquipmentMaintainProjectController {

    @Resource
    private EamEquipmentMaintainProjectService eamEquipmentMaintainProjectService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentMaintainProject eamEquipmentMaintainProject) {
        return ControllerUtil.returnCRUD(eamEquipmentMaintainProjectService.save(eamEquipmentMaintainProject));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentMaintainProjectService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentMaintainProject.update.class) EamEquipmentMaintainProject eamEquipmentMaintainProject) {
        return ControllerUtil.returnCRUD(eamEquipmentMaintainProjectService.update(eamEquipmentMaintainProject));
    }

    @ApiOperation("修改状态")
    @PostMapping("/updateStatus")
    public ResponseEntity updateStatus(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentMaintainProject.update.class) EamEquipmentMaintainProject eamEquipmentMaintainProject) {
        return ControllerUtil.returnCRUD(eamEquipmentMaintainProjectService.updateStatus(eamEquipmentMaintainProject));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentMaintainProject> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentMaintainProject  eamEquipmentMaintainProject = eamEquipmentMaintainProjectService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentMaintainProject,StringUtils.isEmpty(eamEquipmentMaintainProject)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentMaintainProjectDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentMaintainProject searchEamEquipmentMaintainProject) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentMaintainProject.getStartPage(),searchEamEquipmentMaintainProject.getPageSize());
        List<EamEquipmentMaintainProjectDto> list = eamEquipmentMaintainProjectService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaintainProject));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamEquipmentMaintainProjectDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentMaintainProject searchEamEquipmentMaintainProject) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentMaintainProject.getStartPage(),searchEamEquipmentMaintainProject.getPageSize());
        List<EamEquipmentMaintainProjectDto> list = eamEquipmentMaintainProjectService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaintainProject));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquipmentMaintainProject searchEamEquipmentMaintainProject){
    List<EamEquipmentMaintainProjectDto> list = eamEquipmentMaintainProjectService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaintainProject));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "设备保养项目", EamEquipmentMaintainProjectDto.class, "设备保养项目.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
