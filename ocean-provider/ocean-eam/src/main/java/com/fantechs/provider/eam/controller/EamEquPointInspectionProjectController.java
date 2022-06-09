package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionProjectDto;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionProject;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquPointInspectionProject;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.eam.service.EamEquPointInspectionProjectService;
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
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/20.
 */
@RestController
@Api(tags = "设备点检项目")
@RequestMapping("/eamEquPointInspectionProject")
@Validated
public class EamEquPointInspectionProjectController {

    @Resource
    private EamEquPointInspectionProjectService eamEquPointInspectionProjectService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquPointInspectionProject eamEquPointInspectionProject) {
        return ControllerUtil.returnCRUD(eamEquPointInspectionProjectService.save(eamEquPointInspectionProject));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquPointInspectionProjectService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquPointInspectionProject.update.class) EamEquPointInspectionProject eamEquPointInspectionProject) {
        return ControllerUtil.returnCRUD(eamEquPointInspectionProjectService.update(eamEquPointInspectionProject));
    }

    @ApiOperation("修改状态")
    @PostMapping("/updateStatus")
    public ResponseEntity updateStatus(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= EamEquPointInspectionProject.update.class) EamEquPointInspectionProject eamEquPointInspectionProject) {
        return ControllerUtil.returnCRUD(eamEquPointInspectionProjectService.updateStatus(eamEquPointInspectionProject));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquPointInspectionProject> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquPointInspectionProject  eamEquPointInspectionProject = eamEquPointInspectionProjectService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquPointInspectionProject,StringUtils.isEmpty(eamEquPointInspectionProject)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquPointInspectionProjectDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquPointInspectionProject searchEamEquPointInspectionProject) {
        Page<Object> page = PageHelper.startPage(searchEamEquPointInspectionProject.getStartPage(),searchEamEquPointInspectionProject.getPageSize());
        List<EamEquPointInspectionProjectDto> list = eamEquPointInspectionProjectService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquPointInspectionProject));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamEquPointInspectionProjectDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquPointInspectionProject searchEamEquPointInspectionProject) {
        Page<Object> page = PageHelper.startPage(searchEamEquPointInspectionProject.getStartPage(),searchEamEquPointInspectionProject.getPageSize());
        List<EamEquPointInspectionProjectDto> list = eamEquPointInspectionProjectService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquPointInspectionProject));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquPointInspectionProject searchEamEquPointInspectionProject){
    List<EamEquPointInspectionProjectDto> list = eamEquPointInspectionProjectService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquPointInspectionProject));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "设备点检项目", "设备点检项目.xls", response);
    }
}
