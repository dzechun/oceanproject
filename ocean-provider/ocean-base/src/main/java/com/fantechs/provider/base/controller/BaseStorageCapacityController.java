package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseStorageCapacity;
import com.fantechs.common.base.general.entity.basic.history.BaseHtStorageCapacity;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorageCapacity;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.base.service.BaseHtStorageCapacityService;
import com.fantechs.provider.base.service.BaseStorageCapacityService;
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
 * Created by leifengzhi on 2021/10/18.
 */
@RestController
@Api(tags = "库容信息")
@RequestMapping("/baseStorageCapacity")
@Validated
public class BaseStorageCapacityController {

    @Resource
    private BaseStorageCapacityService baseStorageCapacityService;
    @Resource
    private BaseHtStorageCapacityService baseHtStorageCapacityService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseStorageCapacity baseStorageCapacity) {
        return ControllerUtil.returnCRUD(baseStorageCapacityService.save(baseStorageCapacity));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseStorageCapacityService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseStorageCapacity.update.class) BaseStorageCapacity baseStorageCapacity) {
        return ControllerUtil.returnCRUD(baseStorageCapacityService.update(baseStorageCapacity));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseStorageCapacity> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseStorageCapacity  baseStorageCapacity = baseStorageCapacityService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseStorageCapacity,StringUtils.isEmpty(baseStorageCapacity)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseStorageCapacity>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseStorageCapacity searchBaseStorageCapacity) {
        Page<Object> page = PageHelper.startPage(searchBaseStorageCapacity.getStartPage(),searchBaseStorageCapacity.getPageSize());
        List<BaseStorageCapacity> list = baseStorageCapacityService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStorageCapacity));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<BaseStorageCapacity>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchBaseStorageCapacity searchBaseStorageCapacity) {
        List<BaseStorageCapacity> list = baseStorageCapacityService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStorageCapacity));
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtStorageCapacity>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseStorageCapacity searchBaseStorageCapacity) {
        Page<Object> page = PageHelper.startPage(searchBaseStorageCapacity.getStartPage(),searchBaseStorageCapacity.getPageSize());
        List<BaseHtStorageCapacity> list = baseHtStorageCapacityService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseStorageCapacity));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseStorageCapacity searchBaseStorageCapacity){
    List<BaseStorageCapacity> list = baseStorageCapacityService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseStorageCapacity));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "库容信息", BaseStorageCapacity.class, "库容信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
