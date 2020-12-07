package com.fantechs.provider.electronic.controller;

import com.fantechs.common.base.electronic.dto.SmtEquipmentDto;
import com.fantechs.common.base.electronic.entity.SmtEquipment;
import com.fantechs.common.base.entity.basic.search.SearchSmtEquipment;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.electronic.service.SmtEquipmentService;
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
public class SmtEquipmentController {

    @Autowired
    private SmtEquipmentService smtEquipmentService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：equipmentCode、equipmentName", required = true) @RequestBody @Validated SmtEquipment smtEquipment) {
        return ControllerUtil.returnCRUD(smtEquipmentService.save(smtEquipment));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(smtEquipmentService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = SmtEquipment.update.class) SmtEquipment smtEquipment) {
        return ControllerUtil.returnCRUD(smtEquipmentService.update(smtEquipment));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<SmtEquipment> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        SmtEquipment smtEquipment = smtEquipmentService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(smtEquipment, StringUtils.isEmpty(smtEquipment) ? 0 : 1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<SmtEquipmentDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchSmtEquipment searchSmtEquipment) {
        Page<Object> page = PageHelper.startPage(searchSmtEquipment.getStartPage(), searchSmtEquipment.getPageSize());
        List<SmtEquipmentDto> list = smtEquipmentService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtEquipment));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel", notes = "导出excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchSmtEquipment searchSmtEquipment) {
        List<SmtEquipmentDto> list = smtEquipmentService.findList(ControllerUtil.dynamicConditionByEntity(searchSmtEquipment));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出信息", "SmtEquipment信息", SmtEquipmentDto.class, "SmtEquipment.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
