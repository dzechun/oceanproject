package com.fantechs.provider.guest.callagv.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.general.dto.callagv.CallAgvBarcodeDto;
import com.fantechs.common.base.general.entity.callagv.CallAgvBarcode;
import com.fantechs.common.base.general.entity.callagv.search.SearchCallAgvBarcode;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.guest.callagv.service.CallAgvBarcodeService;
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

@RestController
@Api(tags = "木林森条码控制器")
@RequestMapping("/callAgvBarcode")
@Validated
public class CallAgvBarcodeController {

    @Resource
    private CallAgvBarcodeService callAgvBarcodeService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated CallAgvBarcode callAgvBarcode) {
        return ControllerUtil.returnCRUD(callAgvBarcodeService.save(callAgvBarcode));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(callAgvBarcodeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=CallAgvBarcode.update.class) CallAgvBarcode callAgvBarcode) {
        return ControllerUtil.returnCRUD(callAgvBarcodeService.update(callAgvBarcode));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<CallAgvBarcode> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        CallAgvBarcode  callAgvBarcode = callAgvBarcodeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(callAgvBarcode,StringUtils.isEmpty(callAgvBarcode)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<CallAgvBarcodeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchCallAgvBarcode searchCallAgvBarcode) {
        Page<Object> page = PageHelper.startPage(searchCallAgvBarcode.getStartPage(),searchCallAgvBarcode.getPageSize());
        List<CallAgvBarcodeDto> list = callAgvBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvBarcode));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchCallAgvBarcode searchCallAgvBarcode){
    List<CallAgvBarcodeDto> list = callAgvBarcodeService.findList(ControllerUtil.dynamicConditionByEntity(searchCallAgvBarcode));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "CallAgvBarcode信息", CallAgvBarcodeDto.class, "CallAgvBarcode.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
