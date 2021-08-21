package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionProjectItemDto;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionProjectItem;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquPointInspectionProjectItem;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamEquPointInspectionProjectItemService;
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
 * Created by leifengzhi on 2021/08/20.
 */
@RestController
@Api(tags = "eamEquPointInspectionProjectItem控制器")
@RequestMapping("/eamEquPointInspectionProjectItem")
@Validated
public class EamEquPointInspectionProjectItemController {

    @Resource
    private EamEquPointInspectionProjectItemService eamEquPointInspectionProjectItemService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamEquPointInspectionProjectItem eamEquPointInspectionProjectItem) {
        return ControllerUtil.returnCRUD(eamEquPointInspectionProjectItemService.save(eamEquPointInspectionProjectItem));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamEquPointInspectionProjectItemService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamEquPointInspectionProjectItem.update.class) EamEquPointInspectionProjectItem eamEquPointInspectionProjectItem) {
        return ControllerUtil.returnCRUD(eamEquPointInspectionProjectItemService.update(eamEquPointInspectionProjectItem));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamEquPointInspectionProjectItem> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamEquPointInspectionProjectItem  eamEquPointInspectionProjectItem = eamEquPointInspectionProjectItemService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamEquPointInspectionProjectItem,StringUtils.isEmpty(eamEquPointInspectionProjectItem)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamEquPointInspectionProjectItemDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquPointInspectionProjectItem searchEamEquPointInspectionProjectItem) {
        Page<Object> page = PageHelper.startPage(searchEamEquPointInspectionProjectItem.getStartPage(),searchEamEquPointInspectionProjectItem.getPageSize());
        List<EamEquPointInspectionProjectItemDto> list = eamEquPointInspectionProjectItemService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquPointInspectionProjectItem));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamEquPointInspectionProjectItemDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamEquPointInspectionProjectItem searchEamEquPointInspectionProjectItem) {
        Page<Object> page = PageHelper.startPage(searchEamEquPointInspectionProjectItem.getStartPage(),searchEamEquPointInspectionProjectItem.getPageSize());
        List<EamEquPointInspectionProjectItemDto> list = eamEquPointInspectionProjectItemService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamEquPointInspectionProjectItem));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamEquPointInspectionProjectItem searchEamEquPointInspectionProjectItem){
    List<EamEquPointInspectionProjectItemDto> list = eamEquPointInspectionProjectItemService.findList(ControllerUtil.dynamicConditionByEntity(searchEamEquPointInspectionProjectItem));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "EamEquPointInspectionProjectItem信息", EamEquPointInspectionProjectItemDto.class, "EamEquPointInspectionProjectItem.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
