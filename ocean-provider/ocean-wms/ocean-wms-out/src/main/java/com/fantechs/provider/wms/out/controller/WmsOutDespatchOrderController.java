package com.fantechs.provider.wms.out.controller;

import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.out.WmsOutDespatchOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDespatchOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutDespatchOrder;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.EasyPoiUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.wms.out.service.WmsOutDespatchOrderService;
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
 * Created by Mr.Lei on 2021/05/10.
 */
@RestController
@Api(tags = "装车作业表头")
@RequestMapping("/wmsOutDespatchOrder")
@Validated
public class WmsOutDespatchOrderController {

    @Resource
    private WmsOutDespatchOrderService wmsOutDespatchOrderService;

    @ApiOperation(value = "新增",notes = "新增")
    @PostMapping("/add")
    public ResponseEntity<String> add(@ApiParam(value = "必传：",required = true)@RequestBody @Validated WmsOutDespatchOrder wmsOutDespatchOrder) {
        return ControllerUtil.returnSuccess("操作成功",wmsOutDespatchOrderService.add(wmsOutDespatchOrder));
    }

    @ApiOperation("删除")
    @PostMapping("/delete")
    public ResponseEntity delete(@ApiParam(value = "对象ID列表，多个逗号分隔",required = true) @RequestParam @NotBlank(message="ids不能为空") String ids) {
        return ControllerUtil.returnCRUD(wmsOutDespatchOrderService.batchDelete(ids));
    }

    @ApiOperation("修改")
    @PostMapping("/update")
    public ResponseEntity update(@ApiParam(value = "对象，Id必传",required = true)@RequestBody @Validated(value=WmsOutDespatchOrder.update.class) WmsOutDespatchOrder wmsOutDespatchOrder) {
        return ControllerUtil.returnCRUD(wmsOutDespatchOrderService.update(wmsOutDespatchOrder));
    }

    @ApiOperation("获取详情")
    @PostMapping("/detail")
    public ResponseEntity<WmsOutDespatchOrder> detail(@ApiParam(value = "ID",required = true)@RequestParam  @NotNull(message="id不能为空") Long id) {
        WmsOutDespatchOrder  wmsOutDespatchOrder = wmsOutDespatchOrderService.selectByKey(id);
        return  ControllerUtil.returnDataSuccess(wmsOutDespatchOrder,StringUtils.isEmpty(wmsOutDespatchOrder)?0:1);
    }

    @ApiOperation("列表")
    @PostMapping("/findList")
    public ResponseEntity<List<WmsOutDespatchOrderDto>> findList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDespatchOrder searchWmsOutDespatchOrder) {
        Page<Object> page = PageHelper.startPage(searchWmsOutDespatchOrder.getStartPage(),searchWmsOutDespatchOrder.getPageSize());
        List<WmsOutDespatchOrderDto> list = wmsOutDespatchOrderService.findList(searchWmsOutDespatchOrder);
        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
    }

//    @ApiOperation("历史列表")
//    @PostMapping("/findHtList")
//    public ResponseEntity<List<WmsOutDespatchOrder>> findHtList(@ApiParam(value = "查询对象")@RequestBody SearchWmsOutDespatchOrder searchWmsOutDespatchOrder) {
//        Page<Object> page = PageHelper.startPage(searchWmsOutDespatchOrder.getStartPage(),searchWmsOutDespatchOrder.getPageSize());
//        List<WmsOutDespatchOrder> list = wmsOutDespatchOrderService.findHtList(ControllerUtil.dynamicConditionByEntity(searchWmsOutDespatchOrder));
//        return ControllerUtil.returnDataSuccess(list,(int)page.getTotal());
//    }

    @PostMapping(value = "/export")
    @ApiOperation(value = "导出excel",notes = "导出excel",produces = "application/octet-stream")
    public void exportExcel(HttpServletResponse response, @ApiParam(value = "查询对象")
    @RequestBody(required = false) SearchWmsOutDespatchOrder searchWmsOutDespatchOrder){
    List<WmsOutDespatchOrderDto> list = wmsOutDespatchOrderService.findList(searchWmsOutDespatchOrder);
    try {
        // 导出操作
        EasyPoiUtils.exportExcel(list, "导出信息", "WmsOutDespatchOrder信息", WmsOutDespatchOrderDto.class, "WmsOutDespatchOrder.xls", response);
        } catch (Exception e) {
        throw new BizErrorException(e);
        }
    }

    @ApiOperation("完成装车")
    @PostMapping("/finishTruckloading")
    public ResponseEntity finishTruckloading(@RequestParam @NotNull(message = "id不能为空") String ids){
        return ControllerUtil.returnCRUD(wmsOutDespatchOrderService.finishTruckloading(ids));
    }

    @ApiOperation("发运")
    @PostMapping("/forwarding")
    public ResponseEntity forwarding(@ApiParam("逗号间隔") @RequestParam @NotNull(message = "id不能为空") String ids){
        return ControllerUtil.returnCRUD(wmsOutDespatchOrderService.forwarding(ids));
    }

    @ApiOperation("批量新增")
    @PostMapping("/batchSave")
    public ResponseEntity batchSave(@RequestBody List<WmsOutDespatchOrder> list){
        return ControllerUtil.returnCRUD(wmsOutDespatchOrderService.batchSave(list));
    }
}
