package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutProductionMaterialDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterial;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutProductionMaterial;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutProductionMaterialService;
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
 * Created by leifengzhi on 2021/01/18.
 */
@RestController
@Api(tags = "生产领料表（发料计划）控制器")
@RequestMapping("/wmsOutProductionMaterial")
@Validated
public class WmsOutProductionMaterialController {

    @Resource
    private WmsOutProductionMaterialService wmsOutProductionMaterialService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutProductionMaterial wmsOutProductionMaterial) {
        return ControllerUtil.returnCRUD(wmsOutProductionMaterialService.save(wmsOutProductionMaterial));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutProductionMaterialService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutProductionMaterial.update.class) WmsOutProductionMaterial wmsOutProductionMaterial) {
        return ControllerUtil.returnCRUD(wmsOutProductionMaterialService.update(wmsOutProductionMaterial));
    }

    @ApiOperation("根据工单和物料反写发料数量")
    @PostMapping("/updateByWorkOrderId")
    public ResponseEntity updateByWorkOrderId(@ApiParam(value = "对象，Id必传",required = true)@RequestBody WmsOutProductionMaterial wmsOutProductionMaterial) {
        return ControllerUtil.returnCRUD(wmsOutProductionMaterialService.updateByWorkOrderId(wmsOutProductionMaterial));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutProductionMaterial> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutProductionMaterial  wmsOutProductionMaterial = wmsOutProductionMaterialService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutProductionMaterial,StringUtils.isEmpty(wmsOutProductionMaterial)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutProductionMaterialDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutProductionMaterial searchWmsOutProductionMaterial) {
        Page<Object> page = PageHelper.startPage(searchWmsOutProductionMaterial.getStartPage(),searchWmsOutProductionMaterial.getPageSize());
        List<WmsOutProductionMaterialDto> list = wmsOutProductionMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutProductionMaterial));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsOutProductionMaterial>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutProductionMaterial searchWmsOutProductionMaterial) {
        Page<Object> page = PageHelper.startPage(searchWmsOutProductionMaterial.getStartPage(),searchWmsOutProductionMaterial.getPageSize());
        List<WmsOutProductionMaterial> list = wmsOutProductionMaterialService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsOutProductionMaterial));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutProductionMaterial searchWmsOutProductionMaterial){
    List<WmsOutProductionMaterialDto> list = wmsOutProductionMaterialService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutProductionMaterial));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsOutProductionMaterial信息", WmsOutProductionMaterialDto.class, "WmsOutProductionMaterial.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
