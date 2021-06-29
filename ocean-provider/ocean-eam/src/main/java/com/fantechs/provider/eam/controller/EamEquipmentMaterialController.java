package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaterialDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaterial;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentMaterial;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamEquipmentMaterialService;
import com.fantechs.provider.eam.service.EamHtEquipmentMaterialService;
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
 * Created by leifengzhi on 2021/06/28.
 */
@RestController
@Api(tags = "设备绑定产品")
@RequestMapping("/eamEquipmentMaterial")
@Validated
public class EamEquipmentMaterialController {

    @Resource
    private EamEquipmentMaterialService eamEquipmentMaterialService;
    @Resource
    private EamHtEquipmentMaterialService eamHtEquipmentMaterialService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentMaterial eamEquipmentMaterial) {
        return ControllerUtil.returnCRUD(eamEquipmentMaterialService.save(eamEquipmentMaterial));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentMaterialService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentMaterial.update.class) EamEquipmentMaterial eamEquipmentMaterial) {
        return ControllerUtil.returnCRUD(eamEquipmentMaterialService.update(eamEquipmentMaterial));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentMaterial> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentMaterial  eamEquipmentMaterial = eamEquipmentMaterialService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentMaterial,StringUtils.isEmpty(eamEquipmentMaterial)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentMaterialDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentMaterial searchEamEquipmentMaterial) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentMaterial.getStartPage(),searchEamEquipmentMaterial.getPageSize());
        List<EamEquipmentMaterialDto> list = eamEquipmentMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaterial));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtEquipmentMaterial>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentMaterial searchEamEquipmentMaterial) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentMaterial.getStartPage(),searchEamEquipmentMaterial.getPageSize());
        List<EamHtEquipmentMaterial> list = eamHtEquipmentMaterialService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaterial));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquipmentMaterial searchEamEquipmentMaterial){
    List<EamEquipmentMaterialDto> list = eamEquipmentMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaterial));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "设备绑定产品", EamEquipmentMaterialDto.class, "设备绑定产品.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
