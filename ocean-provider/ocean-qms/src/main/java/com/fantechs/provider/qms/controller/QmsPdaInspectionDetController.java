package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsPdaInspectionDetDto;
import com.fantechs.common.base.general.entity.qms.QmsPdaInspectionDet;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsPdaInspectionDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsPdaInspectionDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/01/07.
 */
@RestController
@Api(tags = "PDA质检明细")
@RequestMapping("/qmsPdaInspectionDet")
@Validated
public class QmsPdaInspectionDetController {

    @Autowired
    private QmsPdaInspectionDetService qmsPdaInspectionDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsPdaInspectionDet qmsPdaInspectionDet) {
        return ControllerUtil.returnCRUD(qmsPdaInspectionDetService.save(qmsPdaInspectionDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsPdaInspectionDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsPdaInspectionDet.update.class) QmsPdaInspectionDet qmsPdaInspectionDet) {
        return ControllerUtil.returnCRUD(qmsPdaInspectionDetService.update(qmsPdaInspectionDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsPdaInspectionDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsPdaInspectionDet  qmsPdaInspectionDet = qmsPdaInspectionDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsPdaInspectionDet,StringUtils.isEmpty(qmsPdaInspectionDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsPdaInspectionDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsPdaInspectionDet searchQmsPdaInspectionDet) {
        Page<Object> page = PageHelper.startPage(searchQmsPdaInspectionDet.getStartPage(),searchQmsPdaInspectionDet.getPageSize());
        List<QmsPdaInspectionDetDto> list = qmsPdaInspectionDetService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsPdaInspectionDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsPdaInspectionDet searchQmsPdaInspectionDet){
    List<QmsPdaInspectionDetDto> list = qmsPdaInspectionDetService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsPdaInspectionDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "QmsPdaInspectionDet信息", QmsPdaInspectionDet.class, "QmsPdaInspectionDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
