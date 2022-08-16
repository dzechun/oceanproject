package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.qms.QmsInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.QmsIpqcInspectionOrderDetSample;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsIpqcInspectionOrderDetSample;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsIpqcInspectionOrderDetSampleService;
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
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/06/04.
 */
@RestController
@Api(tags = "IPQC检验单明细样本值")
@RequestMapping("/qmsIpqcInspectionOrderDetSample")
@Validated
public class QmsIpqcInspectionOrderDetSampleController {

    @Resource
    private QmsIpqcInspectionOrderDetSampleService qmsIpqcInspectionOrderDetSampleService;


    @ApiOperation(value = "条码检验批量新增",notes = "条码检验批量新增")
    @PostMapping("/barcodeBatchAdd")
    public ResponseEntity barcodeBatchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty(message="样本信息不能为空")List<QmsIpqcInspectionOrderDetSample> qmsIpqcInspectionOrderDetSampleList) {
        return ControllerUtil.returnCRUD(qmsIpqcInspectionOrderDetSampleService.barcodeBatchAdd(qmsIpqcInspectionOrderDetSampleList));
    }

    @ApiOperation(value = "批量新增",notes = "批量新增")
    @PostMapping("/batchAdd")
    public ResponseEntity batchAdd(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty(message="样本信息不能为空")List<QmsIpqcInspectionOrderDetSample> qmsIpqcInspectionOrderDetSampleList) {
        return ControllerUtil.returnCRUD(qmsIpqcInspectionOrderDetSampleService.batchAdd(qmsIpqcInspectionOrderDetSampleList));
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsIpqcInspectionOrderDetSample qmsIpqcInspectionOrderDetSample) {
        return ControllerUtil.returnCRUD(qmsIpqcInspectionOrderDetSampleService.save(qmsIpqcInspectionOrderDetSample));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsIpqcInspectionOrderDetSampleService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsIpqcInspectionOrderDetSample.update.class) QmsIpqcInspectionOrderDetSample qmsIpqcInspectionOrderDetSample) {
        return ControllerUtil.returnCRUD(qmsIpqcInspectionOrderDetSampleService.update(qmsIpqcInspectionOrderDetSample));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsIpqcInspectionOrderDetSample> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsIpqcInspectionOrderDetSample  qmsIpqcInspectionOrderDetSample = qmsIpqcInspectionOrderDetSampleService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsIpqcInspectionOrderDetSample,StringUtils.isEmpty(qmsIpqcInspectionOrderDetSample)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsIpqcInspectionOrderDetSample>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIpqcInspectionOrderDetSample searchQmsIpqcInspectionOrderDetSample) {
        Page<Object> page = PageHelper.startPage(searchQmsIpqcInspectionOrderDetSample.getStartPage(),searchQmsIpqcInspectionOrderDetSample.getPageSize());
        List<QmsIpqcInspectionOrderDetSample> list = qmsIpqcInspectionOrderDetSampleService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrderDetSample));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("检查条码")
    @PostMapping("/checkBarcode")
    public ResponseEntity<Boolean> checkBarcode(@ApiParam(value = "查询对象")@RequestBody SearchQmsIpqcInspectionOrderDetSample searchQmsIpqcInspectionOrderDetSample) {
        Boolean bool = qmsIpqcInspectionOrderDetSampleService.checkBarcode(searchQmsIpqcInspectionOrderDetSample.getBarcode(), searchQmsIpqcInspectionOrderDetSample.getIpqcInspectionOrderDetId());
        return ControllerUtil.returnDataSuccess(bool,1);
    }

    /*@ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<QmsIpqcInspectionOrderDetSample>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsIpqcInspectionOrderDetSample searchQmsIpqcInspectionOrderDetSample) {
        Page<Object> page = PageHelper.startPage(searchQmsIpqcInspectionOrderDetSample.getStartPage(),searchQmsIpqcInspectionOrderDetSample.getPageSize());
        List<QmsIpqcInspectionOrderDetSample> list = qmsIpqcInspectionOrderDetSampleService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrderDetSample));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }*/

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsIpqcInspectionOrderDetSample searchQmsIpqcInspectionOrderDetSample){
    List<QmsIpqcInspectionOrderDetSample> list = qmsIpqcInspectionOrderDetSampleService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsIpqcInspectionOrderDetSample));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "IPQC检验单明细样本值", QmsIpqcInspectionOrderDetSample.class, "IPQC检验单明细样本值.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
