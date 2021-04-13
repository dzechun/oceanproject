package com.fantechs.provider.wms.inner.controller;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStorageInventoryDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStorageInventory;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerStorageInventory;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.inner.service.WmsInnerStorageInventoryService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
 * Created by leifengzhi on 2020/12/02.
 */
@RestController
@Api(tags = "储位库存")
@RequestMapping("/wmsInnerStorageInventory")
@Validated
public class WmsInnerStorageInventoryController {

    @Resource
    private WmsInnerStorageInventoryService wmsInnerStorageInventoryService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    @Transactional
    @LcnTransaction
    public ResponseEntity<WmsInnerStorageInventory> add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerStorageInventory wmsInnerStorageInventory) {
        wmsInnerStorageInventoryService.save(wmsInnerStorageInventory);
        return ControllerUtil.returnDataSuccess(wmsInnerStorageInventory, StringUtils.isEmpty(wmsInnerStorageInventory)?0:1);
    }

    @ApiOperation(value = "扣除库存",notes = "扣除库存")
    @PostMapping("/out")
    public ResponseEntity<WmsInnerStorageInventory> out(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsInnerStorageInventory wmsInnerStorageInventory) {
        return ControllerUtil.returnCRUD(wmsInnerStorageInventoryService.out(wmsInnerStorageInventory));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsInnerStorageInventoryService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    @Transactional
    @LcnTransaction
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value= WmsInnerStorageInventory.update.class) WmsInnerStorageInventory wmsInnerStorageInventory) {
        return ControllerUtil.returnCRUD(wmsInnerStorageInventoryService.update(wmsInnerStorageInventory));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsInnerStorageInventory> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsInnerStorageInventory wmsInnerStorageInventory = wmsInnerStorageInventoryService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsInnerStorageInventory, StringUtils.isEmpty(wmsInnerStorageInventory)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsInnerStorageInventoryDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsInnerStorageInventory searchWmsInnerStorageInventory) {
        Page<Object> page = PageHelper.startPage(searchWmsInnerStorageInventory.getStartPage(), searchWmsInnerStorageInventory.getPageSize());
        List<WmsInnerStorageInventoryDto> list = wmsInnerStorageInventoryService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStorageInventory));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsInnerStorageInventory searchWmsInnerStorageInventory){
    List<WmsInnerStorageInventoryDto> list = wmsInnerStorageInventoryService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsInnerStorageInventory));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "SmtStorageInventory信息", WmsInnerStorageInventory.class, "SmtStorageInventory.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }


}
