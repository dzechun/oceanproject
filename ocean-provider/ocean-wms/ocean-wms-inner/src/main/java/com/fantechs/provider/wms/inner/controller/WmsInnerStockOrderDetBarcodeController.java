package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDetBarcodeDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDetBarcode;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStockOrderDetBarcode;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerStockOrderDetBarcodeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/12/28.
 */
@RestController
@Api(tags = "盘点单条码明细控制器")
@RequestMapping("/wmsInnerStockOrderDetBarcode")
@Validated
public class WmsInnerStockOrderDetBarcodeController {

    @Resource
    private WmsInnerStockOrderDetBarcodeService wmsInnerStockOrderDetBarcodeService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerStockOrderDetBarcode wmsInnerStockOrderDetBarcode) {
        return ControllerUtil.returnCRUD(wmsInnerStockOrderDetBarcodeService.save(wmsInnerStockOrderDetBarcode));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerStockOrderDetBarcodeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerStockOrderDetBarcode.update.class) WmsInnerStockOrderDetBarcode wmsInnerStockOrderDetBarcode) {
        return ControllerUtil.returnCRUD(wmsInnerStockOrderDetBarcodeService.update(wmsInnerStockOrderDetBarcode));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerStockOrderDetBarcode> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerStockOrderDetBarcode  wmsInnerStockOrderDetBarcode = wmsInnerStockOrderDetBarcodeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerStockOrderDetBarcode,StringUtils.isEmpty(wmsInnerStockOrderDetBarcode)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerStockOrderDetBarcodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerStockOrderDetBarcode searchWmsInnerStockOrderDetBarcode) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerStockOrderDetBarcode.getStartPage(),searchWmsInnerStockOrderDetBarcode.getPageSize());
        List<WmsInnerStockOrderDetBarcodeDto> list = wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStockOrderDetBarcode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsInnerStockOrderDetBarcodeDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsInnerStockOrderDetBarcode searchWmsInnerStockOrderDetBarcode) {
        List<WmsInnerStockOrderDetBarcodeDto> list = wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStockOrderDetBarcode));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerStockOrderDetBarcode searchWmsInnerStockOrderDetBarcode){
    List<WmsInnerStockOrderDetBarcodeDto> list = wmsInnerStockOrderDetBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStockOrderDetBarcode));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInnerStockOrderDetBarcode信息", WmsInnerStockOrderDetBarcodeDto.class, "WmsInnerStockOrderDetBarcode.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

}
