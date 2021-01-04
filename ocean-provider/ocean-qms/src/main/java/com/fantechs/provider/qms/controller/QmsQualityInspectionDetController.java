package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsQualityInspectionDetDto;
import com.fantechs.common.base.general.entity.qms.QmsQualityInspectionDet;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsQualityInspectionDet;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsQualityInspectionDetService;
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
 * Created by leifengzhi on 2020/12/16.
 */
@RestController
@Api(tags = "质检单明细")
@RequestMapping("/qmsQualityInspectionDet")
@Validated
public class QmsQualityInspectionDetController {

    @Autowired
    private QmsQualityInspectionDetService qmsQualityInspectionDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsQualityInspectionDet qmsQualityInspectionDet) {
        return ControllerUtil.returnCRUD(qmsQualityInspectionDetService.save(qmsQualityInspectionDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsQualityInspectionDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsQualityInspectionDet.update.class) QmsQualityInspectionDet qmsQualityInspectionDet) {
        return ControllerUtil.returnCRUD(qmsQualityInspectionDetService.update(qmsQualityInspectionDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsQualityInspectionDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsQualityInspectionDet  qmsQualityInspectionDet = qmsQualityInspectionDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsQualityInspectionDet,StringUtils.isEmpty(qmsQualityInspectionDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsQualityInspectionDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsQualityInspectionDet searchQmsQualityInspectionDet) {
        Page<Object> page = PageHelper.startPage(searchQmsQualityInspectionDet.getStartPage(),searchQmsQualityInspectionDet.getPageSize());
        List<QmsQualityInspectionDetDto> list = qmsQualityInspectionDetService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsQualityInspectionDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsQualityInspectionDet searchQmsQualityInspectionDet){
    List<QmsQualityInspectionDetDto> list = qmsQualityInspectionDetService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsQualityInspectionDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "质检单明细导出信息", "质检单明细信息", QmsQualityInspectionDetDto.class, "质检单明细.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
