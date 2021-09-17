package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentBackupDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentBackup;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentBackup;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentBackup;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamEquipmentBackupService;
import com.fantechs.provider.eam.service.EamHtEquipmentBackupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/09/16.
 */
@RestController
@Api(tags = "备用件控制器")
@RequestMapping("/eamEquipmentBackup")
@Validated
public class EamEquipmentBackupController {

    @Resource
    private EamEquipmentBackupService eamEquipmentBackupService;
    @Resource
    private EamHtEquipmentBackupService eamHtEquipmentBackupService;
    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentBackup eamEquipmentBackup) {
        return ControllerUtil.returnCRUD(eamEquipmentBackupService.save(eamEquipmentBackup));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentBackupService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentBackup.update.class) EamEquipmentBackup eamEquipmentBackup) {
        return ControllerUtil.returnCRUD(eamEquipmentBackupService.update(eamEquipmentBackup));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentBackup> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentBackup  eamEquipmentBackup = eamEquipmentBackupService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentBackup,StringUtils.isEmpty(eamEquipmentBackup)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentBackupDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentBackup searchEamEquipmentBackup) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentBackup.getStartPage(),searchEamEquipmentBackup.getPageSize());
        List<EamEquipmentBackupDto> list = eamEquipmentBackupService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentBackup));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<EamEquipmentBackupDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchEamEquipmentBackup searchEamEquipmentBackup) {
        List<EamEquipmentBackupDto> list = eamEquipmentBackupService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentBackup));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtEquipmentBackup>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentBackup searchEamEquipmentBackup) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentBackup.getStartPage(),searchEamEquipmentBackup.getPageSize());
        List<EamHtEquipmentBackup> list = eamHtEquipmentBackupService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentBackup));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquipmentBackup searchEamEquipmentBackup){
    List<EamEquipmentBackupDto> list = eamEquipmentBackupService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentBackup));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "EamEquipmentBackup信息", EamEquipmentBackupDto.class, "EamEquipmentBackup.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
