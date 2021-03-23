package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStocktakingDetDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerHtStocktakingDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStocktakingDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStocktakingDet;
import com.fantechs.provider.wms.inner.service.WmsInnerHtStocktakingDetService;
import com.fantechs.provider.wms.inner.service.WmsInnerStocktakingDetService;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/03/22.
 */
@RestController
@Api(tags = "盘点单明细管理")
@RequestMapping("/wmsInnerStocktakingDet")
@Validated
public class WmsInnerStocktakingDetController {

    @Resource
    private WmsInnerStocktakingDetService wmsInnerStocktakingDetService;
    @Resource
    private WmsInnerHtStocktakingDetService wmsInnerHtStocktakingDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerStocktakingDet wmsInnerStocktakingDet) {
        return ControllerUtil.returnCRUD(wmsInnerStocktakingDetService.save(wmsInnerStocktakingDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerStocktakingDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerStocktakingDet.update.class) WmsInnerStocktakingDet wmsInnerStocktakingDet) {
        return ControllerUtil.returnCRUD(wmsInnerStocktakingDetService.update(wmsInnerStocktakingDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerStocktakingDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerStocktakingDet  wmsInnerStocktakingDet = wmsInnerStocktakingDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerStocktakingDet,StringUtils.isEmpty(wmsInnerStocktakingDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerStocktakingDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerStocktakingDet searchWmsInnerStocktakingDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerStocktakingDet.getStartPage(),searchWmsInnerStocktakingDet.getPageSize());
        List<WmsInnerStocktakingDetDto> list = wmsInnerStocktakingDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStocktakingDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInnerHtStocktakingDet>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerStocktakingDet searchWmsInnerStocktakingDet) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerStocktakingDet.getStartPage(),searchWmsInnerStocktakingDet.getPageSize());
        List<WmsInnerHtStocktakingDet> list = wmsInnerHtStocktakingDetService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStocktakingDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerStocktakingDet searchWmsInnerStocktakingDet){
    List<WmsInnerStocktakingDetDto> list = wmsInnerStocktakingDetService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStocktakingDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInnerStocktakingDet信息", WmsInnerStocktakingDetDto.class, "WmsInnerStocktakingDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
