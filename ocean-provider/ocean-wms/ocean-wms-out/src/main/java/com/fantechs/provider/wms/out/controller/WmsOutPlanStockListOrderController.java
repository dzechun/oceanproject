package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDetDto;
import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutPlanStockListOrder;

import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutPlanStockListOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/12/22.
 */
@RestController
@Api(tags = "备料计划控制器")
@RequestMapping("/wmsOutPlanStockListOrder")
@Validated
public class WmsOutPlanStockListOrderController {

    @Resource
    private WmsOutPlanStockListOrderService wmsOutPlanStockListOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutPlanStockListOrderDto wmsOutPlanStockListOrderDto) {
        return ControllerUtil.returnCRUD(wmsOutPlanStockListOrderService.save(wmsOutPlanStockListOrderDto));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutPlanStockListOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutPlanStockListOrderDto.update.class) WmsOutPlanStockListOrderDto wmsOutPlanStockListOrderDto) {
        return ControllerUtil.returnCRUD(wmsOutPlanStockListOrderService.update(wmsOutPlanStockListOrderDto));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutPlanStockListOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutPlanStockListOrder  wmsOutPlanStockListOrder = wmsOutPlanStockListOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutPlanStockListOrder,StringUtils.isEmpty(wmsOutPlanStockListOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutPlanStockListOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutPlanStockListOrder searchWmsOutPlanStockListOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsOutPlanStockListOrder.getStartPage(),searchWmsOutPlanStockListOrder.getPageSize());
        List<WmsOutPlanStockListOrderDto> list = wmsOutPlanStockListOrderService.findList(searchWmsOutPlanStockListOrder);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("列表(不分页)")
    @PostMapping("/findAll")
    public ResponseEntity<List<WmsOutPlanStockListOrderDto>> findAll(@ApiParam(value = "查询对象") @RequestBody SearchWmsOutPlanStockListOrder searchWmsOutPlanStockListOrder) {
        List<WmsOutPlanStockListOrderDto> list = wmsOutPlanStockListOrderService.findList(searchWmsOutPlanStockListOrder);
        return ControllerUtil.returnDataSuccess(list, list.size());
    }

    @ApiOperation(value = "下推",notes = "下推")
    @PostMapping("/pushDown")
    public ResponseEntity pushDown(@ApiParam(value = "必传：",required = true)@RequestBody @Validated @NotEmpty List<WmsOutPlanStockListOrderDetDto> wmsOutPlanStockListOrderDetDtos) {
        return ControllerUtil.returnCRUD(wmsOutPlanStockListOrderService.pushDown(wmsOutPlanStockListOrderDetDtos));
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutPlanStockListOrder searchWmsOutPlanStockListOrder){
    List<WmsOutPlanStockListOrderDto> list = wmsOutPlanStockListOrderService.findList(searchWmsOutPlanStockListOrder);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsOutPlanStockListOrder信息", WmsOutPlanStockListOrderDto.class, "WmsOutPlanStockListOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

}
