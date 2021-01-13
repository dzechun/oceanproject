package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDeliveryOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutDeliveryOrderService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/01/09.
 */
@RestController
@Api(tags = "出库单控制器")
@RequestMapping("/wmsOutDeliveryOrder")
@Validated
public class WmsOutDeliveryOrderController {

    @Resource
    private WmsOutDeliveryOrderService wmsOutDeliveryOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutDeliveryOrder wmsOutDeliveryOrder) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderService.save(wmsOutDeliveryOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutDeliveryOrder.update.class) WmsOutDeliveryOrder wmsOutDeliveryOrder) {
        return ControllerUtil.returnCRUD(wmsOutDeliveryOrderService.update(wmsOutDeliveryOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutDeliveryOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutDeliveryOrder  wmsOutDeliveryOrder = wmsOutDeliveryOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutDeliveryOrder,StringUtils.isEmpty(wmsOutDeliveryOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutDeliveryOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDeliveryOrder searchWmsOutDeliveryOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsOutDeliveryOrder.getStartPage(),searchWmsOutDeliveryOrder.getPageSize());
        List<WmsOutDeliveryOrderDto> list = wmsOutDeliveryOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @ApiOperation("历史列表")
    @PostMapping("/findHtList")
    public ResponseEntity<List<WmsOutDeliveryOrderDto>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDeliveryOrder searchWmsOutDeliveryOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsOutDeliveryOrder.getStartPage(),searchWmsOutDeliveryOrder.getPageSize());
        List<WmsOutDeliveryOrderDto> list = wmsOutDeliveryOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrder));
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutDeliveryOrder searchWmsOutDeliveryOrder){
    List<WmsOutDeliveryOrderDto> list = wmsOutDeliveryOrderService.findList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDeliveryOrder));
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "出库单信息", WmsOutDeliveryOrderDto.class, "出库单.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }
}