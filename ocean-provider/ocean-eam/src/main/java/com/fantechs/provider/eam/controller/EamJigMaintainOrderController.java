package com.fantechs.provider.eam.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.eam.EamJigMaintainOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigMaintainOrder;
import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainOrder;
import com.fantechs.common.base.general.entity.eam.search.SearchEamJigMaintainOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.service.EamHtJigMaintainOrderService;
import com.fantechs.provider.eam.service.EamJigMaintainOrderService;
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
 * Created by leifengzhi on 2021/08/13.
 */
@RestController
@Api(tags = "治具保养单")
@RequestMapping("/eamJigMaintainOrder")
@Validated
public class EamJigMaintainOrderController {

    @Resource
    private EamJigMaintainOrderService eamJigMaintainOrderService;
    @Resource
    private EamHtJigMaintainOrderService eamHtJigMaintainOrderService;

    @ApiOperation("新建保养单")
    @PostMapping("/pdaCreateOrder")
    public ResponseEntity<EamJigMaintainOrder> pdaCreateOrder(@ApiParam(value = "治具条码",required = true)@RequestParam  @NotBlank(message="治具条码不能为空") String jigBarcode) {
        EamJigMaintainOrder  eamJigMaintainOrder = eamJigMaintainOrderService.pdaCreateOrder(jigBarcode);
        return  ControllerUtil.returnDataSuccess(eamJigMaintainOrder,StringUtils.isEmpty(eamJigMaintainOrder)?0:1);
    }

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated EamJigMaintainOrder eamJigMaintainOrder) {
        return ControllerUtil.returnCRUD(eamJigMaintainOrderService.save(eamJigMaintainOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(eamJigMaintainOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=EamJigMaintainOrder.update.class) EamJigMaintainOrder eamJigMaintainOrder) {
        return ControllerUtil.returnCRUD(eamJigMaintainOrderService.update(eamJigMaintainOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<EamJigMaintainOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        EamJigMaintainOrder  eamJigMaintainOrder = eamJigMaintainOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(eamJigMaintainOrder,StringUtils.isEmpty(eamJigMaintainOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<EamJigMaintainOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigMaintainOrder searchEamJigMaintainOrder) {
        Page<Object> page = PageHelper.startPage(searchEamJigMaintainOrder.getStartPage(),searchEamJigMaintainOrder.getPageSize());
        List<EamJigMaintainOrderDto> list = eamJigMaintainOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaintainOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<EamHtJigMaintainOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchEamJigMaintainOrder searchEamJigMaintainOrder) {
        Page<Object> page = PageHelper.startPage(searchEamJigMaintainOrder.getStartPage(),searchEamJigMaintainOrder.getPageSize());
        List<EamHtJigMaintainOrder> list = eamHtJigMaintainOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaintainOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchEamJigMaintainOrder searchEamJigMaintainOrder){
    List<EamJigMaintainOrderDto> list = eamJigMaintainOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchEamJigMaintainOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "治具保养单", EamJigMaintainOrderDto.class, "治具保养单.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}
