package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamReturnOrderDto;
import com.fantechs.common.base.general.entity.eam.EamReturnOrder;
import com.fantechs.common.base.general.entity.eam.history.EamHtReturnOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamReturnOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamHtReturnOrderService;
import com.fantechs.provider.eam.service.EamReturnOrderService;
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
 * Created by leifengzhi on 2021/06/29.
 */
@RestController
@Api(tags = "设备归还")
@RequestMapping("/eamReturnOrder")
@Validated
public class EamReturnOrderController {

    @Resource
    private EamReturnOrderService eamReturnOrderService;
    @Resource
    private EamHtReturnOrderService eamHtReturnOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamReturnOrder eamReturnOrder) {
        return ControllerUtil.returnCRUD(eamReturnOrderService.save(eamReturnOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamReturnOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamReturnOrder.update.class) EamReturnOrder eamReturnOrder) {
        return ControllerUtil.returnCRUD(eamReturnOrderService.update(eamReturnOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamReturnOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamReturnOrder  eamReturnOrder = eamReturnOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamReturnOrder,StringUtils.isEmpty(eamReturnOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamReturnOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamReturnOrder searchEamReturnOrder) {
        Page<Object> page = PageHelper.startPage(searchEamReturnOrder.getStartPage(),searchEamReturnOrder.getPageSize());
        List<EamReturnOrderDto> list = eamReturnOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamReturnOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtReturnOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamReturnOrder searchEamReturnOrder) {
        Page<Object> page = PageHelper.startPage(searchEamReturnOrder.getStartPage(),searchEamReturnOrder.getPageSize());
        List<EamHtReturnOrder> list = eamHtReturnOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamReturnOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamReturnOrder searchEamReturnOrder){
    List<EamReturnOrderDto> list = eamReturnOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamReturnOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "设备归还", EamReturnOrderDto.class, "设备归还.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
