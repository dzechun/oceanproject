package com.fantechs.provider.guest.callagv.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.callagv.CallAgvWarehouseAreaBarcodeLogDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvWarehouseAreaBarcodeLog;
import com.fantechs.common.base.general.entity.callagv.search.SearchCallAgvWarehouseAreaBarcodeLog;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.callagv.service.CallAgvWarehouseAreaBarcodeLogService;
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
@Api(tags = "callAgvWarehouseAreaBarcodeLog控制器")
@RequestMapping("/callAgvWarehouseAreaBarcodeLog")
@Validated
public class CallAgvWarehouseAreaBarcodeLogController {

    @Resource
    private CallAgvWarehouseAreaBarcodeLogService callAgvWarehouseAreaBarcodeLogService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated CallAgvWarehouseAreaBarcodeLog callAgvWarehouseAreaBarcodeLog) {
        return ControllerUtil.returnCRUD(callAgvWarehouseAreaBarcodeLogService.save(callAgvWarehouseAreaBarcodeLog));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(callAgvWarehouseAreaBarcodeLogService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=CallAgvWarehouseAreaBarcodeLog.update.class) CallAgvWarehouseAreaBarcodeLog callAgvWarehouseAreaBarcodeLog) {
        return ControllerUtil.returnCRUD(callAgvWarehouseAreaBarcodeLogService.update(callAgvWarehouseAreaBarcodeLog));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<CallAgvWarehouseAreaBarcodeLog> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        CallAgvWarehouseAreaBarcodeLog  callAgvWarehouseAreaBarcodeLog = callAgvWarehouseAreaBarcodeLogService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(callAgvWarehouseAreaBarcodeLog,StringUtils.isEmpty(callAgvWarehouseAreaBarcodeLog)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<CallAgvWarehouseAreaBarcodeLogDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchCallAgvWarehouseAreaBarcodeLog searchCallAgvWarehouseAreaBarcodeLog) {
        Page<Object> page = PageHelper.startPage(searchCallAgvWarehouseAreaBarcodeLog.getStartPage(),searchCallAgvWarehouseAreaBarcodeLog.getPageSize());
        List<CallAgvWarehouseAreaBarcodeLogDto> list = callAgvWarehouseAreaBarcodeLogService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvWarehouseAreaBarcodeLog));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<CallAgvWarehouseAreaBarcodeLogDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchCallAgvWarehouseAreaBarcodeLog searchCallAgvWarehouseAreaBarcodeLog) {
        List<CallAgvWarehouseAreaBarcodeLogDto> list = callAgvWarehouseAreaBarcodeLogService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvWarehouseAreaBarcodeLog));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchCallAgvWarehouseAreaBarcodeLog searchCallAgvWarehouseAreaBarcodeLog){
    List<CallAgvWarehouseAreaBarcodeLogDto> list = callAgvWarehouseAreaBarcodeLogService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvWarehouseAreaBarcodeLog));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "CallAgvWarehouseAreaBarcodeLog信息", CallAgvWarehouseAreaBarcodeLogDto.class, "CallAgvWarehouseAreaBarcodeLog.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
