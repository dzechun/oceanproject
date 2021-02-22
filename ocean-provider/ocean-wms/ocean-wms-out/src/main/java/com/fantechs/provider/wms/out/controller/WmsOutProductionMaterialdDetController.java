package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.general.entity.wms.out.WmsOutProductionMaterialdDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.provider.wms.out.service.WmsOutProductionMaterialdDetService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * Created by leifengzhi on 2021/02/04.
 */
@RestController
@Api(tags = "生产领料明细表（发料计划）控制器")
@RequestMapping("/wmsOutProductionMaterialdDet")
@Validated
public class WmsOutProductionMaterialdDetController {

    @Resource
    private WmsOutProductionMaterialdDetService wmsOutProductionMaterialdDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutProductionMaterialdDet wmsOutProductionMaterialdDet) {
        return ControllerUtil.returnCRUD(wmsOutProductionMaterialdDetService.save(wmsOutProductionMaterialdDet));
    }

//    @ApiOperation("删除")
//    @PostMapping("/delete")
//    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
//        return ControllerUtil.returnCRUD(wmsOutProductionMaterialdDetService.batchDelete(ids));
//    }
//
//    @ApiOperation("修改")
//    @PostMapping("/update")
//    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutProductionMaterialdDet.update.class) WmsOutProductionMaterialdDet wmsOutProductionMaterialdDet) {
//        return ControllerUtil.returnCRUD(wmsOutProductionMaterialdDetService.update(wmsOutProductionMaterialdDet));
//    }
//
//    @ApiOperation("获取详情")
//    @PostMapping("/detail")
//    public ResponseEntity<WmsOutProductionMaterialdDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
//        WmsOutProductionMaterialdDet  wmsOutProductionMaterialdDet = wmsOutProductionMaterialdDetService.selectByKey(id);
//        return  ControllerUtil.returnDataSuccess(wmsOutProductionMaterialdDet,StringUtils.isEmpty(wmsOutProductionMaterialdDet)?0:1);
//    }

//    @ApiOperation("列表")
//    @PostMapping("/findList")
//    public ResponseEntity<List<WmsOutProductionMaterialdDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutProductionMaterialdDet searchWmsOutProductionMaterialdDet) {
//        Page<Object> page = PageHelper.startPage(searchWmsOutProductionMaterialdDet.getStartPage(),searchWmsOutProductionMaterialdDet.getPageSize());
//        List<WmsOutProductionMaterialdDetDto> list = wmsOutProductionMaterialdDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutProductionMaterialdDet));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }
//
//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<WmsOutProductionMaterialdDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutProductionMaterialdDet searchWmsOutProductionMaterialdDet) {
//        Page<Object> page = PageHelper.startPage(searchWmsOutProductionMaterialdDet.getStartPage(),searchWmsOutProductionMaterialdDet.getPageSize());
//        List<WmsOutProductionMaterialdDet> list = wmsOutProductionMaterialdDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsOutProductionMaterialdDet));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }
//
//    @PostMapping(value = "/export")
//    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
//    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
//    @RequestBody(required = false) SearchWmsOutProductionMaterialdDet searchWmsOutProductionMaterialdDet){
//    List<WmsOutProductionMaterialdDetDto> list = wmsOutProductionMaterialdDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutProductionMaterialdDet));
//    try {
//        // 导出操作
//        EasyPoiUtils.exportExcel(list, "导出信息", "WmsOutProductionMaterialdDet信息", WmsOutProductionMaterialdDetDto.class, "WmsOutProductionMaterialdDet.xls", response);
//        } catch (Exception e) {
//        throw new BizErrorException(e);
//        }
//    }
}
