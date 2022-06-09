package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.general.dto.eam.EamJigMaterialDto;
import com.fantechs.common.base.general.entity.eam.EamJigMaterial;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaterial;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.BeanUtils;
import com.fantechs.common.base.utils.CustomFormUtils;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import com.fantechs.provider.eam.service.EamHtJigMaterialService;
import com.fantechs.provider.eam.service.EamJigMaterialService;
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
@Api(tags = "治具绑定产品")
@RequestMapping("/eamJigMaterial")
@Validated
public class EamJigMaterialController {

    @Resource
    private EamJigMaterialService eamJigMaterialService;
    @Resource
    private EamHtJigMaterialService eamHtJigMaterialService;
    @Resource
    private AuthFeignApi securityFeignApi;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamJigMaterialDto eamJigMaterialDto) {
        return ControllerUtil.returnCRUD(eamJigMaterialService.save(eamJigMaterialDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamJigMaterialService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamJigMaterial.update.class) EamJigMaterialDto eamJigMaterialDto) {
        return ControllerUtil.returnCRUD(eamJigMaterialService.update(eamJigMaterialDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamJigMaterial> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamJigMaterial  eamJigMaterial = eamJigMaterialService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamJigMaterial,StringUtils.isEmpty(eamJigMaterial)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamJigMaterialDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigMaterial searchEamJigMaterial) {
        Page<Object> page = PageHelper.startPage(searchEamJigMaterial.getStartPage(),searchEamJigMaterial.getPageSize());
        List<EamJigMaterialDto> list = eamJigMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaterial));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtJigMaterial>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigMaterial searchEamJigMaterial) {
        Page<Object> page = PageHelper.startPage(searchEamJigMaterial.getStartPage(),searchEamJigMaterial.getPageSize());
        List<EamHtJigMaterial> list = eamHtJigMaterialService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaterial));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamJigMaterial searchEamJigMaterial){
    List<EamJigMaterialDto> list = eamJigMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaterial));
        // 获取自定义导出参数列表
        List<Map<String, Object>> customExportParamList = BeanUtils.objectListToMapList(securityFeignApi.findCustomExportParamList(CustomFormUtils.getFromRout()).getData());
        // 自定义导出操作
        EasyPoiUtils.customExportExcel(list, customExportParamList, "导出信息", "治具绑定产品", "治具绑定产品.xls", response);
    }
}
