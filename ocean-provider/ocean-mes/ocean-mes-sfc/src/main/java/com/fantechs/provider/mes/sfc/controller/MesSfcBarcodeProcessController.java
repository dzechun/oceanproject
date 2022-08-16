package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcess;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcess;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessService;
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
 * 生产管理-产品条码过站表
 * mes_sfc_barcode_process
 * @author hyc
 * @date 2021-04-09 15:29:27
 */
@RestController
@Api(tags = "生产管理-产品条码过站表控制器")
@RequestMapping("/mesSfcBarcodeProcess")
@Validated
public class MesSfcBarcodeProcessController {

    @Resource
    private MesSfcBarcodeProcessService mesSfcBarcodeProcessService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesSfcBarcodeProcess mesSfcBarcodeProcess) {
        return ControllerUtil.returnCRUD(mesSfcBarcodeProcessService.save(mesSfcBarcodeProcess));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesSfcBarcodeProcessService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesSfcBarcodeProcess.update.class) MesSfcBarcodeProcess mesSfcBarcodeProcess) {
        return ControllerUtil.returnCRUD(mesSfcBarcodeProcessService.update(mesSfcBarcodeProcess));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesSfcBarcodeProcess> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesSfcBarcodeProcess  mesSfcBarcodeProcess = mesSfcBarcodeProcessService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesSfcBarcodeProcess,StringUtils.isEmpty(mesSfcBarcodeProcess)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesSfcBarcodeProcessDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess) {
        Page<Object> page = PageHelper.startPage(searchMesSfcBarcodeProcess.getStartPage(),searchMesSfcBarcodeProcess.getPageSize());
        List<MesSfcBarcodeProcessDto> list = mesSfcBarcodeProcessService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcess));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("查询条码")
    @PostMapping("/findBarcode")
    public ResponseEntity<List<MesSfcBarcodeProcess>> findBarcode(@RequestBody  SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess){
        List<MesSfcBarcodeProcess> list = mesSfcBarcodeProcessService.findBarcode(searchMesSfcBarcodeProcess);
        return ControllerUtil.returnDataSuccess(list,StringUtils.isEmpty(list)?0:1);
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<MesSfcBarcodeProcess>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess) {
//        Page<Object> page = PageHelper.startPage(searchMesSfcBarcodeProcess.getStartPage(),searchMesSfcBarcodeProcess.getPageSize());
//        List<MesSfcBarcodeProcess> list = mesSfcBarcodeProcessService.findHtList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcess));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesSfcBarcodeProcess searchMesSfcBarcodeProcess){
    List<MesSfcBarcodeProcessDto> list = mesSfcBarcodeProcessService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcess));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesSfcBarcodeProcess信息", MesSfcBarcodeProcessDto.class, "MesSfcBarcodeProcess.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
