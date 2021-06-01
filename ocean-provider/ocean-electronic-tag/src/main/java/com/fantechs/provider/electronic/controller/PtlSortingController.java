package com.fantechs.provider.electronic.controller;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.dto.PtlSortingDto;
import com.fantechs.common.base.electronic.entity.PtlSorting;
import com.fantechs.common.base.electronic.entity.search.SearchPtlSorting;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.service.PtlSortingService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
public class PtlSortingController {

    @Autowired
    private PtlSortingService ptlSortingService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    @Transactional
    @LcnTransaction
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated PtlSorting ptlSorting) {
        return ControllerUtil.returnCRUD(ptlSortingService.save(ptlSorting));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(ptlSortingService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    @Transactional
    @LcnTransaction
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= PtlSorting.update.class) PtlSorting ptlSorting) {
        return ControllerUtil.returnCRUD(ptlSortingService.update(ptlSorting));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<PtlSorting> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        PtlSorting PtlSorting = ptlSortingService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(PtlSorting,StringUtils.isEmpty(PtlSorting)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<PtlSortingDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchPtlSorting searchPtlSorting) {
        Page<Object> page = PageHelper.startPage(searchPtlSorting.getStartPage(), searchPtlSorting.getPageSize());
        List<PtlSortingDto> ptlSortingDtos = ptlSortingService.findList(ControllerUtil.dynamicConditionByEntity(searchPtlSorting));
        return ControllerUtil.returnDataSuccess(ptlSortingDtos,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchPtlSorting searchPtlSorting){
        List<PtlSortingDto> SortingDtos = ptlSortingService.findList(ControllerUtil.dynamicConditionByEntity(searchPtlSorting));
        try {
        // 导出操作
        EasyPoiUtils.exportExcel(SortingDtos, "导出信息", "SmtSorting信息", PtlSortingDto.class, "SmtSorting.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("批量修改")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= PtlSorting.update.class) List<PtlSorting> ptlSortings) {
        return ControllerUtil.returnCRUD(ptlSortingService.batchUpdate(ptlSortings));
    }

    @ApiOperation("批量新增")
    @PostMapping("/batchSave")
    @Transactional
    @LcnTransaction
    public ResponseEntity batchInsert(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated List<PtlSorting> ptlSortings) {
        return ControllerUtil.returnCRUD(ptlSortingService.batchSave(ptlSortings));
    }

    @ApiOperation("批量删除")
    @PostMapping("/batchDelete")
    public ResponseEntity batchDelete(@ApiParam(value = "对象，sortingCode必传",required = true)@RequestBody @Validated List<String> sortingCodes) {
        return ControllerUtil.returnCRUD(ptlSortingService.delBatchBySortingCode(sortingCodes));
    }

    @ApiOperation("修改状态")
    @GetMapping("/updateStatus")
    public ResponseEntity updateStatus(
            @ApiParam(value = "任务单",required = true)@RequestParam String sortingCode,
            @ApiParam(value = "操作状态（1-激活 2-完成 3-异常）",required = true)@RequestParam Byte status) {

        int i = ptlSortingService.updateStatus(sortingCode, status);
        if (i == 0) {
            return ControllerUtil.returnFail("当前选择任务号没有待激活任务", ErrorCodeEnum.GL9999404.getCode());
        }
        return ControllerUtil.returnCRUD(i);
    }
}
