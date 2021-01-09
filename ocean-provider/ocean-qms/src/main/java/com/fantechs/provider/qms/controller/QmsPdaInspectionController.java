package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.dto.storage.MesPackageManagerDTO;
import com.fantechs.common.base.dto.storage.SearchMesPackageManagerListDTO;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsPdaInspectionDto;
import com.fantechs.common.base.general.entity.qms.QmsPdaInspection;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsPdaInspection;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsPdaInspectionService;
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
@Api(tags = "PDA质检")
@RequestMapping("/qmsPdaInspection")
@Validated
public class QmsPdaInspectionController {

    @Autowired
    private QmsPdaInspectionService qmsPdaInspectionService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated QmsPdaInspection qmsPdaInspection) {
        return ControllerUtil.returnCRUD(qmsPdaInspectionService.save(qmsPdaInspection));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsPdaInspectionService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=QmsPdaInspection.update.class) QmsPdaInspection qmsPdaInspection) {
        return ControllerUtil.returnCRUD(qmsPdaInspectionService.update(qmsPdaInspection));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsPdaInspection> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        QmsPdaInspection  qmsPdaInspection = qmsPdaInspectionService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(qmsPdaInspection,StringUtils.isEmpty(qmsPdaInspection)?0:1);
    }

    @ApiOperation("解析传入的箱码")
    @PostMapping("/analysisCode")
    public ResponseEntity<QmsPdaInspectionDto> analysisCode(@ApiParam(value = "查询条件，请参考Model说明")@RequestBody(required = false) SearchMesPackageManagerListDTO searchMesPackageManagerListDTO) {
        QmsPdaInspectionDto  qmsPdaInspectionDto = qmsPdaInspectionService.analysisCode(ControllerUtil.dynamicConditionByEntity(searchMesPackageManagerListDTO));
        return  ControllerUtil.returnDataSuccess(qmsPdaInspectionDto,StringUtils.isEmpty(qmsPdaInspectionDto)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsPdaInspectionDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchQmsPdaInspection searchQmsPdaInspection) {
        Page<Object> page = PageHelper.startPage(searchQmsPdaInspection.getStartPage(),searchQmsPdaInspection.getPageSize());
        List<QmsPdaInspectionDto> list = qmsPdaInspectionService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsPdaInspection));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }
//
//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<QmsPdaInspection>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchQmsPdaInspection searchQmsPdaInspection) {
//        Page<Object> page = PageHelper.startPage(searchQmsPdaInspection.getStartPage(),searchQmsPdaInspection.getPageSize());
//        List<QmsPdaInspection> list = qmsPdaInspectionService.findList(searchQmsPdaInspection);
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }
//
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsPdaInspection searchQmsPdaInspection){
    List<QmsPdaInspectionDto> list = qmsPdaInspectionService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsPdaInspection));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "PDA质检导出信息", "PDA质检信息", QmsPdaInspectionDto.class, "PDA质检.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}