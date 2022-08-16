package com.fantechs.provider.base.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.basic.BaseInventoryStatus;
import com.fantechs.common.base.general.entity.basic.history.BaseHtInventoryStatus;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseInventoryStatus;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;

import com.fantechs.provider.base.service.BaseHtInventoryStatusService;
import com.fantechs.provider.base.service.BaseInventoryStatusService;
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
 * Created by leifengzhi on 2021/04/25.
 */
@RestController
@Api(tags = "库存状态")
@RequestMapping("/baseInventoryStatus")
@Validated
public class BaseInventoryStatusController {

    @Resource
    private BaseInventoryStatusService baseInventoryStatusService;
    @Resource
    private BaseHtInventoryStatusService baseHtInventoryStatusService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated BaseInventoryStatus baseInventoryStatus) {
        return ControllerUtil.returnCRUD(baseInventoryStatusService.save(baseInventoryStatus));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(baseInventoryStatusService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=BaseInventoryStatus.update.class) BaseInventoryStatus baseInventoryStatus) {
        return ControllerUtil.returnCRUD(baseInventoryStatusService.update(baseInventoryStatus));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<BaseInventoryStatus> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        BaseInventoryStatus  baseInventoryStatus = baseInventoryStatusService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(baseInventoryStatus,StringUtils.isEmpty(baseInventoryStatus)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<BaseInventoryStatus>> findList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInventoryStatus searchBaseInventoryStatus) {
        Page<Object> page = PageHelper.startPage(searchBaseInventoryStatus.getStartPage(),searchBaseInventoryStatus.getPageSize());
        List<BaseInventoryStatus> list = baseInventoryStatusService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInventoryStatus));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<BaseHtInventoryStatus>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchBaseInventoryStatus searchBaseInventoryStatus) {
        Page<Object> page = PageHelper.startPage(searchBaseInventoryStatus.getStartPage(),searchBaseInventoryStatus.getPageSize());
        List<BaseHtInventoryStatus> list = baseHtInventoryStatusService.findHtList(ControllerUtil.dynamicConditionByEntity(searchBaseInventoryStatus));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchBaseInventoryStatus searchBaseInventoryStatus){
    List<BaseInventoryStatus> list = baseInventoryStatusService.findList(ControllerUtil.dynamicConditionByEntity(searchBaseInventoryStatus));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "库存状态信息", BaseInventoryStatus.class, "库存状态信息.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
