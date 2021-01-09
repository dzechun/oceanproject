package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.dto.storage.SearchMesPackageManagerListDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsFirstInspectionDto;
import com.fantechs.common.base.general.entity.qms.QmsFirstInspection;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsFirstInspection;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsFirstInspectionService;
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


@RestController
@Api(tags = "PDA首检")
@RequestMapping("/qmsFirstInspection")
@Validated
public class QmsFirstInspectionController {

    @Autowired
    private QmsFirstInspectionService qmsFirstInspectionService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsFirstInspection qmsFirstInspection) {
        return ControllerUtil.returnCRUD(qmsFirstInspectionService.save(qmsFirstInspection));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsFirstInspectionService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsFirstInspection.update.class) QmsFirstInspection qmsFirstInspection) {
        return ControllerUtil.returnCRUD(qmsFirstInspectionService.update(qmsFirstInspection));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsFirstInspection> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsFirstInspection  qmsFirstInspection = qmsFirstInspectionService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsFirstInspection,StringUtils.isEmpty(qmsFirstInspection)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsFirstInspectionDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsFirstInspection searchQmsFirstInspection) {
        Page<Object> page = PageHelper.startPage(searchQmsFirstInspection.getStartPage(),searchQmsFirstInspection.getPageSize());
        List<QmsFirstInspectionDto> list = qmsFirstInspectionService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsFirstInspection));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsFirstInspection searchQmsFirstInspection){
    List<QmsFirstInspectionDto> list = qmsFirstInspectionService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsFirstInspection));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "QmsFirstInspection信息", QmsFirstInspection.class, "QmsFirstInspection.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
