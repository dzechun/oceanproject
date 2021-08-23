package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainProjectItemDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainProjectItem;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipmentMaintainProjectItem;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamEquipmentMaintainProjectItemService;
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
 * Created by leifengzhi on 2021/08/21.
 */
@RestController
@Api(tags = "设备保养项目明细")
@RequestMapping("/eamEquipmentMaintainProjectItem")
@Validated
public class EamEquipmentMaintainProjectItemController {

    @Resource
    private EamEquipmentMaintainProjectItemService eamEquipmentMaintainProjectItemService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquipmentMaintainProjectItem eamEquipmentMaintainProjectItem) {
        return ControllerUtil.returnCRUD(eamEquipmentMaintainProjectItemService.save(eamEquipmentMaintainProjectItem));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquipmentMaintainProjectItemService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquipmentMaintainProjectItem.update.class) EamEquipmentMaintainProjectItem eamEquipmentMaintainProjectItem) {
        return ControllerUtil.returnCRUD(eamEquipmentMaintainProjectItemService.update(eamEquipmentMaintainProjectItem));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquipmentMaintainProjectItem> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquipmentMaintainProjectItem  eamEquipmentMaintainProjectItem = eamEquipmentMaintainProjectItemService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquipmentMaintainProjectItem,StringUtils.isEmpty(eamEquipmentMaintainProjectItem)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquipmentMaintainProjectItemDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentMaintainProjectItem searchEamEquipmentMaintainProjectItem) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentMaintainProjectItem.getStartPage(),searchEamEquipmentMaintainProjectItem.getPageSize());
        List<EamEquipmentMaintainProjectItemDto> list = eamEquipmentMaintainProjectItemService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaintainProjectItem));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamEquipmentMaintainProjectItemDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquipmentMaintainProjectItem searchEamEquipmentMaintainProjectItem) {
        Page<Object> page = PageHelper.startPage(searchEamEquipmentMaintainProjectItem.getStartPage(),searchEamEquipmentMaintainProjectItem.getPageSize());
        List<EamEquipmentMaintainProjectItemDto> list = eamEquipmentMaintainProjectItemService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaintainProjectItem));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquipmentMaintainProjectItem searchEamEquipmentMaintainProjectItem){
    List<EamEquipmentMaintainProjectItemDto> list = eamEquipmentMaintainProjectItemService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquipmentMaintainProjectItem));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "设备保养项目明细", EamEquipmentMaintainProjectItemDto.class, "设备保养项目明细.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
