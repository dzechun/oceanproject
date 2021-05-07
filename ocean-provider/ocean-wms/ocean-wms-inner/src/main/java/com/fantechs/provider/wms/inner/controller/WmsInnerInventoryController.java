package com.fantechs.provider.wms.inner.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.history.WmsHtInnerInventory;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerInventory;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsHtInnerInventoryService;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryService;
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
 * Created by leifengzhi on 2021/05/07.
 */
@RestController
@Api(tags = "wmsInnerInventory控制器")
@RequestMapping("/wmsInnerInventory")
@Validated
public class WmsInnerInventoryController {

    @Resource
    private WmsInnerInventoryService wmsInnerInventoryService;

    @Resource
    private WmsHtInnerInventoryService wmsHtInnerInventoryService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerInventory wmsInnerInventory) {
        return ControllerUtil.returnCRUD(wmsInnerInventoryService.save(wmsInnerInventory));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerInventoryService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsInnerInventory.update.class) WmsInnerInventory wmsInnerInventory) {
        return ControllerUtil.returnCRUD(wmsInnerInventoryService.update(wmsInnerInventory));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerInventory> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerInventory  wmsInnerInventory = wmsInnerInventoryService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerInventory,StringUtils.isEmpty(wmsInnerInventory)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerInventoryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInventory searchWmsInnerInventory) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerInventory.getStartPage(),searchWmsInnerInventory.getPageSize());
        List<WmsInnerInventoryDto> list = wmsInnerInventoryService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsHtInnerInventory>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerInventory searchWmsInnerInventory) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerInventory.getStartPage(),searchWmsInnerInventory.getPageSize());
        List<WmsHtInnerInventory> list = wmsHtInnerInventoryService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerInventory searchWmsInnerInventory){
    List<WmsInnerInventoryDto> list = wmsInnerInventoryService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerInventory));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsInnerInventory信息", WmsInnerInventoryDto.class, "WmsInnerInventory.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
