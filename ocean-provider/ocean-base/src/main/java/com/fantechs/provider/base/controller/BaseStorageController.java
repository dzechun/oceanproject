package com.fantechs.provider.base.controller;


import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorage;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtStorageService;
import com.fantechs.provider.base.service.BaseStorageService;
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
 * Created by wcz on 2020/09/23.
 */
@RestController
@Api(tags = "储位信息管理")
@RequestMapping("/baseStorage")
@Validated
public class BaseStorageController {

    @Resource
    private BaseStorageService baseStorageService;

    @Resource
    private BaseHtStorageService baseHtStorageService;

    @ApiOperation(value = "批量更新", notes = "批量更新")
    @PostMapping("/batchUpdate")
    public ResponseEntity batchUpdate(@ApiParam(value = "储位集合", required = true) @RequestBody List<BaseStorage> baseStorages) {
        return ControllerUtil.returnCRUD(baseStorageService.batchUpdate(baseStorages));
    }

    @ApiOperation(value = "批量新增", notes = "批量新增")
    @PostMapping("/batchSave")
    public ResponseEntity batchAdd(@ApiParam(value = "储位集合", required = true) @RequestBody List<BaseStorage> baseStorages) {
        return ControllerUtil.returnCRUD(baseStorageService.batchSave(baseStorages));
    }


    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：storageCode、storageName", required = true) @RequestBody @Validated BaseStorage storage) {
        return ControllerUtil.returnCRUD(baseStorageService.save(storage));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔", required = true) @RequestParam @NotBlank(message = "ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseStorageService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传", required = true) @RequestBody @Validated(value = BaseStorage.update.class) BaseStorage storage) {
        return ControllerUtil.returnCRUD(baseStorageService.update(storage));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseStorage> detail(@ApiParam(value = "ID", required = true) @RequestParam @NotNull(message = "id不能为空") Long id) {
        BaseStorage storage = baseStorageService.selectByKey(id);
        return ControllerUtil.returnDataSuccess(storage, StringUtils.isEmpty(storage) ? 0 : 1);
    }

    @ApiOperation("根据条件查询信息列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseStorage>> findList(@ApiParam(value = "查询对象") @RequestBody SearchBaseStorage searchBaseStorage) {
        Page<Object> page = PageHelper.startPage(searchBaseStorage.getStartPage(), searchBaseStorage.getPageSize());
        List<BaseStorage> list = baseStorageService.findList(searchBaseStorage);
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    @ApiOperation("根据条件查询信息历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtStorage>> findHtList(@ApiParam(value = "查询对象") @RequestBody SearchBaseStorage searchBaseStorage) {
        Page<Object> page = PageHelper.startPage(searchBaseStorage.getStartPage(), searchBaseStorage.getPageSize());
        List<BaseHtStorage> list = baseHtStorageService.findHtList(searchBaseStorage);
        return ControllerUtil.returnDataSuccess(list, (int) page.getTotal());
    }

    /**
     * 导出数据
     *
     * @return
     * @throws
     */
    @PostMapping(value = "/export")
    @ApiOperation(value = "导出储位信息excel", notes = "导出储位信息excel", produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseStorage searchBaseStorage) {
        List<BaseStorage> list = baseStorageService.findList(searchBaseStorage);
        try {
            // 导出操作
            EasyPoiUtils.exportExcel(list, "导出储位信息", "储位信息", BaseStorage.class, "储位信息.xls", response);
        } catch (Exception e) {
            throw new BizErrorException(e);
        }
    }
}
