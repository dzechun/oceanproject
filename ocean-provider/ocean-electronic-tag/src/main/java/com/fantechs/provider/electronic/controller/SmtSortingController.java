package com.fantechs.provider.electronic.controller;

import com.fantechs.common.base.electronic.dto.SmtSortingDto;
import com.fantechs.common.base.electronic.entity.SmtSorting;
import com.fantechs.common.base.electronic.entity.search.SearchSmtSorting;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.service.SmtSortingService;
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
 * Created by leifengzhi on 2020/12/08.
 */
@RestController
@Api(tags = "分拣单管理")
@RequestMapping("/smtSorting")
@Validated
public class SmtSortingController {

    @Autowired
    private SmtSortingService SmtSortingService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated SmtSorting SmtSorting) {
        return ControllerUtil.returnCRUD(SmtSortingService.save(SmtSorting));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(SmtSortingService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtSorting.update.class) SmtSorting SmtSorting) {
        return ControllerUtil.returnCRUD(SmtSortingService.update(SmtSorting));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtSorting> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        SmtSorting  SmtSorting = SmtSortingService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(SmtSorting,StringUtils.isEmpty(SmtSorting)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtSortingDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchSmtSorting searchSmtSorting) {
        Page<Object> page = PageHelper.startPage(searchSmtSorting.getStartPage(),searchSmtSorting.getPageSize());
        List<SmtSortingDto> SmtSortingDtos = SmtSortingService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtSorting));
        return ControllerUtil.returnDataSuccess(SmtSortingDtos,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtSorting searchSmtSorting){
        List<SmtSortingDto> SortingDtos = SmtSortingService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtSorting));
        try {
        // 导出操作
        EasyPoiUtils.exportExcel(SortingDtos, "导出信息", "SmtSorting信息", SmtSortingDto.class, "SmtSorting.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("批量修改")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=SmtSorting.update.class) List<SmtSorting> SmtSortings) {
        return ControllerUtil.returnCRUD(SmtSortingService.batchUpdate(SmtSortings));
    }

    @ApiOperation("批量新增")
    @PostMapping("/batchSave")
    public ResponseEntity batchInsert(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated List<SmtSorting> SmtSortings) {
        return ControllerUtil.returnCRUD(SmtSortingService.batchSave(SmtSortings));
    }

    @ApiOperation("批量删除")
    @PostMapping("/batchDelete")
    public ResponseEntity batchDelete(@ApiParam(value = "对象，sortingCode必传",required = true)@RequestBody @Validated List<String> sortingCodes) {
        return ControllerUtil.returnCRUD(SmtSortingService.delBatchBySortingCode(sortingCodes));
    }
}
