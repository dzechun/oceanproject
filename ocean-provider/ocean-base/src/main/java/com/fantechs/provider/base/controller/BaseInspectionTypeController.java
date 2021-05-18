package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseInspectionTypeDto;
import com.fantechs.common.base.general.entity.basic.BaseInspectionType;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInspectionType;
import com.fantechs.common.base.general.entity.qms.search.SearchQmsInspectionType;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtInspectionTypeService;
import com.fantechs.provider.base.service.BaseInspectionTypeService;
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
 * Created by leifengzhi on 2020/12/23.
 */
@RestController
@Api(tags = "检验类型")
@RequestMapping("/baseInspectionType")
@Validated
public class BaseInspectionTypeController {

    @Autowired
    private BaseInspectionTypeService baseInspectionTypeService;
    @Autowired
    private BaseHtInspectionTypeService baseHtInspectionTypeService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：", required = true) @RequestBody @Validated BaseInspectionType baseInspectionType) {
        return ControllerUtil.returnCRUD(baseInspectionTypeService.save(baseInspectionType));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {

        return ControllerUtil.returnCRUD(baseInspectionTypeService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = BaseInspectionType.update.class) BaseInspectionType baseInspectionType) {
        return ControllerUtil.returnCRUD(baseInspectionTypeService.update(baseInspectionType));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseInspectionType> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseInspectionType baseInspectionType = baseInspectionTypeService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(baseInspectionType, StringUtils.isEmpty(baseInspectionType) ? 0 : 1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseInspectionTypeDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchQmsInspectionType searchQmsInspectionType) {
        Page<Object> page = PageHelper.startPage(searchQmsInspectionType.getStartPage(), searchQmsInspectionType.getPageSize());
        List<BaseInspectionTypeDto> list = baseInspectionTypeService.findList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionType));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtInspectionType>> findHtList(@ApiParam(value = "查询对象") @RequestBody SearchQmsInspectionType searchQmsInspectionType) {
        Page<Object> page = PageHelper.startPage(searchQmsInspectionType.getStartPage(), searchQmsInspectionType.getPageSize());
        List<BaseHtInspectionType> list = baseHtInspectionTypeService.findHtList(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionType));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel", notes = "导出excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchQmsInspectionType searchQmsInspectionType) {
        List<BaseInspectionTypeDto> list = baseInspectionTypeService.exportExcel(ControllerUtil.dynamicConditionByEntity(searchQmsInspectionType));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "检验类型导出信息", "检验类型信息", BaseInspectionType.class, "检验类型.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
