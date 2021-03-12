package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryScrapDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventoryScrapDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventoryScrapDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryScrapDetService;
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
 * Created by leifengzhi on 2021/03/10.
 */
@RestController
@Api(tags = "wmsInnerInventoryScrapDet控制器")
@RequestMapping("/wmsInnerInventoryScrapDet")
@Validated
public class WmsInnerInventoryScrapDetController {

    @Resource
    private WmsInnerInventoryScrapDetService wmsInnerInventoryScrapDetService;

//    @ApiOperation(value = "新增",notes = "新增")
//    @PostMapping("/add")
//    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerInventoryScrapDet wmsInnerInventoryScrapDet) {
//        return ControllerUtil.returnCRUD(wmsInnerInventoryScrapDetService.save(wmsInnerInventoryScrapDet));
//    }
//
//    @ApiOperation("删除")
//    @PostMapping("/delete")
//    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
//        return ControllerUtil.returnCRUD(wmsInnerInventoryScrapDetService.batchDelete(ids));
//    }
//
//    @ApiOperation("修改")
//    @PostMapping("/update")
//    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerInventoryScrapDet.update.class) WmsInnerInventoryScrapDet wmsInnerInventoryScrapDet) {
//        return ControllerUtil.returnCRUD(wmsInnerInventoryScrapDetService.update(wmsInnerInventoryScrapDet));
//    }
//
//    @ApiOperation("获取详情")
//    @PostMapping("/detail")
//    public ResponseEntity<WmsInnerInventoryScrapDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
//        WmsInnerInventoryScrapDet  wmsInnerInventoryScrapDet = wmsInnerInventoryScrapDetService.selectByKey(id);
//        return  ControllerUtil.returnDataSuccess(wmsInnerInventoryScrapDet,StringUtils.isEmpty(wmsInnerInventoryScrapDet)?0:1);
//    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerInventoryScrapDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInventoryScrapDet searchWmsInnerInventoryScrapDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerInventoryScrapDet.getStartPage(),searchWmsInnerInventoryScrapDet.getPageSize());
        List<WmsInnerInventoryScrapDetDto> list = wmsInnerInventoryScrapDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventoryScrapDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<WmsInnerInventoryScrapDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInventoryScrapDet searchWmsInnerInventoryScrapDet) {
//        Page<Object> page = PageHelper.startPage(searchWmsInnerInventoryScrapDet.getStartPage(),searchWmsInnerInventoryScrapDet.getPageSize());
//        List<WmsInnerInventoryScrapDet> list = wmsInnerInventoryScrapDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventoryScrapDet));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }
//
//    @PostMapping(value = "/export")
//    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
//    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
//    @RequestBody(required = false) SearchWmsInnerInventoryScrapDet searchWmsInnerInventoryScrapDet){
//    List<WmsInnerInventoryScrapDetDto> list = wmsInnerInventoryScrapDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventoryScrapDet));
//    try {
//        // 导出操作
//        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInnerInventoryScrapDet信息", WmsInnerInventoryScrapDetDto.class, "WmsInnerInventoryScrapDet.xls", response);
//        } catch (Exception e) {
//        throw new BizErrorException(e);
//        }
//    }
}
