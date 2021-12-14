package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeReOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcodeReOrder;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtMaterialBarcodeReOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerMaterialBarcodeReOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerHtMaterialBarcodeReOrderService;
import com.fantechs.provider.wms.inner.service.WmsInnerMaterialBarcodeReOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
 * Created by leifengzhi on 2021/12/14.
 */
@RestController
@Api(tags = "wmsInnerMaterialBarcodeReOrder控制器")
@RequestMapping("/wmsInnerMaterialBarcodeReOrder")
@Validated
public class WmsInnerMaterialBarcodeReOrderController {

    @Resource
    private WmsInnerMaterialBarcodeReOrderService wmsInnerMaterialBarcodeReOrderService;
    @Resource
    private WmsInnerHtMaterialBarcodeReOrderService wmsInnerHtMaterialBarcodeReOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder) {
        return ControllerUtil.returnCRUD(wmsInnerMaterialBarcodeReOrderService.save(wmsInnerMaterialBarcodeReOrder));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<WmsInnerMaterialBarcodeReOrder> list) {
        return ControllerUtil.returnCRUD(wmsInnerMaterialBarcodeReOrderService.batchAdd(list));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerMaterialBarcodeReOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerMaterialBarcodeReOrder.update.class) WmsInnerMaterialBarcodeReOrder wmsInnerMaterialBarcodeReOrder) {
        return ControllerUtil.returnCRUD(wmsInnerMaterialBarcodeReOrderService.update(wmsInnerMaterialBarcodeReOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerMaterialBarcodeReOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerMaterialBarcodeReOrder  wmsInnerMaterialBarcodeReOrder = wmsInnerMaterialBarcodeReOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerMaterialBarcodeReOrder,StringUtils.isEmpty(wmsInnerMaterialBarcodeReOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerMaterialBarcodeReOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerMaterialBarcodeReOrder searchWmsInnerMaterialBarcodeReOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerMaterialBarcodeReOrder.getStartPage(),searchWmsInnerMaterialBarcodeReOrder.getPageSize());
        List<WmsInnerMaterialBarcodeReOrderDto> list = wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerMaterialBarcodeReOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsInnerMaterialBarcodeReOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsInnerMaterialBarcodeReOrder searchWmsInnerMaterialBarcodeReOrder) {
        List<WmsInnerMaterialBarcodeReOrderDto> list = wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerMaterialBarcodeReOrder));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsInnerHtMaterialBarcodeReOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerMaterialBarcodeReOrder searchWmsInnerMaterialBarcodeReOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerMaterialBarcodeReOrder.getStartPage(),searchWmsInnerMaterialBarcodeReOrder.getPageSize());
        List<WmsInnerHtMaterialBarcodeReOrder> list = wmsInnerHtMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerMaterialBarcodeReOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerMaterialBarcodeReOrder searchWmsInnerMaterialBarcodeReOrder){
    List<WmsInnerMaterialBarcodeReOrderDto> list = wmsInnerMaterialBarcodeReOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerMaterialBarcodeReOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInnerMaterialBarcodeReOrder信息", WmsInnerMaterialBarcodeReOrderDto.class, "WmsInnerMaterialBarcodeReOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

}
