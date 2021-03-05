package com.fantechs.provider.qms.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.qms.QmsQualityInspectionDto;
import com.fantechs.common.base.general.entity.qms.QmsQualityInspection;
import com.fantechs.common.base.general.entity.qms.history.QmsHtQualityInspection;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsQualityInspection;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.qms.service.QmsHtQualityInspectionService;
import com.fantechs.provider.qms.service.QmsQualityInspectionService;
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
 * Created by leifengzhi on 2020/12/16.
 */
@RestController
@Api(tags = "检验单")
@RequestMapping("/qmsQualityInspection")
@Validated
public class QmsQualityInspectionController {

    @Autowired
    private QmsQualityInspectionService qmsQualityInspectionService;

    @Autowired
    private QmsHtQualityInspectionService qmsHtQualityInspectionService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：", required = true) @RequestBody @Validated QmsQualityInspection qmsQualityInspection) {
        return ControllerUtil.returnCRUD(qmsQualityInspectionService.save(qmsQualityInspection));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(qmsQualityInspectionService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = QmsQualityInspection.update.class) QmsQualityInspection qmsQualityInspection) {
        return ControllerUtil.returnCRUD(qmsQualityInspectionService.update(qmsQualityInspection));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<QmsQualityInspection> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        QmsQualityInspection qmsQualityInspection = qmsQualityInspectionService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(qmsQualityInspection, StringUtils.isEmpty(qmsQualityInspection) ? 0 : 1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<QmsQualityInspectionDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchQmsQualityInspection searchQmsQualityInspection) {
        Page<Object> page = PageHelper.startPage(searchQmsQualityInspection.getStartPage(), searchQmsQualityInspection.getPageSize());
        List<QmsQualityInspectionDto> list = qmsQualityInspectionService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsQualityInspection));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<QmsHtQualityInspection>> findHtList(@ApiParam(value = "查询对象") @RequestBody SearchQmsQualityInspection searchQmsQualityInspection) {
        Page<Object> page = PageHelper.startPage(searchQmsQualityInspection.getStartPage(), searchQmsQualityInspection.getPageSize());
        List<QmsHtQualityInspection> list = qmsHtQualityInspectionService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsQualityInspection));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel", notes = "导出excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsQualityInspection searchQmsQualityInspection) {
        List<QmsQualityInspectionDto> list = qmsQualityInspectionService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsQualityInspection));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "质检计划信息导出", "质检计划信息", QmsQualityInspectionDto.class, "质检计划信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }


}
