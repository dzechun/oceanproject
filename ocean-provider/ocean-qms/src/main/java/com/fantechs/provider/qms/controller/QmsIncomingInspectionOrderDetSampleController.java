package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.PdaIncomingCheckBarcodeDto;
import com.fantechs.common.base.general.dto.qms.PdaIncomingSampleSubmitDto;
import com.fantechs.common.base.general.dto.qms.QmsIncomingInspectionOrderDetSampleDto;
import com.fantechs.common.base.general.entity.qms.QmsIncomingInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.history.QmsHtIncomingInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsIncomingInspectionOrderDetSample;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsIncomingInspectionOrderDetSampleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/12/06.
 */
@RestController
@Api(tags = "来料检验单明细样本值")
@RequestMapping("/qmsIncomingInspectionOrderDetSample")
@Validated
@Slf4j
public class QmsIncomingInspectionOrderDetSampleController {

    @Resource
    private QmsIncomingInspectionOrderDetSampleService qmsIncomingInspectionOrderDetSampleService;

    @ApiOperation(value = "批量保存",notes = "批量保存")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty List<QmsIncomingInspectionOrderDetSample> qmsIncomingInspectionOrderDetSampleList) {
        return ControllerUtil.returnCRUD(qmsIncomingInspectionOrderDetSampleService.batchAdd(qmsIncomingInspectionOrderDetSampleList));
    }

    @ApiOperation("PDA条码校验")
    @PostMapping("/checkBarcode")
    public ResponseEntity<String> checkBarcode(@ApiParam(value = "查询对象")@RequestBody @Validated PdaIncomingCheckBarcodeDto pdaIncomingCheckBarcodeDto) {
        String barcode = qmsIncomingInspectionOrderDetSampleService.checkBarcode(pdaIncomingCheckBarcodeDto);
        return  ControllerUtil.returnDataSuccess(barcode,StringUtils.isEmpty(barcode)?0:1);
    }

    @ApiOperation(value = "PDA样本值提交",notes = "PDA样本值提交")
    @PostMapping("/sampleSubmit")
    public ResponseEntity sampleSubmit(@ApiParam(value = "必传：",required = true)@RequestBody @Validated List<PdaIncomingSampleSubmitDto> list) {
        return ControllerUtil.returnCRUD(qmsIncomingInspectionOrderDetSampleService.sampleSubmit(list));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsIncomingInspectionOrderDetSample qmsIncomingInspectionOrderDetSample) {
        return ControllerUtil.returnCRUD(qmsIncomingInspectionOrderDetSampleService.save(qmsIncomingInspectionOrderDetSample));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsIncomingInspectionOrderDetSampleService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsIncomingInspectionOrderDetSample.update.class) QmsIncomingInspectionOrderDetSample qmsIncomingInspectionOrderDetSample) {
        return ControllerUtil.returnCRUD(qmsIncomingInspectionOrderDetSampleService.update(qmsIncomingInspectionOrderDetSample));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsIncomingInspectionOrderDetSample> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsIncomingInspectionOrderDetSample  qmsIncomingInspectionOrderDetSample = qmsIncomingInspectionOrderDetSampleService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsIncomingInspectionOrderDetSample,StringUtils.isEmpty(qmsIncomingInspectionOrderDetSample)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsIncomingInspectionOrderDetSampleDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIncomingInspectionOrderDetSample searchQmsIncomingInspectionOrderDetSample) {
        Page<Object> page = PageHelper.startPage(searchQmsIncomingInspectionOrderDetSample.getStartPage(),searchQmsIncomingInspectionOrderDetSample.getPageSize());
        List<QmsIncomingInspectionOrderDetSampleDto> list = qmsIncomingInspectionOrderDetSampleService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIncomingInspectionOrderDetSample));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<QmsIncomingInspectionOrderDetSampleDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchQmsIncomingInspectionOrderDetSample searchQmsIncomingInspectionOrderDetSample) {
        List<QmsIncomingInspectionOrderDetSampleDto> list = qmsIncomingInspectionOrderDetSampleService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIncomingInspectionOrderDetSample));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<QmsHtIncomingInspectionOrderDetSample>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIncomingInspectionOrderDetSample searchQmsIncomingInspectionOrderDetSample) {
        Page<Object> page = PageHelper.startPage(searchQmsIncomingInspectionOrderDetSample.getStartPage(),searchQmsIncomingInspectionOrderDetSample.getPageSize());
        List<QmsHtIncomingInspectionOrderDetSample> list = qmsIncomingInspectionOrderDetSampleService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsIncomingInspectionOrderDetSample));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsIncomingInspectionOrderDetSample searchQmsIncomingInspectionOrderDetSample){
    List<QmsIncomingInspectionOrderDetSampleDto> list = qmsIncomingInspectionOrderDetSampleService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIncomingInspectionOrderDetSample));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "来料检验单明细样本值", QmsIncomingInspectionOrderDetSampleDto.class, "来料检验单明细样本值.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

}
