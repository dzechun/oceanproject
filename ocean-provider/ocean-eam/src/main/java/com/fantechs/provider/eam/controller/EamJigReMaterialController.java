package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigReMaterialDto;
import com.fantechs.common.base.general.entity.eam.EamJigReMaterial;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigReMaterial;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigReMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamHtJigReMaterialService;
import com.fantechs.provider.eam.service.EamJigReMaterialService;
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
 * Created by leifengzhi on 2021/07/28.
 */
@RestController
@Api(tags = "治具绑定产品")
@RequestMapping("/eamJigReMaterial")
@Validated
public class EamJigReMaterialController {

    @Resource
    private EamJigReMaterialService eamJigReMaterialService;
    @Resource
    private EamHtJigReMaterialService eamHtJigReMaterialService;

    @ApiOperation(value = "批量新增或修改",notes = "批量新增或修改")
    @PostMapping("/batchAddOrUpdate")
    public ResponseEntity batchAddOrUpdate(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<EamJigReMaterial> list) {
        return ControllerUtil.returnCRUD(eamJigReMaterialService.batchAddOrUpdate(list));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamJigReMaterial eamJigReMaterial) {
        return ControllerUtil.returnCRUD(eamJigReMaterialService.save(eamJigReMaterial));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamJigReMaterialService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamJigReMaterial.update.class) EamJigReMaterial eamJigReMaterial) {
        return ControllerUtil.returnCRUD(eamJigReMaterialService.update(eamJigReMaterial));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamJigReMaterial> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamJigReMaterial  eamJigReMaterial = eamJigReMaterialService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamJigReMaterial,StringUtils.isEmpty(eamJigReMaterial)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamJigReMaterialDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigReMaterial searchEamJigReMaterial) {
        Page<Object> page = PageHelper.startPage(searchEamJigReMaterial.getStartPage(),searchEamJigReMaterial.getPageSize());
        List<EamJigReMaterialDto> list = eamJigReMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigReMaterial));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtJigReMaterial>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigReMaterial searchEamJigReMaterial) {
        Page<Object> page = PageHelper.startPage(searchEamJigReMaterial.getStartPage(),searchEamJigReMaterial.getPageSize());
        List<EamHtJigReMaterial> list = eamHtJigReMaterialService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamJigReMaterial));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamJigReMaterial searchEamJigReMaterial){
    List<EamJigReMaterialDto> list = eamJigReMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigReMaterial));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "治具绑定产品", EamJigReMaterialDto.class, "治具绑定产品.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
