package com.fantechs.provider.guest.callagv.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.callagv.CallAgvWarehouseAreaReBarcodeDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvWarehouseAreaReBarcode;
import com.fantechs.common.base.general.entity.callagv.search.SearchCallAgvWarehouseAreaReBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.callagv.service.CallAgvWarehouseAreaReBarcodeService;
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

@RestController
@Api(tags = "callAgvWarehouseAreaReBarcode控制器")
@RequestMapping("/callAgvWarehouseAreaReBarcode")
@Validated
public class CallAgvWarehouseAreaReBarcodeController {

    @Resource
    private CallAgvWarehouseAreaReBarcodeService callAgvWarehouseAreaReBarcodeService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated CallAgvWarehouseAreaReBarcode callAgvWarehouseAreaReBarcode) {
        return ControllerUtil.returnCRUD(callAgvWarehouseAreaReBarcodeService.save(callAgvWarehouseAreaReBarcode));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(callAgvWarehouseAreaReBarcodeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=CallAgvWarehouseAreaReBarcode.update.class) CallAgvWarehouseAreaReBarcode callAgvWarehouseAreaReBarcode) {
        return ControllerUtil.returnCRUD(callAgvWarehouseAreaReBarcodeService.update(callAgvWarehouseAreaReBarcode));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<CallAgvWarehouseAreaReBarcode> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        CallAgvWarehouseAreaReBarcode  callAgvWarehouseAreaReBarcode = callAgvWarehouseAreaReBarcodeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(callAgvWarehouseAreaReBarcode,StringUtils.isEmpty(callAgvWarehouseAreaReBarcode)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<CallAgvWarehouseAreaReBarcodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchCallAgvWarehouseAreaReBarcode searchCallAgvWarehouseAreaReBarcode) {
        Page<Object> page = PageHelper.startPage(searchCallAgvWarehouseAreaReBarcode.getStartPage(),searchCallAgvWarehouseAreaReBarcode.getPageSize());
        List<CallAgvWarehouseAreaReBarcodeDto> list = callAgvWarehouseAreaReBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvWarehouseAreaReBarcode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<CallAgvWarehouseAreaReBarcodeDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchCallAgvWarehouseAreaReBarcode searchCallAgvWarehouseAreaReBarcode) {
        List<CallAgvWarehouseAreaReBarcodeDto> list = callAgvWarehouseAreaReBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvWarehouseAreaReBarcode));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchCallAgvWarehouseAreaReBarcode searchCallAgvWarehouseAreaReBarcode){
    List<CallAgvWarehouseAreaReBarcodeDto> list = callAgvWarehouseAreaReBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvWarehouseAreaReBarcode));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "CallAgvWarehouseAreaReBarcode信息", CallAgvWarehouseAreaReBarcodeDto.class, "CallAgvWarehouseAreaReBarcode.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
