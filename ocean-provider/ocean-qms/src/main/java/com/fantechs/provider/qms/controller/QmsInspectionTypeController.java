package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsInspectionTypeDto;
import com.fantechs.common.base.general.entity.qms.QmsInspectionType;
import com.fantechs.common.base.general.entity.qms.history.QmsHtInspectionType;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionType;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsHtInspectionTypeService;
import com.fantechs.provider.qms.service.QmsInspectionTypeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2020/12/23.
 */
@RestController
@Api(tags = "检验类型")
@RequestMapping("/qmsInspectionType")
@Validated
public class QmsInspectionTypeController {

    @Autowired
    private QmsInspectionTypeService qmsInspectionTypeService;
    @Autowired
    private QmsHtInspectionTypeService qmsHtInspectionTypeService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsInspectionType qmsInspectionType) {
        return ControllerUtil.returnCRUD(qmsInspectionTypeService.save(qmsInspectionType));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {

        return ControllerUtil.returnCRUD(qmsInspectionTypeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsInspectionType.update.class) QmsInspectionType qmsInspectionType) {
        return ControllerUtil.returnCRUD(qmsInspectionTypeService.update(qmsInspectionType));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsInspectionType> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsInspectionType  qmsInspectionType = qmsInspectionTypeService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsInspectionType,StringUtils.isEmpty(qmsInspectionType)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsInspectionTypeDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionType searchQmsInspectionType) {
        Page<Object> page = PageHelper.startPage(searchQmsInspectionType.getStartPage(),searchQmsInspectionType.getPageSize());
        List<QmsInspectionTypeDto> list = qmsInspectionTypeService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionType));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<QmsHtInspectionType>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsInspectionType searchQmsInspectionType) {
        Page<Object> page = PageHelper.startPage(searchQmsInspectionType.getStartPage(),searchQmsInspectionType.getPageSize());
        List<QmsHtInspectionType> list = qmsHtInspectionTypeService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionType));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsInspectionType searchQmsInspectionType){
    List<QmsInspectionTypeDto> list = qmsInspectionTypeService.exportExcel(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionType));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "检验类型导出信息", "检验类型信息", QmsInspectionType.class, "检验类型.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
