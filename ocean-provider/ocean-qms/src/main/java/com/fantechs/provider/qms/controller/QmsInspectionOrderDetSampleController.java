package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionOrderDetSample;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsInspectionOrderDetSampleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/05/25.
 */
@RestController
@Api(tags = "成品检验单明细样本")
@RequestMapping("/qmsInspectionOrderDetSample")
@Validated
public class QmsInspectionOrderDetSampleController {

    @Resource
    private QmsInspectionOrderDetSampleService qmsInspectionOrderDetSampleService;

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty(message="样本信息不能为空")List<QmsInspectionOrderDetSample> qmsInspectionOrderDetSampleList) {
        return ControllerUtil.returnCRUD(qmsInspectionOrderDetSampleService.batchAdd(qmsInspectionOrderDetSampleList));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsInspectionOrderDetSample qmsInspectionOrderDetSample) {
        return ControllerUtil.returnCRUD(qmsInspectionOrderDetSampleService.save(qmsInspectionOrderDetSample));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsInspectionOrderDetSampleService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsInspectionOrderDetSample.update.class) QmsInspectionOrderDetSample qmsInspectionOrderDetSample) {
        return ControllerUtil.returnCRUD(qmsInspectionOrderDetSampleService.update(qmsInspectionOrderDetSample));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsInspectionOrderDetSample> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsInspectionOrderDetSample  qmsInspectionOrderDetSample = qmsInspectionOrderDetSampleService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsInspectionOrderDetSample,StringUtils.isEmpty(qmsInspectionOrderDetSample)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsInspectionOrderDetSample>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionOrderDetSample searchQmsInspectionOrderDetSample) {
        Page<Object> page = PageHelper.startPage(searchQmsInspectionOrderDetSample.getStartPage(),searchQmsInspectionOrderDetSample.getPageSize());
        List<QmsInspectionOrderDetSample> list = qmsInspectionOrderDetSampleService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrderDetSample));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("检查条码")
    @PostMapping("/checkBarcode")
    public ResponseEntity<Boolean> checkBarcode(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionOrderDetSample searchQmsInspectionOrderDetSample) {
        Boolean bool = qmsInspectionOrderDetSampleService.checkBarcode(searchQmsInspectionOrderDetSample.getBarcode(), searchQmsInspectionOrderDetSample.getInspectionOrderDetId());
        return ControllerUtil.returnDataSuccess(bool,1);
    }

    @ApiOperation("条码列表")
    @PostMapping("/findBarcodes")
    public ResponseEntity<List<QmsInspectionOrderDetSample>> findBarcodes(@ApiParam(value = "检验单ID",required = true)@RequestParam  @NotNull(message="检验单ID不能为空") Long inspectionOrderId) {
        List<QmsInspectionOrderDetSample> list = qmsInspectionOrderDetSampleService.findBarcodes(inspectionOrderId);
        return ControllerUtil.returnDataSuccess(list,list.size());
    }

    @ApiOperation(value = "检验并保存条码",notes = "检验并保存条码")
    @PostMapping("/checkAndSaveBarcode")
    public ResponseEntity<QmsInspectionOrderDetSample> checkAndSaveBarcode(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsInspectionOrderDetSample qmsInspectionOrderDetSample) {
        QmsInspectionOrderDetSample inspectionOrderDetSample = qmsInspectionOrderDetSampleService.checkAndSaveBarcode(qmsInspectionOrderDetSample);
        return  ControllerUtil.returnDataSuccess(inspectionOrderDetSample,StringUtils.isEmpty(inspectionOrderDetSample)?0:1);
    }

    /*@ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<QmsInspectionOrderDetSample>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionOrderDetSample searchQmsInspectionOrderDetSample) {
        Page<Object> page = PageHelper.startPage(searchQmsInspectionOrderDetSample.getStartPage(),searchQmsInspectionOrderDetSample.getPageSize());
        List<QmsInspectionOrderDetSample> list = qmsInspectionOrderDetSampleService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrderDetSample));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }*/

    /*@PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsInspectionOrderDetSample searchQmsInspectionOrderDetSample){
    List<QmsInspectionOrderDetSample> list = qmsInspectionOrderDetSampleService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionOrderDetSample));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "QmsInspectionOrderDetSample信息", QmsInspectionOrderDetSample.class, "QmsInspectionOrderDetSample.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }*/
}
