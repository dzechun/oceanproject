package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.general.dto.eam.EamEquipmentCategoryDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentCategory;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentCategory;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentCategory;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.eam.service.EamEquipmentCategoryService;
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
 * Created by leifengzhi on 2021/06/25.
 */
@RestController
@Api(tags = "设备类别")
@RequestMapping("/eamEquipmentCategory")
@Validated
public class EamEquipmentCategoryController {

    @Resource
    private EamEquipmentCategoryService eamEquipmentCategoryService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentCategory eamEquipmentCategory) {
        return ControllerUtil.returnCRUD(eamEquipmentCategoryService.save(eamEquipmentCategory));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentCategoryService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentCategory.update.class) EamEquipmentCategory eamEquipmentCategory) {
        return ControllerUtil.returnCRUD(eamEquipmentCategoryService.update(eamEquipmentCategory));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentCategory> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentCategory  eamEquipmentCategory = eamEquipmentCategoryService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentCategory,StringUtils.isEmpty(eamEquipmentCategory)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentCategoryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentCategory searchEamEquipmentCategory) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentCategory.getStartPage(),searchEamEquipmentCategory.getPageSize());
        List<EamEquipmentCategoryDto> list = eamEquipmentCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtEquipmentCategory>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentCategory searchEamEquipmentCategory) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentCategory.getStartPage(),searchEamEquipmentCategory.getPageSize());
        List<EamHtEquipmentCategory> list = eamEquipmentCategoryService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentCategory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquipmentCategory searchEamEquipmentCategory){
    List<EamEquipmentCategoryDto> list = eamEquipmentCategoryService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentCategory));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "设备类别", "设备类别.xls", response);
    }
}
