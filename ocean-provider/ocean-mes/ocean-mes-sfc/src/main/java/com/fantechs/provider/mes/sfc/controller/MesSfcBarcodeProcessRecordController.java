package com.fantechs.provider.mes.sfc.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.MesSfcBarcodeProcessRecordDto;
import com.fantechs.common.base.general.dto.mes.sfc.Search.SearchMesSfcBarcodeProcessRecord;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcBarcodeProcessRecord;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;

import com.fantechs.provider.mes.sfc.service.MesSfcBarcodeProcessRecordService;
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
 * 生产管理-产品条码过站记录表
 * mes_sfc_barcode_process_record
 * @author hyc
 * @date 2021-04-09 15:29:27
 */
@RestController
@Api(tags = "生产管理-产品条码过站记录表控制器")
@RequestMapping("/mesSfcBarcodeProcessRecord")
@Validated
public class MesSfcBarcodeProcessRecordController {

    @Resource
    private MesSfcBarcodeProcessRecordService mesSfcBarcodeProcessRecordService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated MesSfcBarcodeProcessRecord mesSfcBarcodeProcessRecord) {
        return ControllerUtil.returnCRUD(mesSfcBarcodeProcessRecordService.save(mesSfcBarcodeProcessRecord));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(mesSfcBarcodeProcessRecordService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=MesSfcBarcodeProcessRecord.update.class) MesSfcBarcodeProcessRecord mesSfcBarcodeProcessRecord) {
        return ControllerUtil.returnCRUD(mesSfcBarcodeProcessRecordService.update(mesSfcBarcodeProcessRecord));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<MesSfcBarcodeProcessRecord> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        MesSfcBarcodeProcessRecord  mesSfcBarcodeProcessRecord = mesSfcBarcodeProcessRecordService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(mesSfcBarcodeProcessRecord,StringUtils.isEmpty(mesSfcBarcodeProcessRecord)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<MesSfcBarcodeProcessRecordDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcBarcodeProcessRecord searchMesSfcBarcodeProcessRecord) {
        Page<Object> page = PageHelper.startPage(searchMesSfcBarcodeProcessRecord.getStartPage(),searchMesSfcBarcodeProcessRecord.getPageSize());
        List<MesSfcBarcodeProcessRecordDto> list = mesSfcBarcodeProcessRecordService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcessRecord));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<MesSfcBarcodeProcessRecord>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchMesSfcBarcodeProcessRecord searchMesSfcBarcodeProcessRecord) {
//        Page<Object> page = PageHelper.startPage(searchMesSfcBarcodeProcessRecord.getStartPage(),searchMesSfcBarcodeProcessRecord.getPageSize());
//        List<MesSfcBarcodeProcessRecord> list = mesSfcBarcodeProcessRecordService.findHtList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcessRecord));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchMesSfcBarcodeProcessRecord searchMesSfcBarcodeProcessRecord){
    List<MesSfcBarcodeProcessRecordDto> list = mesSfcBarcodeProcessRecordService.findList(ControllerUtil.dynamicConditionByEntity(searchMesSfcBarcodeProcessRecord));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "MesSfcBarcodeProcessRecord信息", MesSfcBarcodeProcessRecordDto.class, "MesSfcBarcodeProcessRecord.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
