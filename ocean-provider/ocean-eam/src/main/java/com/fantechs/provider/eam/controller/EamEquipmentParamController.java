package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.general.dto.eam.EamEquipmentParamDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentParam;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentParam;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentParam;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.eam.service.EamEquipmentParamService;
import com.fantechs.provider.eam.service.EamHtEquipmentParamService;
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
@Api(tags = "设备参数定义")
@RequestMapping("/eamEquipmentParam")
@Validated
public class EamEquipmentParamController {

    @Resource
    private EamEquipmentParamService eamEquipmentParamService;
    @Resource
    private EamHtEquipmentParamService eamHtEquipmentParamService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentParam eamEquipmentParam) {
        return ControllerUtil.returnCRUD(eamEquipmentParamService.save(eamEquipmentParam));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentParamService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentParam.update.class) EamEquipmentParam eamEquipmentParam) {
        return ControllerUtil.returnCRUD(eamEquipmentParamService.update(eamEquipmentParam));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentParam> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentParam  eamEquipmentParam = eamEquipmentParamService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentParam,StringUtils.isEmpty(eamEquipmentParam)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentParamDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentParam searchEamEquipmentParam) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentParam.getStartPage(),searchEamEquipmentParam.getPageSize());
        List<EamEquipmentParamDto> list = eamEquipmentParamService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentParam));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtEquipmentParam>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentParam searchEamEquipmentParam) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentParam.getStartPage(),searchEamEquipmentParam.getPageSize());
        List<EamHtEquipmentParam> list = eamHtEquipmentParamService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentParam));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquipmentParam searchEamEquipmentParam){
    List<EamEquipmentParamDto> list = eamEquipmentParamService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentParam));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "设备参数定义", "设备参数定义.xls", response);
    }
}
