package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerBarcodeOperationDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerBarcodeOperation;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerBarcodeOperation;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerBarcodeOperationService;
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
 * Created by leifengzhi on 2022/03/09.
 */
@RestController
@Api(tags = "条码替换报废控制器")
@RequestMapping("/wmsInnerBarcodeOperation")
@Validated
public class WmsInnerBarcodeOperationController {

    @Resource
    private WmsInnerBarcodeOperationService wmsInnerBarcodeOperationService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerBarcodeOperation wmsInnerBarcodeOperation) {
        return ControllerUtil.returnCRUD(wmsInnerBarcodeOperationService.save(wmsInnerBarcodeOperation));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerBarcodeOperationService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerBarcodeOperation.update.class) WmsInnerBarcodeOperation wmsInnerBarcodeOperation) {
        return ControllerUtil.returnCRUD(wmsInnerBarcodeOperationService.update(wmsInnerBarcodeOperation));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerBarcodeOperation> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerBarcodeOperation  wmsInnerBarcodeOperation = wmsInnerBarcodeOperationService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerBarcodeOperation,StringUtils.isEmpty(wmsInnerBarcodeOperation)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerBarcodeOperationDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerBarcodeOperation searchWmsInnerBarcodeOperation) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerBarcodeOperation.getStartPage(),searchWmsInnerBarcodeOperation.getPageSize());
        List<WmsInnerBarcodeOperationDto> list = wmsInnerBarcodeOperationService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerBarcodeOperation));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsInnerBarcodeOperationDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsInnerBarcodeOperation searchWmsInnerBarcodeOperation) {
        List<WmsInnerBarcodeOperationDto> list = wmsInnerBarcodeOperationService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerBarcodeOperation));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerBarcodeOperation searchWmsInnerBarcodeOperation){
    List<WmsInnerBarcodeOperationDto> list = wmsInnerBarcodeOperationService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerBarcodeOperation));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInnerBarcodeOperation信息", WmsInnerBarcodeOperationDto.class, "WmsInnerBarcodeOperation.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
