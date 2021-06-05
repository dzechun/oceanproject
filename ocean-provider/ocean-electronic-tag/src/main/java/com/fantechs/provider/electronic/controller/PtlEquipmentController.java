package com.fantechs.provider.electronic.controller;

import com.fantechs.common.base.electronic.dto.PtlEquipmentDto;
import com.fantechs.common.base.electronic.entity.PtlEquipment;
import com.fantechs.common.base.electronic.entity.search.SearchPtlEquipment;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.service.PtlEquipmentService;
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
 * Created by leifengzhi on 2020/11/23.
 */
@RestController
@Api(tags = "设备信息管理")
@RequestMapping("/smtEquipment")
@Validated
public class PtlEquipmentController {

    @Autowired
    private PtlEquipmentService ptlEquipmentService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：equipmentCode、equipmentName、clientId、equipmentTagId", required = true) @RequestBody @Validated PtlEquipment ptlEquipment) {
        return ControllerUtil.returnCRUD(ptlEquipmentService.save(ptlEquipment));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(ptlEquipmentService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = PtlEquipment.update.class) PtlEquipment ptlEquipment) {
        return ControllerUtil.returnCRUD(ptlEquipmentService.update(ptlEquipment));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<PtlEquipment> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        PtlEquipment ptlEquipment = ptlEquipmentService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(ptlEquipment, StringUtils.isEmpty(ptlEquipment) ? 0 : 1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<PtlEquipmentDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchPtlEquipment searchPtlEquipment) {
        Page<Object> page = PageHelper.startPage(searchPtlEquipment.getStartPage(), searchPtlEquipment.getPageSize());
        List<PtlEquipmentDto> list = ptlEquipmentService.findList(ControllerUtil.dynamicConditionByEntity(searchPtlEquipment));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel", notes = "导出excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchPtlEquipment searchPtlEquipment) {
        List<PtlEquipmentDto> list = ptlEquipmentService.findList(ControllerUtil.dynamicConditionByEntity(searchPtlEquipment));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出信息", "SmtEquipment信息", PtlEquipmentDto.class, "SmtEquipment.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
