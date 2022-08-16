package com.fantechs.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.ValidGroup;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.dto.StorageMonthEndInventoryDto;
import com.fantechs.entity.StorageMonthEndInventory;
import com.fantechs.entity.search.SearchStorageMonthEndInventory;
import com.fantechs.service.StorageMonthEndInventoryService;
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
 * Created by leifengzhi on 2021/03/02.
 */
@RestController
@Api(tags = "月末库存")
@RequestMapping("/storageMonthEndInventory")
@Validated
public class StorageMonthEndInventoryController {

    @Resource
    private StorageMonthEndInventoryService storageMonthEndInventoryService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：", required = true) @RequestBody @Validated StorageMonthEndInventory storageMonthEndInventory) {
        return ControllerUtil.returnCRUD(storageMonthEndInventoryService.save(storageMonthEndInventory));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(storageMonthEndInventoryService.batchDelete(ids));
    }

    @ApiOperation(value = "记录月末库存数据", notes = "记录月末库存数据")
    @GetMapping("/record")
    public ResponseEntity record() {
        return ControllerUtil.returnCRUD(storageMonthEndInventoryService.record());
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = ValidGroup.update.class) StorageMonthEndInventory storageMonthEndInventory) {
        return ControllerUtil.returnCRUD(storageMonthEndInventoryService.update(storageMonthEndInventory));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<StorageMonthEndInventory> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        StorageMonthEndInventory storageMonthEndInventory = storageMonthEndInventoryService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(storageMonthEndInventory, StringUtils.isEmpty(storageMonthEndInventory) ? 0 : 1);
    }

    @ApiOperation("获取当前库存列表")
    @PostMapping("/findList")
    public ResponseEntity<List<StorageMonthEndInventoryDto>> findList(@ApiParam(value = "查询对象") @RequestBody SearchStorageMonthEndInventory searchStorageMonthEndInventory) {
        Page<Object> page = PageHelper.startPage(searchStorageMonthEndInventory.getStartPage(), searchStorageMonthEndInventory.getPageSize());
        List<StorageMonthEndInventoryDto> list = storageMonthEndInventoryService.findList(ControllerUtil.dynamicConditionByEntity(searchStorageMonthEndInventory));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @ApiOperation("获取月末库存列表")
    @PostMapping("/findMonthEndList")
    public ResponseEntity<List<StorageMonthEndInventoryDto>> findMonthEndList(@ApiParam(value = "查询对象") @RequestBody SearchStorageMonthEndInventory searchStorageMonthEndInventory) {
        Page<Object> page = PageHelper.startPage(searchStorageMonthEndInventory.getStartPage(), searchStorageMonthEndInventory.getPageSize());
        List<StorageMonthEndInventoryDto> list = storageMonthEndInventoryService.findMonthEndList(ControllerUtil.dynamicConditionByEntity(searchStorageMonthEndInventory));
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }


    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel", notes = "导出excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchStorageMonthEndInventory searchStorageMonthEndInventory) {
        List<StorageMonthEndInventoryDto> list = storageMonthEndInventoryService.findList(ControllerUtil.dynamicConditionByEntity(searchStorageMonthEndInventory));
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出月末库存信息", "月末库存信息", StorageMonthEndInventoryDto.class, "月末库存.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
