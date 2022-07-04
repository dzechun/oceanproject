package com.fantechs.provider.electronic.controller;


import com.fantechs.common.base.electronic.dto.PtlJobOrderDetDto;
import com.fantechs.common.base.electronic.entity.PtlJobOrderDet;
import com.fantechs.common.base.electronic.entity.search.SearchPtlJobOrderDet;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.service.PtlJobOrderDetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.seata.spring.annotation.GlobalTransactional;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/06/01.
 */
@RestController
@Api(tags = "电子标签作业任务明细控制器")
@RequestMapping("/ptlJobOrderDet")
@Validated
public class PtlJobOrderDetController {

    @Resource
    private PtlJobOrderDetService ptlJobOrderDetService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated PtlJobOrderDet ptlJobOrderDet) {
        return ControllerUtil.returnCRUD(ptlJobOrderDetService.save(ptlJobOrderDet));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    @Transactional
    @GlobalTransactional
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(ptlJobOrderDetService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    @Transactional
    @GlobalTransactional
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=PtlJobOrderDet.update.class) PtlJobOrderDet ptlJobOrderDet) {
        return ControllerUtil.returnCRUD(ptlJobOrderDetService.update(ptlJobOrderDet));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<PtlJobOrderDet> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        PtlJobOrderDet  ptlJobOrderDet = ptlJobOrderDetService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(ptlJobOrderDet,StringUtils.isEmpty(ptlJobOrderDet)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<PtlJobOrderDetDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchPtlJobOrderDet searchPtlJobOrderDet) {
        Page<Object> page = PageHelper.startPage(searchPtlJobOrderDet.getStartPage(),searchPtlJobOrderDet.getPageSize());
        List<PtlJobOrderDetDto> list = ptlJobOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchPtlJobOrderDet));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchPtlJobOrderDet searchPtlJobOrderDet){
    List<PtlJobOrderDetDto> list = ptlJobOrderDetService.findList(ControllerUtil.dynamicConditionByEntity(searchPtlJobOrderDet));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "PtlJobOrderDet信息", PtlJobOrderDetDto.class, "PtlJobOrderDet.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("批量新增")
    @PostMapping("/batchSave")
    @Transactional
    @GlobalTransactional
    public ResponseEntity batchSave(@ApiParam(value = "对象列表",required = true)@RequestBody @Validated List<PtlJobOrderDet> ptlJobOrderDetList) {
        return ControllerUtil.returnCRUD(ptlJobOrderDetService.batchSave(ptlJobOrderDetList));
    }

    @ApiOperation("批量修改")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "对象列表，Id必传",required = true)@RequestBody @Validated(value= PtlJobOrderDet.update.class) List<PtlJobOrderDet> ptlJobOrderDetList) {
        return ControllerUtil.returnCRUD(ptlJobOrderDetService.batchUpdate(ptlJobOrderDetList));
    }

    @ApiOperation("根据作业单Id修改")
    @PostMapping("/updateByJobOrderId")
    public ResponseEntity updateByJobOrderId(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= PtlJobOrderDet.update.class) PtlJobOrderDet ptlJobOrderDet) {
        return ControllerUtil.returnCRUD(ptlJobOrderDetService.updateByJobOrderId(ptlJobOrderDet));
    }
}
